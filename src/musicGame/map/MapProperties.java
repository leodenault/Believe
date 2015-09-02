package musicGame.map;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

public class MapProperties {
	private static final String NUMBER_DEFAULT = "0";
	
	public int startX;
	public int startY;
	
	private MapProperties() {}
	
	public static MapProperties create(TiledMap map) {
		MapProperties properties = new MapProperties();
		properties.startX = fetchNumber(map, "playerStartX", NUMBER_DEFAULT);
		properties.startY = fetchNumber(map, "playerStartY", NUMBER_DEFAULT);
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
}
