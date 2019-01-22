package believe.graphics_transitions;

import org.newdawn.slick.state.transition.Transition;

public final class GraphicsTransitionPairFactory {
  public interface TransitionFactory {
    Transition create();
  }

  private final TransitionFactory leaveTransition;
  private final TransitionFactory enterTransition;


  public GraphicsTransitionPairFactory(
      TransitionFactory leaveTransition, TransitionFactory enterTransition) {
    this.leaveTransition = leaveTransition;
    this.enterTransition = enterTransition;
  }

  public Transition createLeaveTransition() {
    return leaveTransition.create();
  }

  public Transition createEnterTransition() {
    return enterTransition.create();
  }
}
