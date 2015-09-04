package musicGame.map;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

public class MapProperties {
	private static final int EMPTY_TILE = 0;
	private static final String NUMBER_DEFAULT = "0";
	private static final String COLLISION_DEFAULT = "nonexistent";
	
	public int startX;
	public int startY;
	public List<Tile> collidableTiles;
	
	private MapProperties() {}
	
	public static MapProperties create(TiledMap map) {
		MapProperties properties = new MapProperties();
		properties.startX = fetchNumber(map, "playerStartX", NUMBER_DEFAULT);
		properties.startY = fetchNumber(map, "playerStartY", NUMBER_DEFAULT);
		properties.collidableTiles = fetchCollidableTiles(map);
		return properties;
	}
	
	private static int fetchNumber(TiledMap map, String propertyName, String defaultValue) {
		int value = 0;
		
		try {
			value = Integer.parseInt(map.getMapProperty(propertyName, defaultValue));
		} catch (NumberFormatException e) {
			Log.error(String.format(
					"Could not fetch numeric property %s from map",
					propertyName, defaultValue));
		}
		
		return value;
	}
	
	private static List<Tile> fetchCollidableTiles(TiledMap map) {
		List<Tile> tiles = new LinkedList<Tile>();
		for (int i = 0; i < map.getLayerCount(); i++) {
			String prop = map.getLayerProperty(i, "collision", COLLISION_DEFAULT);
			if (!prop.equals(COLLISION_DEFAULT)) {
				int tileWidth = map.getTileWidth();
				int tileHeight = map.getTileHeight();
				
				for (int x = 0; x < map.getWidth(); x++) {
					for (int y = 0; y < map.getHeight(); y++) {
						if (map.getTileId(x, y, i) != EMPTY_TILE) {
							tiles.add(new Tile(x, y, tileWidth, tileHeight));
						}
					}
				}
			}
		
		}
		return tiles;
	}
}
