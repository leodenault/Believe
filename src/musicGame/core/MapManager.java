package musicGame.core;

import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.Log;

import musicGame.map.LevelMap;
import musicGame.xml.CompoundDef;
import musicGame.xml.ListDef;
import musicGame.xml.XMLCompound;
import musicGame.xml.XMLDataParser;
import musicGame.xml.XMLInteger;
import musicGame.xml.XMLList;
import musicGame.xml.XMLLoadingException;
import musicGame.xml.XMLString;

public class MapManager {
	private static final String FILE = "/data/maps.xml";
	private static final String MAPS_NODE = "maps";
	private static final String MAP_NODE = "map";
	private static final String NAME_NODE = "name";
	private static final String LOCATION_NODE = "location";
	private static final String TILE_SETS_LOCATION_NODE = "tileSetsLocation";
	private static final String BACKGROUNDS_NODE = "backgrounds";
	private static final String BACKGROUND_NODE = "background";
	private static final String LAYER_NODE = "layer";
	private static final String Y_NODE = "y";
	private static final ListDef SCHEMA =
			new ListDef(MAPS_NODE,
					new CompoundDef(MAP_NODE)
					.addString(NAME_NODE)
					.addString(LOCATION_NODE)
					.addString(TILE_SETS_LOCATION_NODE)
					.addList(BACKGROUNDS_NODE, 
							new CompoundDef(BACKGROUND_NODE)
							.addString(LOCATION_NODE)
							.addInteger(LAYER_NODE)
							.addInteger(Y_NODE)
							)
					);
	
	private static MapManager INSTANCE;
	
	private String file;
	private HashMap<String, XMLCompound> uninitializedMaps;
	private HashMap<String, LevelMap> maps;
	
	private MapManager(String file) {
		this.file = file;
		this.uninitializedMaps = new HashMap<String, XMLCompound>();
		this.maps = new HashMap<String, LevelMap>();
		loadMaps();
	}
	
	public static MapManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MapManager(FILE);
		}
		return INSTANCE;
	}
	
	private void loadMaps() {
		XMLDataParser parser = new XMLDataParser(file, SCHEMA);
		
		try {
			XMLList top = parser.loadFile();
			
			for (XMLCompound map : top.children) {
				uninitializedMaps.put(map.<XMLString>getValue(NAME_NODE).value, map);
			}
		} catch (SlickException | XMLLoadingException e) {
			Log.error(String.format("There was an error attempting to "
					+ "read the map data file named '%s'. The exception is listed below:\n\n%s",
					file, e.getMessage()));
		}
	}
	
	public LevelMap getMap(String name, GUIContext container) throws SlickException {
		if (uninitializedMaps.containsKey(name)) {
			XMLCompound xmlMap = uninitializedMaps.get(name);
			LevelMap map = new LevelMap(container,
					xmlMap.<XMLString>getValue(LOCATION_NODE).value,
					xmlMap.<XMLString>getValue(TILE_SETS_LOCATION_NODE).value);
			
			for (XMLCompound background : xmlMap.<XMLList>getValue(BACKGROUNDS_NODE).children) {
				map.addBackground(
						background.<XMLString>getValue(LOCATION_NODE).value,
						background.<XMLInteger>getValue(LAYER_NODE).value,
						background.<XMLInteger>getValue(Y_NODE).value);
			}
			
			maps.put(name, map);
		}
		return maps.get(name);
	}
}
