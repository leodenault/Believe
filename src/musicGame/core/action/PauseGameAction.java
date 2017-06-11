package musicGame.core.action;

import musicGame.gamestate.GamePausedOverlay;
import musicGame.gamestate.GameStateBase;
import musicGame.gamestate.PausableState;

import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;

public class PauseGameAction extends ChangeStateAction {

	PausableState pausableState;
	
	public PauseGameAction(PausableState state,
			StateBasedGame game) {
		super(GamePausedOverlay.class, game);
		pausableState = state;
	}

	@Override
	public void componentActivated(AbstractComponent component) {
		GamePausedOverlay overlay = GameStateBase.getStateInstance(GamePausedOverlay.class);
		overlay.setPausedState(pausableState);
		
		super.componentActivated(component);
	}
}
