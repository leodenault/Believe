package musicGame.map;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

import musicGame.character.EnemyCharacter;

public class MapProperties {
	public interface MapEntityGenerator<T> {
		T generateMapEntity(
				TiledMap map,
				GUIContext container,
				int tileId,
				int x,
				int y,
				int tileWidth,
				int tileHeight, int layer)
						throws SlickException;
	}
	
	private static final String NUMBER_DEFAULT = "0";
	
	protected static final String INVALID_PROP = "invalid";
	protected static final String FRONT = "front";
	protected static final String COLLISION = "collision";
	protected static final String ENEMIES = "enemies";
	protected static final String COMMANDS = "commands";

	private static final List<String> INVISIBLE_LAYERS =
			Arrays.asList(COLLISION, ENEMIES, COMMANDS);
	
	public static final int EMPTY_TILE = 0;
	
	public int startX;
	public int startY;
	public List<Tile> collidableTiles;
	public List<EnemyCharacter> enemies;
	public List<Command> commands;
	public List<Integer> rearLayers;
	public List<Integer> frontLayers;
	
	protected MapProperties() {}
	
	public static MapProperties create(TiledMap map, GUIContext container) throws SlickException {
		MapProperties properties = new MapProperties();
		properties.startX = fetchNumber(map, "playerStartX", NUMBER_DEFAULT);
		properties.startY = fetchNumber(map, "playerStartY", NUMBER_DEFAULT);
		properties.collidableTiles = fetchEntitiesInLayerWithProperty(
				map, container, COLLISION, new Tile.Generator());
		properties.enemies = fetchEntitiesInLayerWithProperty(
				map, container, ENEMIES, new EnemyCharacter.Generator());
		properties.commands = fetchEntitiesInLayerWithProperty(
				map, container, COMMANDS, new Command.Generator());
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
					propertyName));
		}
		
		return value;
	}
	
	private static <T> List<T> fetchEntitiesInLayerWithProperty(
			TiledMap map, GUIContext container, String property, MapEntityGenerator<T> generator)
					throws SlickException {
		List<T> entities = new LinkedList<T>();
		for (int i = 0; i < map.getLayerCount(); i++) {
			String prop = map.getLayerProperty(i, property, INVALID_PROP);
			if (!prop.equals(INVALID_PROP)) {
				int tileWidth = map.getTileWidth();
				int tileHeight = map.getTileHeight();
				
				for (int x = 0; x < map.getWidth(); x++) {
					for (int y = 0; y < map.getHeight(); y++) {
						int tileId = map.getTileId(x, y, i);
						if (tileId != EMPTY_TILE) {
							T entity = generator.generateMapEntity(
									map, container, tileId, x, y, tileWidth, tileHeight, i);
							entities.add(entity);
						}
					}
				}
			}
		
		}
		return entities;
	}
	
	protected static void fetchLayers(TiledMap map, MapProperties properties) {
		List<Integer> rearLayers = new LinkedList<Integer>();
		List<Integer> frontLayers = new LinkedList<Integer>();
		
		for (int i = 0; i < map.getLayerCount(); i++) {
			if (!isInvisible(map, i)) {
				String prop = map.getLayerProperty(i, FRONT, INVALID_PROP);
				if (prop.equals(INVALID_PROP)) {
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
			String value = map.getLayerProperty(layer, property, INVALID_PROP);
			if (!value.equals(INVALID_PROP)) {
				return true;
			}
		}
		
		return false;
	}
}
