package musicGame.statemachine;

import java.util.HashMap;
import java.util.Map;

public class State {
	public static enum Action {
		SELECT_RIGHT, SELECT_LEFT, STOP, JUMP, LAND
	}

	public static class Builder {
		private Map<Action, Transition> transitions;

		private Builder() {
			transitions = new HashMap<Action, Transition>();
		};

		public Builder addTransition(Action action, Transition transition) {
			transitions.put(action, transition);
			return this;
		}

		public State build() {
			return new State(transitions);
		}
	}

	private Map<Action, Transition> transitions;

	private State (Map<Action, Transition> transitions) {
		this.transitions = transitions;
	}

	public static Builder newBuilder() { return new Builder(); }

	public State transition(Action action) {
		if (transitions.containsKey(action)) {
			return transitions.get(action).execute();
		}
		return this;
	}
}
