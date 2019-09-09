package believe.map.io;

import believe.map.data.ObjectLayerData;
import believe.map.tiled.TiledMap;

/** Parser that takes as input an object layer from a Tiled map. */
interface TiledMapObjectLayerParser {
  /**
   * Parses an object layer from a Tiled map.
   *
   * @param tiledMap the {@link TiledMap} from which to parse the object layer.
   * @param layerId the ID of the layer within the map.
   * @return an {@link ObjectLayerData} holding information from the layer.
   */
  ObjectLayerData parseObjectLayer(TiledMap tiledMap, int layerId);
}
