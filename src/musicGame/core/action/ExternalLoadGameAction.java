package musicGame.core.action;

import java.io.IOException;

import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;
import musicGame.state.GameStateBase;
import musicGame.state.PlayGameState;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;

public class ExternalLoadGameAction extends ChangeStateAction {

	private String flowFile;
	
	public ExternalLoadGameAction(String flowFile, StateBasedGame game) {
		super(PlayGameState.class, game);
		this.flowFile = flowFile;
	}
	
	@Override
	public void componentActivated(AbstractComponent component) {
		try {
			GameStateBase.getStateInstance(PlayGameState.class).loadExternalFile(this.flowFile);
		} catch (IOException | FlowFileParserException | SlickException
				| FlowComponentBuilderException e) {
			// TODO Properly handle the exception
			e.printStackTrace();
		}
		this.game.enterState(GameStateBase.getStateID(PlayGameState.class));
	}

}
