package believe.map.io;

import believe.map.data.TiledMapData;
import believe.map.tiled.TiledMap;
import org.newdawn.slick.util.Log;

import java.util.Optional;

interface TiledMapParser {
  static TiledMapParser create(
      String playerStartXProperty,
      String playerStartYProperty,
      TiledMapLayerParser tiledMapLayerParser,
      TiledMapObjectLayerParser tiledMapObjectLayerParser) {
    return new TiledMapParser() {
      @Override
      public TiledMapData parse(TiledMap tiledMap) {
        TiledMapData.Builder tiledMapData =
            TiledMapData.newBuilder(
                fetchNumber(tiledMap, playerStartXProperty) * tiledMap.getTileWidth(),
                fetchNumber(tiledMap, playerStartYProperty) * tiledMap.getTileHeight(),
                /* width= */ tiledMap.getWidth() * tiledMap.getTileWidth(),
                /* height= */ tiledMap.getHeight() * tiledMap.getTileHeight());

        for (int layerId = 0; layerId < tiledMap.getLayerCount(); layerId++) {
          tiledMapData.addLayer(tiledMapLayerParser.parseLayer(tiledMap, layerId));
        }
        for (int objectGroupId = 0;
            objectGroupId < tiledMap.getObjectGroupCount();
            objectGroupId++) {
          tiledMapData.addObjectLayer(
              tiledMapObjectLayerParser.parseObjectLayer(tiledMap, objectGroupId));
        }
        return tiledMapData.build();
      }

      private int fetchNumber(TiledMap map, String propertyName) {
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
    };
  }

  TiledMapData parse(TiledMap tiledMap);
}
