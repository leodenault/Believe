package musicGame.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import musicGame.levelFlow.FlowComponent;
import musicGame.levelFlow.parsing.FlowComponentBuilder;
import musicGame.levelFlow.parsing.FlowFileParser;
import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;

public class PlayGameState extends GameStateBase {
	
	private String flowFile;
	private FlowComponent component;
	
	public PlayGameState(String flowFile) {
		super();
		this.flowFile = flowFile;
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (this.component != null) {
			if (key == Input.KEY_ENTER) {
				if (this.component.isPlaying()) {
					this.component.pause();
				} else {
					this.component.play();
				}
			} else if (key == Input.KEY_SPACE) {
				this.component.stop();
			}
		}
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game)
			throws SlickException {
super.enter(container, game);
		
		FlowComponentBuilder builder = new FlowComponentBuilder(container, 32);
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(this.flowFile));
			FlowFileParser parser = new FlowFileParser(reader, builder);
			parser.parse();
			this.component = builder.buildFlowComponent();
			this.component.setSpeedMultiplier(4);
			this.component.setLocation((container.getWidth() - component.getWidth())/ 2, 32);
			this.component.setHeight(container.getHeight());
//			component.addListener(this);
		} catch (IOException | FlowFileParserException
				| FlowComponentBuilderException e) {
			e.printStackTrace();
			// TODO: Recover gracefully.
		}
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		if (component != null) {
			component.render(container, g);
		}
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		if (component != null) {
			component.update();
		}
	}
}
