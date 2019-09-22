package believe.map.io;

import believe.map.data.BackgroundSceneData;
import believe.map.data.MapData;
import believe.map.io.InternalQualifiers.PlayerStartXProperty;
import believe.map.io.InternalQualifiers.PlayerStartYProperty;
import believe.map.tiled.TiledMap;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.List;
import java.util.Optional;

@Reusable
public final class TiledMapParserImpl implements TiledMapParser {
  private final String playerStartXProperty;
  private final String playerStartYProperty;
  private final TiledMapLayerParser tiledMapLayerParser;
  private final TiledMapObjectLayerParser tiledMapObjectLayerParser;

  @Inject
  TiledMapParserImpl(
      @PlayerStartXProperty String playerStartXProperty,
      @PlayerStartYProperty String playerStartYProperty,
      TiledMapLayerParser tiledMapLayerParser,
      TiledMapObjectLayerParser tiledMapObjectLayerParser) {
    this.playerStartXProperty = playerStartXProperty;
    this.playerStartYProperty = playerStartYProperty;
    this.tiledMapLayerParser = tiledMapLayerParser;
    this.tiledMapObjectLayerParser = tiledMapObjectLayerParser;
  }

  @Override
  public MapData parseMap(TiledMap tiledMap, List<BackgroundSceneData> backgroundScenes) {
    MapData.Builder mapDataBuilder =
        MapData.newBuilder(
            fetchNumber(tiledMap, playerStartXProperty) * tiledMap.getTileWidth(),
            fetchNumber(tiledMap, playerStartYProperty) * tiledMap.getTileHeight(),
            /* width= */ tiledMap.getWidth() * tiledMap.getTileWidth(),
            /* height= */ tiledMap.getHeight() * tiledMap.getTileHeight(),
            backgroundScenes);

    for (int layerId = 0; layerId < tiledMap.getLayerCount(); layerId++) {
      mapDataBuilder.addLayer(tiledMapLayerParser.parseLayer(tiledMap, layerId));
    }
    for (int objectGroupId = 0; objectGroupId < tiledMap.getObjectGroupCount(); objectGroupId++) {
      mapDataBuilder.addObjectLayer(
          tiledMapObjectLayerParser.parseObjectLayer(tiledMap, objectGroupId));
    }
    return mapDataBuilder.build();
  }

  private static int fetchNumber(TiledMap map, String propertyName) {
    int value = 0;
    Optional<String> stringNumber = map.getMapProperty(propertyName);

    if (stringNumber.isPresent()) {
      try {
        value = Integer.parseInt(stringNumber.get());
      } catch (NumberFormatException e) {
        Log.error(String.format("Could not fetch numeric property %s from map", propertyName));
      }
    }

    return value;
  }
}