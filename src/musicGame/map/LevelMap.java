package musicGame.map;

import java.util.List;

import musicGame.gui.ComponentBase;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.tiled.TiledMap;

public class LevelMap extends ComponentBase {
	private static final String MAP_DIRECTORY = "/res/maps/";
	private static final String MAP_SUFFIX = ".tmx";
	
	public static final int TARGET_WIDTH = 1600;
	public static final int TARGET_HEIGHT = 900;
	
	private TiledMap map;
	private MapProperties properties;
			
	public LevelMap(GUIContext container, String name) throws SlickException {
		super(container, 0, 0);
		map = new TiledMap(String.format("%s%s%s", MAP_DIRECTORY, name, MAP_SUFFIX));
		properties = MapProperties.create(map);
		rect.setSize(map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
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
	
	public List<Tile> getCollidableTiles() {
		return properties.collidableTiles;
	}

	@Override
	protected void resetLayout() {}

	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		map.render(getX(), getY());
	}
}
