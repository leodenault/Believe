package believe.map.gui;

import believe.xml.CompoundDef;
import believe.xml.ListDef;
import believe.xml.XMLCompound;
import believe.xml.XMLDataParser;
import believe.xml.XMLInteger;
import believe.xml.XMLList;
import believe.xml.XMLLoadingException;
import believe.xml.XMLString;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MapManager {
  private static final class MapConfig {
    private final XMLCompound xmlMap;
    private LevelMap levelMap;

    private MapConfig(XMLCompound xmlMap) {
      this.xmlMap = xmlMap;
    }

    private void setLevelMap(LevelMap levelMap) {
      this.levelMap = levelMap;
    }
  }

  private static final String DEFAULT_FILE = "/data/maps.xml";
  private static final String MAPS_NODE = "maps";
  private static final String MAP_NODE = "map";
  private static final String NAME_NODE = "name";
  private static final String LOCATION_NODE = "location";
  private static final String TILE_SETS_LOCATION_NODE = "tileSetsLocation";
  private static final String BACKGROUNDS_NODE = "backgrounds";
  private static final String BACKGROUND_NODE = "background";
  private static final String LAYER_NODE = "layer";
  private static final String Y_NODE = "y";
  private static final ListDef SCHEMA = new ListDef(
      MAPS_NODE,
      new CompoundDef(MAP_NODE)
          .addString(NAME_NODE)
          .addString(LOCATION_NODE)
          .addString(TILE_SETS_LOCATION_NODE)
          .addList(
              BACKGROUNDS_NODE,
              new CompoundDef(BACKGROUND_NODE)
                  .addString(LOCATION_NODE)
                  .addInteger(LAYER_NODE)
                  .addInteger(Y_NODE)));

  private static Map<String, MapManager> INSTANCES = new HashMap<>();

  private String file;
  private HashMap<String, MapConfig> uninitializedMaps;
  private HashMap<String, MapConfig> maps;
  private boolean mapsLoaded;

  private MapManager(String file) {
    this.file = file;
    this.uninitializedMaps = new HashMap<>();
    this.maps = new HashMap<>();
    mapsLoaded = false;
  }

  public static MapManager defaultManager() {
    return managerForFile(DEFAULT_FILE);
  }

  public static MapManager managerForFile(String fileName) {
    return INSTANCES.getOrDefault(fileName, new MapManager(fileName));
  }

  private void loadMaps() {
    XMLDataParser parser = new XMLDataParser(file, SCHEMA);

    try {
      XMLList top = parser.loadFile();

      for (XMLCompound map : top.children) {
        uninitializedMaps.put(map.<XMLString>getValue(NAME_NODE).value, new MapConfig(map));
      }
    } catch (SlickException | XMLLoadingException e) {
      Log.error(String.format("There was an error attempting to read the map data file named '%s'"
          + ". The exception is "
          + "listed below:\n\n%s", file, e.getMessage()));
    }
  }

  public LevelMap getMap(String name, GUIContext container, boolean forceReload)
      throws SlickException {
    if (!mapsLoaded) {
      loadMaps();
      mapsLoaded = true;
    }

    MapConfig config;
    if (uninitializedMaps.containsKey(name)) {
      config = uninitializedMaps.get(name);
      fetchMap(config, container);
      uninitializedMaps.remove(name);
    } else if (maps.containsKey(name)) {
      config = maps.get(name);
      fetchMap(config, container);
      maps.remove(name);
    } else {
      throw new RuntimeException("The map '"
          + name
          + "' was not defined in the XML configuration.");
    }

    maps.put(name, config);
    return maps.get(name).levelMap;
  }

  private void fetchMap(MapConfig mapConfig, GUIContext container) throws SlickException {
    XMLCompound xmlMap = mapConfig.xmlMap;
    LevelMap levelMap = new LevelMap(
        container,
        xmlMap.<XMLString>getValue(LOCATION_NODE).value,
        xmlMap.<XMLString>getValue(TILE_SETS_LOCATION_NODE).value);

    for (XMLCompound background : xmlMap.<XMLList>getValue(BACKGROUNDS_NODE).children) {
      levelMap.addBackground(
          background.<XMLString>getValue(LOCATION_NODE).value,
          background.<XMLInteger>getValue(LAYER_NODE).value,
          background.<XMLInteger>getValue(Y_NODE).value);
    }
    mapConfig.setLevelMap(levelMap);
  }
}
