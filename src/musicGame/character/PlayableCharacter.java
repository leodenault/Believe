package musicGame.character;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.Camera;
import musicGame.core.EntityStateMachine;
import musicGame.core.Music;
import musicGame.core.SynchedComboPattern;
import musicGame.physics.Collidable;
import musicGame.physics.CommandCollidable;
import musicGame.physics.CommandCollisionHandler;
import musicGame.physics.DamageHandler.Faction;

public class PlayableCharacter extends Character implements  CommandCollidable {
	public interface SynchedComboListener {
		void activateCombo(SynchedComboPattern pattern);
	}
	
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
	private static final float MAX_FOCUS = 1.0f;
	private static final float FOCUS_RECHARGE_TIME = 60f; // Time in seconds for recharging focus fully
	private static final float FOCUS_RECHARGE_RATE = MAX_FOCUS / (FOCUS_RECHARGE_TIME * 1000f);
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
	
	private boolean onRails;
	private int direction;
	private float verticalSpeed;
	private float horizontalSpeed;
	private float focus;
	private SynchedComboPattern pattern;
	private List<SynchedComboListener> comboListeners;
	private EntityStateMachine<Action, State, Integer> machine;
	private CommandCollisionHandler commandHandler;
	
	public PlayableCharacter(GUIContext container, Music music, boolean onRails, int x, int y)
			throws SlickException {
		super(container, x, y);
		this.onRails = onRails;
		direction = 1;
		verticalSpeed = 0;
		horizontalSpeed = 0;
		focus = MAX_FOCUS;
		pattern = new SynchedComboPattern();
		pattern.addAction(0, 's');
		pattern.addAction(1, 's');
		pattern.addAction(2, 'a');
		pattern.addAction(2.5f, 'd');
		pattern.addAction(3, 'a');
		comboListeners = new LinkedList<SynchedComboListener>();
		machine = new EntityStateMachine<>(TRANSITIONS, State.GROUNDED_STATIONARY);
		commandHandler = new CommandCollisionHandler();
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
	public void landed() {
		machine.transition(Action.LAND);
	}
	
	public float getFocus() {
		return focus;
	}
	
	public void addComboListener(SynchedComboListener listener) {
		this.comboListeners.add(listener);
	}
	
	public void update(int delta) {
		if (horizontalSpeed != 0) {
			setLocation(getFloatX() + delta * horizontalSpeed, getFloatY());
		}
		
		focus = Math.min(MAX_FOCUS, focus + (delta * FOCUS_RECHARGE_RATE));
		anim.update(delta);
	}

	@Override
	public void collision(Collidable other) {
		super.collision(other);
		if (onRails) {
			commandHandler.handleCollision(this, other);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (!onRails) {
			Action action = null;
			switch (key) {
				case Input.KEY_LEFT:
					action = machine.getCurrentState().movingRight() ?
							Action.STOP : Action.SELECT_LEFT;
					break;
				case Input.KEY_RIGHT:
					action = machine.getCurrentState().movingLeft() ?
							Action.STOP : Action.SELECT_RIGHT;
					break;
				case Input.KEY_SPACE:
					action = Action.JUMP;
					break;
			}

			if (action != null) {
				machine.transition(action, key);
			}

			if (key == Input.KEY_C) {
				for (SynchedComboListener listener : comboListeners) {
					listener.activateCombo(pattern);
				}
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		if (!onRails) {
			Action action = null;
			switch (key) {
				case Input.KEY_LEFT:
					action = machine.getCurrentState().movingLeft() ?
							Action.STOP : Action.SELECT_RIGHT;
					break;
				case Input.KEY_RIGHT:
					action = machine.getCurrentState().movingRight() ?
							Action.STOP : Action.SELECT_LEFT;
					break;
			}

			if (action != null) {
				machine.transition(action, key);
			}
		}
	}
	
	@Override
	protected String getSheetName() {
		return "stickFigure";
	}
	
	@Override
	protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
		anim.getCurrentFrame().getFlippedCopy(direction == -1, false).draw(rect.getX(), rect.getY());
	}

	@Override
	public Faction getFaction() {
		return Faction.GOOD;
	}

	@Override
	public void inflictDamage(float damage) {
		focus -= damage;
	}
	
	@Override
	public void executeCommand(Action command) {
		machine.transition(command);
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
}
