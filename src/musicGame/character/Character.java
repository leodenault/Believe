package musicGame.character;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import musicGame.core.AnimationSet;
import musicGame.core.Camera;
import musicGame.core.SpriteSheetManager;
import musicGame.gui.ComponentBase;
import musicGame.physics.Collidable;
import musicGame.physics.DamageBoxCollidable;
import musicGame.physics.DamageHandler;
import musicGame.physics.TileCollisionHandler;
import musicGame.physics.TileCollisionHandler.TileCollidable;
import musicGame.statemachine.EntityStateMachine;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public abstract class Character extends ComponentBase implements TileCollidable, DamageBoxCollidable {

	public enum Action {
		SELECT_RIGHT, SELECT_LEFT, STOP, JUMP, LAND
	}

	public enum State {
		GROUNDED_STATIONARY("idle"), GROUNDED_MOVING_RIGHT("move"), GROUNDED_MOVING_LEFT("move"),
		AIRBORNE_STATIONARY("jump"), AIRBORNE_MOVING_RIGHT("jump"), AIRBORNE_MOVING_LEFT("jump");

		private String animation;

		private State(String animation) {
			this.animation = animation;
		}

		public String getAnimation() {
			return animation;
		}

		public boolean movingLeft() {
			return this == GROUNDED_MOVING_LEFT || this == AIRBORNE_MOVING_LEFT;
		}

		public boolean movingRight() {
			return this == GROUNDED_MOVING_RIGHT || this == AIRBORNE_MOVING_RIGHT;
		}

		public boolean stationary() {
			return this == GROUNDED_STATIONARY || this == AIRBORNE_STATIONARY;
		}
	}

	private static final float JUMP_SPEED = -0.5f;

	@SuppressWarnings("serial")
	private final Map<State, Map<Action, Function<Integer, State>>> TRANSITIONS =
			Collections.unmodifiableMap(new HashMap<State, Map<Action, Function<Integer, State>>>() {{
				put(State.GROUNDED_STATIONARY, new HashMap<Action, Function<Integer, State>>() {{
					put(Action.SELECT_LEFT,
						createHorizontalMovementCallback(-1, State.GROUNDED_MOVING_LEFT));
					put(Action.SELECT_RIGHT,
						createHorizontalMovementCallback(1, State.GROUNDED_MOVING_RIGHT));
					put(Action.JUMP, key -> {
						verticalSpeed = JUMP_SPEED;
						anim = animSet.get(State.AIRBORNE_STATIONARY.getAnimation());
						return State.AIRBORNE_STATIONARY;
					});
				}});
				put(State.GROUNDED_MOVING_RIGHT, new HashMap<Action, Function<Integer, State>>() {{
					put(Action.STOP,
						createHorizontalMovementCallback(0, State.GROUNDED_STATIONARY));
					put(Action.JUMP, key -> {
						verticalSpeed = JUMP_SPEED;
						anim = animSet.get(State.AIRBORNE_MOVING_RIGHT.getAnimation());
						return State.AIRBORNE_MOVING_RIGHT;
					});
				}});
				put(State.GROUNDED_MOVING_LEFT, new HashMap<Action, Function<Integer, State>>() {{
					put(Action.STOP,
						createHorizontalMovementCallback(0, State.GROUNDED_STATIONARY));
					put(Action.JUMP, key -> {
						verticalSpeed = JUMP_SPEED;
						anim = animSet.get(State.AIRBORNE_MOVING_LEFT.getAnimation());
						return State.AIRBORNE_MOVING_LEFT;
					});
				}});
				put(State.AIRBORNE_STATIONARY, new HashMap<Action, Function<Integer, State>>() {{
					put(Action.SELECT_LEFT,
						createHorizontalMovementCallback(-1, State.AIRBORNE_MOVING_LEFT));
					put(Action.SELECT_RIGHT,
						createHorizontalMovementCallback(1, State.AIRBORNE_MOVING_RIGHT));
					put(Action.LAND, key -> {
						anim = animSet.get(State.GROUNDED_STATIONARY.getAnimation());
						return State.GROUNDED_STATIONARY;
					});
				}});
				put(State.AIRBORNE_MOVING_RIGHT, new HashMap<Action, Function<Integer, State>>() {{
					put(Action.STOP,
						createHorizontalMovementCallback(0, State.AIRBORNE_STATIONARY));
					put(Action.LAND, key -> {
						anim = animSet.get(State.GROUNDED_MOVING_RIGHT.getAnimation());
						return State.GROUNDED_MOVING_RIGHT;
					});
				}});
				put(State.AIRBORNE_MOVING_LEFT, new HashMap<Action, Function<Integer, State>>() {{
					put(Action.STOP,
						createHorizontalMovementCallback(0, State.AIRBORNE_STATIONARY));
					put(Action.LAND, key -> {
						anim = animSet.get(State.GROUNDED_MOVING_LEFT.getAnimation());
						return State.GROUNDED_MOVING_LEFT;
					});
				}});
		}});

	public static final float MAX_FOCUS = 1.0f;

	private float focus;
	private int direction;
	private float verticalSpeed;
	private float horizontalSpeed;
	private TileCollisionHandler tileHandler;
	private DamageHandler damageHandler;

	protected EntityStateMachine<Action, State, Integer> machine;
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
		machine = new EntityStateMachine<>(TRANSITIONS, State.GROUNDED_STATIONARY);
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

	private Function<Integer, State> createHorizontalMovementCallback(
			int orientation, State state) {
		if (!(orientation == -1 || orientation == 1 || orientation == 0)) {
			throw new IllegalArgumentException("Direction supplied must be +/- 1 or 0.");
		}

		return key -> {
			horizontalSpeed = orientation * Camera.SCROLL_SPEED;
			direction = orientation == 0 ? direction : orientation;
			anim = animSet.get(state.getAnimation());
			return state;
		};
	}

	public void update(int delta) {
		if (horizontalSpeed != 0) {
			setLocation(getFloatX() + delta * horizontalSpeed, getFloatY());
		}
		anim.update(delta);
	}

	protected abstract String getSheetName();
}