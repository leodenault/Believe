package musicGame.state;

import java.io.IOException;
import java.io.InputStreamReader;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import musicGame.character.PlayableCharacter;
import musicGame.core.Options;
import musicGame.gui.PlayArea;
import musicGame.levelFlow.FlowComponent;
import musicGame.levelFlow.parsing.FlowComponentBuilder;
import musicGame.levelFlow.parsing.FlowFileParser;
import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;
import musicGame.map.LevelMap;

public class ArcadeState extends LevelState {
	private FlowComponent component;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		FlowComponentBuilder builder = new FlowComponentBuilder(container, (int)(0.2 * container.getWidth()));
		try {
			new FlowFileParser(
					new InputStreamReader(
							ResourceLoader.getResourceAsStream("levelFlowFiles/Drive.lfl")), builder).parse();
			component = builder.buildFlowComponent();
			component.setSpeedMultiplier(Options.getInstance().flowSpeed);
			component.setLocation((int)(0.8 * container.getWidth()), 0);
			component.setHeight(container.getHeight());
		} catch (IOException | FlowFileParserException | FlowComponentBuilderException e) {
			throw new RuntimeException(String.format(
					"Could not start arcade state because of the following exception:\n\n %s\n%s",
					e.getMessage(),
					e.getStackTrace()));
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);
		component.update(delta);
		if (!component.isPlaying()) {
			component.play();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
		component.render(container, g);
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (key == Input.KEY_ESCAPE) {
			this.component.pause();
		}
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		component.stop();
	}

	@Override
	protected boolean isOnRails() {
		return true;
	}
	
	@Override
	protected String getMapName() {
		return "pipeTown";
	}
	
	@Override
	protected String getMusicLocation() {
		return "/res/music/Evasion.ogg";
	}

	@Override
	protected PlayArea providePlayArea(GameContainer container, LevelMap map, PlayableCharacter player) {
		return new PlayArea(container, map, player, 0.8f, 1f);
	}
}
