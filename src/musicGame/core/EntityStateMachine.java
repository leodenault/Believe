package musicGame.core;

import java.util.Map;
import java.util.function.Function;

/**
 * State machine to be used by entities in the game for handling input/events and mapping them to
 * behaviour.
 *
 * @param <A> The type of an action which triggers a transition.
 * @param <S> The type of the state used by the state machine.
 */
public class EntityStateMachine<A, S> {
	private Map<S, Map<A, Function<Void, S>>> transitions;
	private S currentState;
	
	public EntityStateMachine(
			Map<S, Map<A, Function<Void, S>>> transitions, S initialState) {
		this.transitions = transitions;
		currentState = initialState;
	}
	
	public S getCurrentState() {
		return currentState;
	}
	
	public S transition(A action) {
		Map<A, Function<Void, S>> stateTransitions = transitions.get(currentState);
		if (stateTransitions != null && stateTransitions.containsKey(action)) {
			S state = stateTransitions.get(action).apply(null);
			if (state != null) {
				currentState = state;
			}
		}
		return currentState;
	}
}
