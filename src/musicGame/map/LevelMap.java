package musicGame.map;

import musicGame.gui.ComponentBase;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.tiled.TiledMap;

public class LevelMap extends ComponentBase {
	private static final float SCROLL_SPEED = 0.1f; // Pixels per millisecond
	private static final String MAP_DIRECTORY = "/res/maps/";
	private static final String MAP_SUFFIX = ".tmx";
	
	public static final int TARGET_WIDTH = 1600;
	public static final int TARGET_HEIGHT = 900;
	
	private boolean scrolling;
	private boolean scrollDirection; // False is left, true is right
	private TiledMap map;
	private MapProperties properties;
			
	public LevelMap(GUIContext container, String name) throws SlickException {
		super(container, 0, 0);
		scrolling = false;
		scrollDirection = false;
		map = new TiledMap(String.format("%s%s%s", MAP_DIRECTORY, name, MAP_SUFFIX));
		properties = MapProperties.create(map);
	}
	
	public void update(int delta) {
		if (scrolling) {
			float distance = delta * SCROLL_SPEED;
			float x = rect.getX();
			rect.setX(scrollDirection ? x - distance : x + distance);
		}
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
		setLocation(0, 0);
	}
	
	public int getPlayerStartX() {
		return properties.startX * map.getTileWidth();
	}
	
	public int getPlayerStartY() {
		return properties.startY * map.getTileHeight();
	}

	@Override
	protected void resetLayout() {}

	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		map.render(getX(), getY());
	}
}
