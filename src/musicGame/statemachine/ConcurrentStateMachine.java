package musicGame.statemachine;

import java.util.List;

public class ConcurrentStateMachine {
	private List<State> concurrentStates;
	
	public ConcurrentStateMachine(List<State> concurrentStartStates) {
		concurrentStates = concurrentStartStates;
	}
}
