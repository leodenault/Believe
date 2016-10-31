package musicGame.character;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CharacterStateMachine {
	public enum Action {
		SELECT_RIGHT, SELECT_LEFT, STOP, JUMP, LAND
	}

	public enum State {
		GROUNDED_STATIONARY, GROUNDED_MOVING_RIGHT, GROUNDED_MOVING_LEFT,
		AIRBORNE_STATIONARY, AIRBORNE_MOVING_RIGHT, AIRBORNE_MOVING_LEFT;
	}
	
	@SuppressWarnings("serial")
	private static final Map<State, Map<Action, State>> TRANSITIONS = Collections.unmodifiableMap(
			new HashMap<State, Map<Action, State>>() {{
				put(State.GROUNDED_STATIONARY, new HashMap<Action, State>() {{
					put(Action.SELECT_LEFT, State.GROUNDED_MOVING_LEFT);
					put(Action.SELECT_RIGHT, State.GROUNDED_MOVING_RIGHT);
					put(Action.JUMP, State.AIRBORNE_STATIONARY);
				}});
				put(State.GROUNDED_MOVING_RIGHT, new HashMap<Action, State>() {{
					put(Action.STOP, State.GROUNDED_STATIONARY);
					put(Action.JUMP, State.AIRBORNE_MOVING_RIGHT);
				}});
				put(State.GROUNDED_MOVING_LEFT, new HashMap<Action, State>() {{
					put(Action.STOP, State.GROUNDED_STATIONARY);
					put(Action.JUMP, State.AIRBORNE_MOVING_LEFT);
				}});
				put(State.AIRBORNE_STATIONARY, new HashMap<Action, State>() {{
					put(Action.SELECT_LEFT, State.AIRBORNE_MOVING_LEFT);
					put(Action.SELECT_RIGHT, State.AIRBORNE_MOVING_RIGHT);
					put(Action.LAND, State.GROUNDED_STATIONARY);
				}});
				put(State.AIRBORNE_MOVING_RIGHT, new HashMap<Action, State>() {{
					put(Action.STOP, State.AIRBORNE_STATIONARY);
					put(Action.LAND, State.GROUNDED_MOVING_RIGHT);
				}});
				put(State.AIRBORNE_MOVING_LEFT, new HashMap<Action, State>() {{
					put(Action.STOP, State.AIRBORNE_STATIONARY);
					put(Action.LAND, State.GROUNDED_MOVING_LEFT);
				}});
	}});
	
	private State currentState;
	
	public CharacterStateMachine(State initialState) {
		currentState = initialState;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public State transition(Action action) {
		Map<Action, State> transitions = TRANSITIONS.get(currentState);
		if (transitions != null) {
			State state = transitions.get(action);
			if (state != null) {
				currentState = state;
			}
		}
		return currentState;
	}
}
