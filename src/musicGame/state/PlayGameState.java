package musicGame.state;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import musicGame.core.action.ChangeStateAction;
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

public class PlayGameState extends GameStateBase implements FlowComponentListener {
	
	private FlowComponent component;
	private StateBasedGame game;
	
	public void loadFile(String flowFile) throws IOException, FlowFileParserException,
		SlickException, FlowComponentBuilderException {
		initializeComponent(this.getClass().getResourceAsStream(flowFile));
	}

	public void loadExternalFile(String flowFile) throws IOException, FlowFileParserException,
	SlickException, FlowComponentBuilderException {
		initializeComponent(new FileInputStream(flowFile));
	}
	
	private void initializeComponent(InputStream stream) throws IOException, FlowFileParserException,
	SlickException, FlowComponentBuilderException {
		GameContainer container = game.getContainer();
		FlowComponentBuilder builder = new FlowComponentBuilder(container, 32);
		InputStreamReader reader = new InputStreamReader(stream);
		FlowFileParser parser = new FlowFileParser(reader, builder);
		parser.parse();
		this.component = builder.buildFlowComponent();
		this.component.setSpeedMultiplier(4);
		this.component.setLocation((container.getWidth() - component.getWidth())/ 2, 32);
		this.component.setHeight(container.getHeight());
		this.component.addListener(this);
	}
	
	public void reset() {
		if (this.component != null) {
			this.component.reset();
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (key == Input.KEY_ESCAPE) {
			new ChangeStateAction(GamePausedOverlay.class, game).componentActivated(null);
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
			component.update();
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		this.component.play();
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
