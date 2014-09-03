package musicGame.core.action;

import musicGame.core.GameStateRegistry;
import musicGame.core.PlayGameState;

import org.newdawn.slick.state.StateBasedGame;

public class LoadGameAction extends ChangeStateAction {

	private String flowFile;
	
	public LoadGameAction(String flowFile, StateBasedGame game) {
		super(PlayGameState.class, game);
		this.flowFile = flowFile;
	}
	
	@Override
	public void performAction() {
		int stateId = GameStateRegistry.getInstance().getEntry(PlayGameState.class);
		((PlayGameState)this.game.getState(stateId)).setFlowFile(this.flowFile);
		this.game.enterState(stateId);
	}

}
