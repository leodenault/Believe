package believe.map.io;

import believe.map.data.LayerData;
import believe.map.tiled.TiledMap;

/**
 * Parser for interpreting a {@link believe.map.tiled.Layer} into {@link LayerData} usable in a
 * Believe application.
 */
interface TiledMapLayerParser {
  /**
   * Parses a {@link believe.map.tiled.Layer} into {@link LayerData}.
   *
   * @param tiledMap the {@link TiledMap} containing the layer to be parsed.
   * @param layerId the identifier for the layer to be parsed.
   */
  LayerData parseLayer(TiledMap tiledMap, int layerId);
}
