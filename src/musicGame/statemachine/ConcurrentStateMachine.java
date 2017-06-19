package musicGame.statemachine;

import java.util.Set;
import java.util.stream.Collectors;

import musicGame.statemachine.State.Action;

public class ConcurrentStateMachine {
	private Set<State> concurrentStates;

	public ConcurrentStateMachine(Set<State> concurrentStartStates) {
		concurrentStates = concurrentStartStates;
	}

	public Set<State> getStates() {
		return concurrentStates;
	}

	public Set<State> transition(Action action) {
		return concurrentStates.stream()
				.map(state -> state.transition(action))
				.collect(Collectors.toSet());
	}
}
