package believe.character;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static believe.util.Util.hashSetOf;

import believe.core.display.AnimationSet;
import believe.core.display.Camera;
import believe.core.display.SpriteSheetManager;
import believe.gui.ComponentBase;
import believe.physics.collision.Collidable;
import believe.physics.collision.DamageBoxCollidable;
import believe.physics.collision.DamageHandler;
import believe.physics.collision.TileCollisionHandler;
import believe.physics.collision.TileCollisionHandler.TileCollidable;
import believe.statemachine.ConcurrentStateMachine;
import believe.statemachine.State;
import believe.statemachine.State.Action;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Character extends ComponentBase
    implements TileCollidable, DamageBoxCollidable, ConcurrentStateMachine.Listener {

  public interface DamageListener {
    DamageListener NONE = (currentFocus, inflictor) -> {};

    void damageInflicted(float currentFocus, Faction inflictor);
  }

  public enum Orientation {
    RIGHT, LEFT
  }

  private static final float JUMP_SPEED = -0.5f;

  public static final float MAX_FOCUS = 1.0f;

  protected final State standingState;
  protected final State movingLeftState;
  protected final State movingRightState;
  protected final State groundedState;
  protected final State jumpingState;
  protected final ConcurrentStateMachine machine;

  private final Map<Set<State>, String> animationMap;
  private final DamageListener damageListener;

  private float focus;
  private float verticalSpeed;
  private TileCollisionHandler tileHandler;
  private DamageHandler damageHandler;

  protected Orientation orientation;
  protected AnimationSet animSet;
  protected Animation anim;
  protected float horizontalSpeed;

  public Character(GUIContext container, DamageListener damageListener, int x, int y) {
    super(container, x, y);
    this.damageListener = damageListener;
    orientation = Orientation.RIGHT;
    verticalSpeed = 0;
    horizontalSpeed = 0;
    tileHandler = new TileCollisionHandler();
    damageHandler = new DamageHandler();
    animSet = SpriteSheetManager.getInstance().getAnimationSet(getSheetName());
    anim = animSet.get("idle");
    rect =
        new believe.geometry.Rectangle(x, y - anim.getHeight(), anim.getWidth(), anim.getHeight());
    anim.stop();
    anim.setCurrentFrame(0);
    focus = MAX_FOCUS;

    standingState = new State();
    movingLeftState = new State();
    movingRightState = new State();
    groundedState = new State();
    jumpingState = new State();
    buildStateMachine();
    machine =
        new ConcurrentStateMachine(new HashSet<>(Arrays.asList(standingState, groundedState)));
    machine.addListener(this);

    animationMap = hashMapOf(entry(hashSetOf(standingState, groundedState), "idle"),
        entry(hashSetOf(movingLeftState, groundedState), "move"),
        entry(hashSetOf(movingRightState, groundedState), "move"),
        entry(hashSetOf(standingState, jumpingState), "jump"),
        entry(hashSetOf(movingLeftState, jumpingState), "jump"),
        entry(hashSetOf(movingRightState, jumpingState), "jump"));
  }

  private void buildStateMachine() {
    standingState.addTransition(Action.SELECT_LEFT,
        () -> updateHorizontalMovement(-1),
        movingLeftState.addTransition(Action.STOP,
            () -> updateHorizontalMovement(0),
            standingState));
    standingState.addTransition(Action.SELECT_RIGHT,
        () -> updateHorizontalMovement(1),
        movingRightState.addTransition(Action.STOP,
            () -> updateHorizontalMovement(0),
            standingState));
    groundedState.addTransition(Action.JUMP,
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
  public void collision(Collidable other) {
    tileHandler.handleCollision(this, other);
    damageHandler.handleCollision(this, other);
  }

  @Override
  public CollidableType getType() {
    return CollidableType.CHARACTER;
  }

  @Override
  public void landed() {
    machine.transition(Action.LAND);
  }

  @Override
  public void resetLayout() {
  }

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
    anim
        .getCurrentFrame()
        .getFlippedCopy(orientation == Orientation.LEFT, false)
        .draw(rect.getX(), rect.getY());
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

  private void updateHorizontalMovement(int orientation) {
    if (orientation < -1 || orientation > 1) {
      throw new IllegalArgumentException("Direction supplied must be +/- 1 or 0.");
    }
    horizontalSpeed = orientation * Camera.SCROLL_SPEED;
    if (orientation != 0) {
      this.orientation = orientation > 0 ? Orientation.RIGHT : Orientation.LEFT;
    }
  }

  public void update(int delta) {
    if (horizontalSpeed != 0) {
      setLocation(getFloatX() + delta * horizontalSpeed, getFloatY());
    }
    anim.update(delta);
  }

  @Override
  public void transitionEnded(Set<State> currentStates) {
    anim = animSet.get(animationMap.get(currentStates));
  }

  protected abstract String getSheetName();
}