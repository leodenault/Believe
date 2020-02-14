package believe.character;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static believe.util.Util.hashSetOf;

import believe.character.playable.proto.PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand.Action;
import believe.core.Updatable;
import believe.core.display.AnimationSet;
import believe.core.display.SpriteSheetManager;
import believe.gui.ComponentBase;
import believe.map.collidable.tile.CollidableTileCollisionHandler.TileCollidable;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.damage.DamageBoxCollidable;
import believe.physics.manager.PhysicsManageable;
import believe.physics.manager.PhysicsManager;
import believe.statemachine.ConcurrentStateMachine;
import believe.statemachine.State;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Character<C extends Character<C>> extends ComponentBase
    implements TileCollidable<C>,
        DamageBoxCollidable<C>,
        PhysicsManageable,
        Updatable,
        ConcurrentStateMachine.Listener<Action> {

  public interface DamageListener {
    DamageListener NONE = (currentFocus, inflictor) -> {};

    void damageInflicted(float currentFocus, Faction inflictor);
  }

  public enum Orientation {
    RIGHT,
    LEFT
  }

  private static final float JUMP_SPEED = -0.5f;
  private static final float SCROLL_SPEED = 0.2f; // Pixels per millisecond

  public static final float MAX_FOCUS = 1.0f;

  protected final State<Action> standingState;
  protected final State<Action> movingLeftState;
  protected final State<Action> movingRightState;
  protected final State<Action> groundedState;
  protected final State<Action> jumpingState;
  protected final ConcurrentStateMachine<Action> machine;

  private final Map<Set<State<Action>>, String> animationMap;
  private final DamageListener damageListener;
  private final Set<CollisionHandler<? extends Collidable<?>, ? super C>> rightCompatibleHandlers;

  private float focus;
  private float verticalSpeed;

  protected Orientation orientation;
  protected AnimationSet animSet;
  protected Animation anim;
  protected float horizontalSpeed;

  public Character(
      GUIContext container,
      DamageListener damageListener,
      Set<CollisionHandler<? extends Collidable<?>, ? super C>> rightCompatibleHandlers,
      int x,
      int y) {
    super(container, x, y);
    this.damageListener = damageListener;
    this.rightCompatibleHandlers = rightCompatibleHandlers;
    orientation = Orientation.RIGHT;
    verticalSpeed = 0;
    horizontalSpeed = 0;
    animSet = SpriteSheetManager.getInstance().getAnimationSet(getSheetName());
    anim = animSet.get("idle");
    rect =
        new believe.geometry.Rectangle(x, y - anim.getHeight(), anim.getWidth(), anim.getHeight());
    anim.stop();
    anim.setCurrentFrame(0);
    focus = MAX_FOCUS;

    standingState = new State<>();
    movingLeftState = new State<>();
    movingRightState = new State<>();
    groundedState = new State<>();
    jumpingState = new State<>();
    buildStateMachine();
    machine =
        new ConcurrentStateMachine<>(new HashSet<>(Arrays.asList(standingState, groundedState)));
    machine.addListener(this);

    animationMap =
        hashMapOf(
            entry(hashSetOf(standingState, groundedState), "idle"),
            entry(hashSetOf(movingLeftState, groundedState), "move"),
            entry(hashSetOf(movingRightState, groundedState), "move"),
            entry(hashSetOf(standingState, jumpingState), "jump"),
            entry(hashSetOf(movingLeftState, jumpingState), "jump"),
            entry(hashSetOf(movingRightState, jumpingState), "jump"));
  }

  private void buildStateMachine() {
    standingState.addTransition(
        Action.SELECT_LEFT,
        () -> updateHorizontalMovement(-1),
        movingLeftState.addTransition(
            Action.STOP, () -> updateHorizontalMovement(0), standingState));
    standingState.addTransition(
        Action.SELECT_RIGHT,
        () -> updateHorizontalMovement(1),
        movingRightState.addTransition(
            Action.STOP, () -> updateHorizontalMovement(0), standingState));
    groundedState.addTransition(
        Action.JUMP,
        () -> verticalSpeed = JUMP_SPEED,
        jumpingState.addTransition(Action.LAND, groundedState));
  }

  @Override
  public float getFloatX() {
    return rect.getX();
  }

  @Override
  public float getFloatY() {
    return rect.getY();
  }

  @Override
  public void setLocation(float x, float y) {
    if (rect != null) {
      rect.setLocation(x, y);
      resetLayout();
    }
  }

  @Override
  public void landed() {
    machine.transition(Action.LAND);
  }

  @Override
  public void resetLayout() {}

  @Override
  public float getVerticalSpeed() {
    return verticalSpeed;
  }

  @Override
  public void setVerticalSpeed(float speed) {
    verticalSpeed = speed;
  }

  @Override
  protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
    anim.getCurrentFrame()
        .getFlippedCopy(orientation == Orientation.LEFT, false)
        .draw(rect.getX(), rect.getY());
  }

  @Override
  public Set<CollisionHandler<? super C, ? extends Collidable<?>>> leftCompatibleHandlers() {
    return Collections.emptySet();
  }

  @Override
  public Set<CollisionHandler<? extends Collidable<?>, ? super C>> rightCompatibleHandlers() {
    return rightCompatibleHandlers;
  }

  public float getFocus() {
    return focus;
  }

  @Override
  public void inflictDamage(float damage, Faction inflictor) {
    focus = Math.max(0f, focus - damage);
    damageListener.damageInflicted(focus, inflictor);
  }

  public void heal(float health) {
    focus = Math.min(MAX_FOCUS, focus + health);
  }

  public void transition(Action action) {
    machine.transition(action);
  }

  private void updateHorizontalMovement(int orientation) {
    if (orientation < -1 || orientation > 1) {
      throw new IllegalArgumentException("Direction supplied must be +/- 1 or 0.");
    }
    horizontalSpeed = orientation * SCROLL_SPEED;
    if (orientation != 0) {
      this.orientation = orientation > 0 ? Orientation.RIGHT : Orientation.LEFT;
    }
  }

  @Override
  public void update(int delta) {
    if (horizontalSpeed != 0) {
      setLocation(getFloatX() + delta * horizontalSpeed, getFloatY());
    }
    anim.update(delta);
  }

  @Override
  public void transitionEnded(Set<State<Action>> currentStates) {
    anim = animSet.get(animationMap.get(currentStates));
  }

  @Override
  public void addToPhysicsManager(PhysicsManager physicsManager) {
    physicsManager.addCollidable(this);
    physicsManager.addGravityObject(this);
  }

  protected abstract String getSheetName();
}
