package musicGame.state;

import musicGame.map.LevelMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PlatformingState extends GameStateBase {

	private LevelMap map;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
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
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		if (map != null) {
			map.stopScroll(key);
		}
	}

	public void reset() throws SlickException {
		map = new LevelMap("testMap");
	}
}
