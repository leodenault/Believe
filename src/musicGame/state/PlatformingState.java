package musicGame.state;

import musicGame.core.action.PauseGameAction;
import musicGame.map.LevelMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PlatformingState extends GameStateBase implements PausableState {

	private StateBasedGame game;
	private LevelMap map;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (map != null) {
			map.render();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (map != null) {
			map.update(delta);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (map != null) {
			map.scroll(key);
		}
		
		if (key == Input.KEY_ESCAPE) {
			new PauseGameAction(this, game).componentActivated(null);
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		if (map != null) {
			map.stopScroll(key);
		}
	}
	
	@Override
	public void reset() {
		map.reset();
	}

	public void setUp() throws SlickException {
		map = new LevelMap("testMap");
	}
}
