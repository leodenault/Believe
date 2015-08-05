package musicGame.map;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class LevelMap {
	private static final float SCROLL_SPEED = 0.1f; // Pixels per millisecond
	private static final String MAP_DIRECTORY = "/res/maps/";
	private static final String MAP_SUFFIX = ".tmx";
	
	private boolean scrolling;
	private boolean scrollDirection; // False is left, true is right
	private float x;
	private float y;
	private TiledMap map;
			
	public LevelMap(String name) throws SlickException {
		scrolling = false;
		scrollDirection = false;
		x = 0;
		y = 0;
		map = new TiledMap(String.format("%s%s%s", MAP_DIRECTORY, name, MAP_SUFFIX));
	}
	
	public void update(int delta) {
		if (scrolling) {
			float distance = delta * SCROLL_SPEED;
			x = scrollDirection ? x - distance : x + distance;
		}
	}
	
	public void render() {
		map.render((int)x, (int)y);
	}
	
	public void scroll(int key) {
		if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
			scrolling = true;
			scrollDirection = (key == Input.KEY_RIGHT);
			
		}
	}
	
	public void stopScroll(int key) {
		if (scrolling) {
			if (key == Input.KEY_LEFT && !scrollDirection ||
					key == Input.KEY_RIGHT && scrollDirection) {
				scrolling = false;
			}
		}
	}
	
	public void reset() {
		x = 0;
		y = 0;
	}
}
