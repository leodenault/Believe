package musicGame.statemachine;

import java.util.HashMap;
import java.util.Map;

public class State {
	public static enum Action {
		SELECT_RIGHT, SELECT_LEFT, STOP, JUMP, LAND
	}

	private Map<Action, Transition> transitions;

	public State () {
		transitions = new HashMap<Action, Transition>();
	}

	public State addTransition(Action action, Transition transition) {
		transitions.put(action, transition);
		return this;
	}

	public State transition(Action action) {
		if (transitions.containsKey(action)) {
			return transitions.get(action).execute();
		}
		return this;
	}
}
