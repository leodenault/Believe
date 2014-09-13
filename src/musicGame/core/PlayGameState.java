package musicGame.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import musicGame.core.action.ChangeStateAction;
import musicGame.levelFlow.FlowComponent;
import musicGame.levelFlow.parsing.FlowComponentBuilder;
import musicGame.levelFlow.parsing.FlowFileParser;
import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PlayGameState extends HierarchicalGameState {
	
	public interface SubPlayGameState extends SubGameState {
		public void setFlowComponent(FlowComponent component);
	}

	public PlayGameState() {
		this(NO_XML_FILE);
	}
	
	public PlayGameState(String niftyXmlFile) {
		super(niftyXmlFile);
	}

	protected FlowComponent component;
	
	private String flowFile;
	
	public void setFlowFile(String flowFile) {
		this.flowFile = flowFile;
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enterState(container, game);
		FlowComponentBuilder builder = new FlowComponentBuilder(container, 32);
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(this.flowFile));
			FlowFileParser parser = new FlowFileParser(reader, builder);
			parser.parse();
			this.component = builder.buildFlowComponent();
		} catch (IOException | FlowFileParserException
				| FlowComponentBuilderException | SlickException e) {
			e.printStackTrace();
			// TODO: Recover gracefully.
		}
		
		this.component.setSpeedMultiplier(4);
		this.component.setLocation((container.getWidth() - this.component.getWidth())/ 2, 32);
		this.component.setHeight(container.getHeight());
		
		for (SubGameState subState : this.subStates) {
			((SubPlayGameState)subState).setFlowComponent(this.component);
		}
		
		new ChangeStateAction(this.defaultState.getClass(), game).performAction();
	}
}
