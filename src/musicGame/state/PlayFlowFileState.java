package musicGame.state;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import musicGame.core.Options;
import musicGame.core.action.ChangeStateAction;
import musicGame.core.action.PauseGameAction;
import musicGame.levelFlow.FlowComponent;
import musicGame.levelFlow.FlowComponentListener;
import musicGame.levelFlow.parsing.FlowComponentBuilder;
import musicGame.levelFlow.parsing.FlowFileParser;
import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;

public class PlayFlowFileState extends GameStateBase implements FlowComponentListener, PausableState {
	
	private FlowComponent component;
	private StateBasedGame game;
	
	public void loadFile(String flowFile) throws IOException, FlowFileParserException,
	SlickException, FlowComponentBuilderException {
		InputStream stream = new FileInputStream(flowFile);
		GameContainer container = game.getContainer();
		FlowComponentBuilder builder = new FlowComponentBuilder(container, container.getWidth() / 3);
		InputStreamReader reader = new InputStreamReader(stream);
		FlowFileParser parser = new FlowFileParser(reader, builder);
		parser.parse();
		this.component = builder.buildFlowComponent();
		this.component.setSpeedMultiplier(Options.getInstance().flowSpeed);
		this.component.setLocation((container.getWidth() - component.getWidth())/ 2, container.getHeight() / 15);
		this.component.setHeight(container.getHeight());
		this.component.addListener(this);
	}
	
	@Override
	public void reset() {
		if (this.component != null) {
			this.component.reset();
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (key == Input.KEY_ESCAPE) {
			this.component.pause();
			new PauseGameAction(this, game).componentActivated(null);
		}
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (component != null) {
			component.render(container, g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (component != null) {
			component.update(delta);
		}

		if (!component.isPlaying()) {
			component.play();
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);
		this.component.pause();
	}

	@Override
	public void beatSuccess(int index) {}

	@Override
	public void beatFailed() {}

	@Override
	public void beatMissed() {}

	@Override
	public void songEnded() {
		new ChangeStateAction(MainMenuState.class, game).componentActivated(null);
	}

	@Override
	public void componentActivated(AbstractComponent source) {}
}
