package believe.action;

import believe.gamestate.GameStateBase;
import believe.gamestate.temporarystate.PrecedingState;
import believe.gamestate.temporarystate.TemporaryState;
import believe.graphicstransitions.GraphicsTransitionPairFactory;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;

/** Callback specializing in pausing a playable state. */
public final class ChangeToTemporaryStateAction<PrecedingStateT extends PrecedingState> {
  private final ChangeStateAction<? extends TemporaryState<PrecedingStateT>> changeStateAction;
  private final PrecedingStateT precedingState;

  public ChangeToTemporaryStateAction(
      Class<? extends TemporaryState<PrecedingStateT>> overlay,
      PrecedingStateT state,
      StateBasedGame game) {
    changeStateAction =
        new ChangeStateAction<>(
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
