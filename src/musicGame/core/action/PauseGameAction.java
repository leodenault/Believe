package musicGame.core.action;

import musicGame.gamestate.base.GameStateBase;

import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class PauseGameAction extends ChangeStateAction {
  public interface OverlayState extends GameState {
    void setPausedState(PausableState state);
  }

  PausableState pausableState;

  public PauseGameAction(Class<? extends OverlayState> overlay, PausableState state, StateBasedGame game) {
    super(overlay, game);
    pausableState = state;
  }

  @Override
  public void componentActivated(AbstractComponent component) {
    OverlayState overlay =
        (OverlayState) GameStateBase.getStateInstance(state);
    overlay.setPausedState(pausableState);

    super.componentActivated(component);
  }
}
