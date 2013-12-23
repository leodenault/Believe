package musicGame.core.action;

import org.newdawn.slick.state.StateBasedGame;

import musicGame.core.GameStateBase;
import musicGame.core.GameStateRegistry;
import musicGame.menu.action.MenuAction;

public class ChangeStateAction implements MenuAction {

	private Class<? extends GameStateBase> state;
	
	protected StateBasedGame game;
	
	public ChangeStateAction(Class<? extends GameStateBase> state, StateBasedGame game) {
		this.state = state;
		this.game = game;
	}
	
	@Override
	public void performAction() {
		game.enterState(GameStateRegistry.getInstance().getEntry(this.state));
	}

}
