package believe.map.io;

import believe.map.data.ObjectLayerData;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledObjectGroup;

/** Parser that takes as input an object layer from a Tiled map. */
interface TiledMapObjectLayerParser {
  /**
   * Parses an object layer from a Tiled map.
   *
   * @param tiledObjectGroup the {@link TiledObjectGroup} to be parsed.
   * @return an {@link ObjectLayerData} holding information from the layer.
   */
  ObjectLayerData parseObjectGroup(TiledObjectGroup tiledObjectGroup);
}
