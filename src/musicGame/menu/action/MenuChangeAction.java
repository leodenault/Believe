package musicGame.menu.action;

import org.newdawn.slick.state.StateBasedGame;

import musicGame.core.GameStateBase;
import musicGame.core.GameStateRegistry;

public class MenuChangeAction implements MenuAction {

	private Class<? extends GameStateBase> state;
	private StateBasedGame game;
	
	public MenuChangeAction(Class<? extends GameStateBase> state, StateBasedGame game) {
		this.state = state;
		this.game = game;
	}
	
	@Override
	public void performAction() {
		game.enterState(GameStateRegistry.getInstance().getEntry(state));
	}

}
