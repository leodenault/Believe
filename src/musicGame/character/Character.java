package musicGame.character;

import static musicGame.core.Util.hashSetOf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.AnimationSet;
import musicGame.core.Camera;
import musicGame.core.SpriteSheetManager;
import musicGame.gui.ComponentBase;
import musicGame.physics.Collidable;
import musicGame.physics.DamageBoxCollidable;
import musicGame.physics.DamageHandler;
import musicGame.physics.TileCollisionHandler;
import musicGame.physics.TileCollisionHandler.TileCollidable;
import musicGame.statemachine.ConcurrentStateMachine;
import musicGame.statemachine.State;
import musicGame.statemachine.State.Action;

public abstract class Character
		extends ComponentBase
		implements TileCollidable, DamageBoxCollidable, ConcurrentStateMachine.Listener {
	protected static final State STANDING = new State();
	protected static final State MOVING_LEFT = new State();
	protected static final State MOVING_RIGHT = new State();
	{{
		STANDING.addTransition(
				Action.SELECT_LEFT,
				() -> updateHorizontalMovement(-1),
				MOVING_LEFT.addTransition(
						Action.STOP,
						() -> updateHorizontalMovement(0),
						STANDING));
		STANDING.addTransition(
				Action.SELECT_RIGHT,
				() -> updateHorizontalMovement(1),
				MOVING_RIGHT.addTransition(
						Action.STOP,
						() -> updateHorizontalMovement(0),
						STANDING));
	}};
	protected static final State GROUNDED = new State();
	protected static final State JUMPING = new State();
	{{
		GROUNDED.addTransition(
				Action.JUMP,
				() -> { verticalSpeed = JUMP_SPEED; },
				JUMPING.addTransition(Action.LAND, GROUNDED));
	}};
	
	private static final float JUMP_SPEED = -0.5f;
	private static final Set<State> STATES = new HashSet<State>(Arrays.asList(STANDING, GROUNDED));
	@SuppressWarnings("serial")
	private static final Map<Set<State>, String> ANIMATION_MAP =
			new HashMap<Set<State>, String>() {{
		put(hashSetOf(STANDING, GROUNDED), "idle");
		put(hashSetOf(MOVING_LEFT, GROUNDED), "move");
		put(hashSetOf(MOVING_RIGHT, GROUNDED), "move");
		put(hashSetOf(STANDING, JUMPING), "jump");
		put(hashSetOf(MOVING_LEFT, JUMPING), "jump");
		put(hashSetOf(MOVING_RIGHT, JUMPING), "jump");
	}};

	public static final float MAX_FOCUS = 1.0f;

	private float focus;
	private int direction;
	private float verticalSpeed;
	private float horizontalSpeed;
	private TileCollisionHandler tileHandler;
	private DamageHandler damageHandler;

	protected ConcurrentStateMachine machine;
	protected AnimationSet animSet;
	protected Animation anim;

	public Character(GUIContext container, int x, int y) throws SlickException {
		super(container, x, y);
		direction = 1;
		verticalSpeed = 0;
		horizontalSpeed = 0;
		tileHandler = new TileCollisionHandler();
		damageHandler = new DamageHandler();
		animSet = SpriteSheetManager.getInstance().getAnimationSet(getSheetName());
		anim = animSet.get("idle");
		rect = new musicGame.geometry.Rectangle(
				x, y - anim.getHeight(), anim.getWidth(), anim.getHeight());
		anim.stop();
		anim.setCurrentFrame(0);
		machine = new ConcurrentStateMachine(STATES);
		machine.addListener(this);
		focus = MAX_FOCUS;
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
	protected void resetLayout() {}

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
			.getFlippedCopy(direction == -1, false)
			.draw(rect.getX(), rect.getY());
	}

	public float getFocus() {
		return focus;
	}

	@Override
	public void inflictDamage(float damage) {
		focus = Math.max(0f, focus - damage);
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
			direction = orientation;
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
		anim = animSet.get(ANIMATION_MAP.get(currentStates));
	}

	protected abstract String getSheetName();
}