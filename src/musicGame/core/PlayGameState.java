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
	private GamePausedOverlay pauseOverlay;
	
	public PlayGameState() {
		this.pauseOverlay = new GamePausedOverlay(this);
	}
	
	public void setFlowFile(String flowFile) {
		this.flowFile = flowFile;
	}
	
	public void restart() {
		this.pauseOverlay.setVisible(false);
		this.pauseOverlay.reset();
		if (this.component != null) {
			this.component.stop();
			this.component.play();
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (this.component != null) {
			if (key == Input.KEY_SPACE) {
				this.component.stop();
			}
		}
		if (key == Input.KEY_ESCAPE) {
			if (this.component != null) {
				if (this.component.isPlaying()) {
					this.component.pause();
					this.pauseOverlay.setVisible(true);
				} else if (this.component.isPaused()) {
					this.pauseOverlay.setVisible(false);
					this.pauseOverlay.reset();
					this.component.play();
				}
			}
		}
		this.pauseOverlay.keyPressed(key);
	}

	@Override
	public void initGameAndGUI(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.initGameAndGUI(container, game);
		this.pauseOverlay.init(this.getNifty(), game);
	}

	@Override
	public void enterState(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enterState(container, game);
		this.pauseOverlay.setVisible(false);
		
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
			this.component.play();
		} catch (IOException | FlowFileParserException
				| FlowComponentBuilderException e) {
			e.printStackTrace();
			// TODO: Recover gracefully.
		}
	}

	@Override
	public void renderGame(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (component != null) {
			component.render(container, g);
		}
	}

	@Override
	public void updateGame(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (component != null) {
			component.update();
		}
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		this.pauseOverlay.prepareNifty(nifty, game);
	}
}
