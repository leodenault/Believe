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
	public List<Tile> enemies;
	public List<Integer> rearLayers;
	public List<Integer> frontLayers;
	
	protected MapProperties() {}
	
	public static MapProperties create(TiledMap map) {
		MapProperties properties = new MapProperties();
		properties.startX = fetchNumber(map, "playerStartX", NUMBER_DEFAULT);
		properties.startY = fetchNumber(map, "playerStartY", NUMBER_DEFAULT);
		properties.collidableTiles = fetchTilesInLayerWithProperty(map, COLLISION);
		properties.enemies = fetchTilesInLayerWithProperty(map, ENEMIES);
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
	
	private static List<Tile> fetchTilesInLayerWithProperty(TiledMap map, String property) {
		List<Tile> tiles = new LinkedList<Tile>();
		for (int i = 0; i < map.getLayerCount(); i++) {
			String prop = map.getLayerProperty(i, property, NO_PROP_DEFAULT);
			if (!prop.equals(NO_PROP_DEFAULT)) {
				int tileWidth = map.getTileWidth();
				int tileHeight = map.getTileHeight();
				
				for (int x = 0; x < map.getWidth(); x++) {
					for (int y = 0; y < map.getHeight(); y++) {
						if (map.getTileId(x, y, i) != EMPTY_TILE) {
							Tile tile = new Tile(x, y, tileWidth, tileHeight);
							tiles.add(tile);
							findNeighbours(map, tile, x, y, i);
						}
					}
				}
			}
		
		}
		return tiles;
	}
	
	protected static void fetchLayers(TiledMap map, MapProperties properties) {
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
	
	private static void findNeighbours(TiledMap map, Tile tile, int x, int y, int layer) {
		if (x > 0 && map.getTileId(x - 1, y, layer) != EMPTY_TILE) {
			tile.setLeftNeighbour(true);
		}
		
		if (x + 1 < map.getWidth() && map.getTileId(x + 1, y, layer) != EMPTY_TILE) {
			tile.setRightNeighbour(true);
		}
		
		if (y > 0 && map.getTileId(x, y - 1, layer) != EMPTY_TILE) {
			tile.setTopNeighbour(true);
		}
		
		if (y + 1 < map.getHeight() && map.getTileId(x, y + 1, layer) != EMPTY_TILE) {
			tile.setBottomNeighbour(true);
		}
	}
}
