package musicGame.map;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

public class MapProperties {
	private static final int EMPTY_TILE = 0;
	private static final String NUMBER_DEFAULT = "0";
	
	protected static final String NO_PROP_DEFAULT = "nonexistent";
	protected static final String FRONT = "front";
	protected static final String COLLISION = "collision";
	protected static final String ENEMIES = "enemies";
	
	private static final List<String> INVISIBLE_LAYERS = Arrays.asList(COLLISION, ENEMIES);
	
	public int startX;
	public int startY;
	public List<Tile> collidableTiles;
	public List<Integer> rearLayers;
	public List<Integer> frontLayers;
	
	protected MapProperties() {}
	
	public static MapProperties create(TiledMap map) {
		MapProperties properties = new MapProperties();
		properties.startX = fetchNumber(map, "playerStartX", NUMBER_DEFAULT);
		properties.startY = fetchNumber(map, "playerStartY", NUMBER_DEFAULT);
		properties.collidableTiles = fetchCollidableTiles(map);
		fetchLayers(map, properties);
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
			String prop = map.getLayerProperty(i, COLLISION, NO_PROP_DEFAULT);
			if (!prop.equals(NO_PROP_DEFAULT)) {
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
	
	public static void fetchLayers(TiledMap map, MapProperties properties) {
		List<Integer> rearLayers = new LinkedList<Integer>();
		List<Integer> frontLayers = new LinkedList<Integer>();
		
		for (int i = 0; i < map.getLayerCount(); i++) {
			if (!isInvisible(map, i)) {
				String prop = map.getLayerProperty(i, FRONT, NO_PROP_DEFAULT);
				if (prop.equals(NO_PROP_DEFAULT)) {
						rearLayers.add(i);
				} else {
					frontLayers.add(i);
				}
			}
		}
		
		properties.rearLayers = rearLayers;
		properties.frontLayers = frontLayers;
	}
	
	private static boolean isInvisible(TiledMap map, int layer) {
		for (String property : INVISIBLE_LAYERS) {
			String value = map.getLayerProperty(layer, property, NO_PROP_DEFAULT);
			if (!value.equals(NO_PROP_DEFAULT)) {
				return true;
			}
		}
		
		return false;
	}
}
