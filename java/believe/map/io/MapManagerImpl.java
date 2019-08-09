package believe.map.io;

import static believe.app.annotation.Nullability.notNull;

import believe.map.data.BackgroundSceneData;
import believe.map.data.MapData;
import believe.map.io.InternalQualifiers.MapDefinitionsFile;
import believe.map.tiled.TiledMap;
import believe.xml.CompoundDef;
import believe.xml.ListDef;
import believe.xml.XMLCompound;
import believe.xml.XMLDataParser;
import believe.xml.XMLInteger;
import believe.xml.XMLList;
import believe.xml.XMLLoadingException;
import believe.xml.XMLString;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/** Default implementation of {@link MapManager}. */
@Singleton
public class MapManagerImpl implements MapManager {
  private static final class MapConfig {
    private final XMLCompound xmlMap;
    @Nullable private MapData mapData;

    private MapConfig(XMLCompound xmlMap) {
      this.xmlMap = xmlMap;
    }

    private void setMapData(MapData mapData) {
      this.mapData = mapData;
    }
  }

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
      new ListDef(
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

  private final TiledMapParser tiledMapParser;
  private final String mapDefinitionsFile;
  private final HashMap<String, MapConfig> uninitializedMaps;
  private final HashMap<String, MapConfig> maps;

  private boolean mapsLoaded;

  @Inject
  MapManagerImpl(TiledMapParser tiledMapParser, @MapDefinitionsFile String mapDefinitionsFile) {
    this.tiledMapParser = tiledMapParser;
    this.mapDefinitionsFile = mapDefinitionsFile;
    this.uninitializedMaps = new HashMap<>();
    this.maps = new HashMap<>();
    mapsLoaded = false;
  }

  private void loadMaps() {
    XMLDataParser parser = new XMLDataParser(mapDefinitionsFile, SCHEMA);

    try {
      XMLList top = parser.loadFile();

      for (XMLCompound map : top.children) {
        uninitializedMaps.put(map.<XMLString>getValue(NAME_NODE).value, new MapConfig(map));
      }
    } catch (SlickException | XMLLoadingException e) {
      Log.error(
          String.format(
              "There was an error attempting to read the map data file named '%s'. The exception is"
                  + " listed below:\n\n%s",
              mapDefinitionsFile, e.getMessage()));
    }
  }

  @Override
  public MapData getMap(String name) throws SlickException {
    if (!mapsLoaded) {
      loadMaps();
      mapsLoaded = true;
    }

    MapConfig config;
    if (uninitializedMaps.containsKey(name)) {
      config = uninitializedMaps.get(name);
      fetchMap(config);
      uninitializedMaps.remove(name);
    } else if (maps.containsKey(name)) {
      config = maps.get(name);
      fetchMap(config);
      maps.remove(name);
    } else {
      throw new RuntimeException(
          "The map '" + name + "' was not defined in the XML configuration.");
    }

    maps.put(name, config);
    return notNull(config.mapData);
  }

  private void fetchMap(MapConfig mapConfig) throws SlickException {
    XMLCompound xmlMap = mapConfig.xmlMap;

    TiledMap tiledMap =
        new TiledMap(
            xmlMap.<XMLString>getValue(LOCATION_NODE).value,
            xmlMap.<XMLString>getValue(TILE_SETS_LOCATION_NODE).value);
    tiledMap.load();

    Set<BackgroundSceneData> backgroundScenes = new HashSet<>();
    for (XMLCompound background : xmlMap.<XMLList>getValue(BACKGROUNDS_NODE).children) {
      backgroundScenes.add(
          BackgroundSceneData.create(
              new Image(background.<XMLString>getValue(LOCATION_NODE).value),
              background.<XMLInteger>getValue(LAYER_NODE).value,
              background.<XMLInteger>getValue(Y_NODE).value));
    }
    mapConfig.setMapData(tiledMapParser.parseMap(tiledMap, backgroundScenes));
  }
}
