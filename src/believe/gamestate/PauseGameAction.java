package believe.gamestate;

import believe.graphics_transitions.GraphicsTransitionPairFactory;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;

public class PauseGameAction {
  public interface OverlayState extends GameState {
    void setPausedState(PausableState state);

    void setBackgroundImage(Image backgroundImage);
  }

  private final ChangeStateAction changeStateAction;
  private final PausableState pausableState;

  public PauseGameAction(
      Class<? extends OverlayState> overlay, PausableState state, StateBasedGame game) {
    changeStateAction = new ChangeStateAction(
        overlay,
        game,
        new GraphicsTransitionPairFactory(EmptyTransition::new, EmptyTransition::new));
    pausableState = state;
  }

  public void pause(Image backgroundImage) {
    OverlayState overlay = (OverlayState) GameStateBase.getStateInstance(changeStateAction.state);
    overlay.setPausedState(pausableState);
    overlay.setBackgroundImage(backgroundImage);

    changeStateAction.componentActivated(null);
  }
}
