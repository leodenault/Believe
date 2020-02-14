package believe.statemachine;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A state machine that allows running multiple sets of states at the same time.
 *
 * @param <A> the type of action the triggers a transition.
 */
public class ConcurrentStateMachine<A> {
  /**
   * A listener that reacts to changes in state.
   *
   * @param <A> the type of action the triggers a transition.
   */
  public interface Listener<A> {
    /**
     * Indicates that a state transition has ended.
     *
     * @param currentStates the set of currently-active states.
     */
    void transitionEnded(Set<State<A>> currentStates);
  }

  private Set<State<A>> concurrentStates;
  private Set<Listener<A>> listeners;

  public ConcurrentStateMachine(Set<State<A>> concurrentStartStates) {
    concurrentStates = concurrentStartStates;
    listeners = new HashSet<>();
  }

  public Set<State<A>> getStates() {
    return concurrentStates;
  }

  public Set<State<A>> transition(A action) {
    concurrentStates =
        concurrentStates.stream()
            .map(state -> state.transition(action))
            .collect(Collectors.toSet());
    listeners.forEach(listener -> listener.transitionEnded(concurrentStates));
    return concurrentStates;
  }

  public void addListener(Listener<A> listener) {
    listeners.add(listener);
  }
}
