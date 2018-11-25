package believe.gamestate;

import believe.graphics_transitions.GraphicsTransitionPairFactory;
import com.sun.webkit.perf.PerfLogger.ProbeStat;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;

/** Callback specializing in pausing a playable state. */
class ChangeToTemporaryStateAction<PrecedingStateT extends PrecedingState> {
  private final ChangeStateAction<? extends TemporaryState<PrecedingStateT>>
      changeStateAction;
  private final PrecedingStateT precedingState;

  ChangeToTemporaryStateAction(
      Class<? extends TemporaryState<PrecedingStateT>> overlay,
      PrecedingStateT state,
      StateBasedGame game) {
    changeStateAction = new ChangeStateAction<>(
        overlay,
        game,
        new GraphicsTransitionPairFactory(EmptyTransition::new, EmptyTransition::new));
    precedingState = state;
  }

  public void activate() {
    GameStateBase.getStateInstance(changeStateAction.state).setPrecedingState(precedingState);

    changeStateAction.componentActivated(null);
  }
}
