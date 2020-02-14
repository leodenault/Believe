package believe.statemachine;

import java.util.HashMap;
import java.util.Map;

/**
 * A state within a concurrent state machine.
 *
 * @param <A> the type of action that triggers a transition.
 */
public class State<A> {
  private Map<A, Transition<A>> transitions;

  public State() {
    transitions = new HashMap<>();
  }

  public State<A> addTransition(A action, Transition<A> transition) {
    transitions.put(action, transition);
    return this;
  }

  public State<A> addTransition(A action, Runnable runnable, State<A> endState) {
    return addTransition(action, new Transition<>(runnable, endState));
  }

  public State<A> addTransition(A action, State<A> endState) {
    return addTransition(action, () -> {}, endState);
  }

  public State<A> transition(A action) {
    if (transitions.containsKey(action)) {
      return transitions.get(action).execute();
    }
    return this;
  }
}
