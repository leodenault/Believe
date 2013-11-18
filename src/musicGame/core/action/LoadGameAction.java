package musicGame.core.action;

import org.newdawn.slick.state.StateBasedGame;

import musicGame.core.GameStateRegistry;
import musicGame.core.PlayGameState;
import musicGame.menu.action.MenuAction;

public class LoadGameAction implements MenuAction {

	private String flowFile;
	private StateBasedGame game;
	
	public LoadGameAction(String flowFile, StateBasedGame game) {
		this.flowFile = flowFile;
		this.game = game;
	}
	
	@Override
	public void performAction() {
		PlayGameState state = new PlayGameState(flowFile);
		this.game.addState(state);
		this.game.enterState(GameStateRegistry.getInstance().getEntry(PlayGameState.class));
	}

}
