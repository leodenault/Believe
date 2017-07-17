package musicGame.statemachine;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import musicGame.statemachine.State.Action;

public class ConcurrentStateMachine {
	public interface Listener {
		void transitionEnded(Set<State> currentStates);
	}
	
	private Set<State> concurrentStates;
	private Set<Listener> listeners;

	public ConcurrentStateMachine(Set<State> concurrentStartStates) {
		concurrentStates = concurrentStartStates;
		listeners = new HashSet<>();
	}

	public Set<State> getStates() {
		return concurrentStates;
	}

	public Set<State> transition(Action action) {
		concurrentStates = concurrentStates.stream()
				.map(state -> state.transition(action))
				.collect(Collectors.toSet());
		listeners.stream().forEach(listener -> listener.transitionEnded(concurrentStates));
		return concurrentStates;
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
}
