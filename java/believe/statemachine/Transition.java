package believe.statemachine;

/**
 * A transition between two states in a state machine.
 *
 * @param <A> the type of action that triggers a change in state.
 */
public class Transition<A> {
  private Runnable runnable;
  private State<A> endState;

  /**
   * Instantiates a transistion.
   *
   * @param runnable the {@link Runnable} that will be executed as part of this transition.
   * @param endState the state that will be transitioned to.
   */
  public Transition(Runnable runnable, State<A> endState) {
    this.runnable = runnable;
    this.endState = endState;
  }

  /** Executes this transition and returns the end state. */
  public State<A> execute() {
    runnable.run();
    return endState;
  }
}
