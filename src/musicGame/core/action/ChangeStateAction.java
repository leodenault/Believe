package musicGame.core.action;

import musicGame.core.GameStateRegistry;
import musicGame.menu.action.MenuAction;

import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;

public class ChangeStateAction implements MenuAction {
	
	private Class<? extends NiftyOverlayBasicGameState> state;
	
	protected StateBasedGame game;
	
	public ChangeStateAction(Class<? extends NiftyOverlayBasicGameState> state, StateBasedGame game) {
		this.state = state;
		this.game = game;
	}
	
	@Override
	public void performAction() {
		game.enterState(GameStateRegistry.getInstance().getEntry(this.state));
	}

}
