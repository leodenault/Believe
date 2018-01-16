package musicGame.statemachine;

public class Transition {
  private Runnable runnable;
  private State endState;

  public Transition(Runnable runnable, State endState) {
    this.runnable = runnable;
    this.endState = endState;
  }

  public State execute() {
    runnable.run();
    return endState;
  }
}
