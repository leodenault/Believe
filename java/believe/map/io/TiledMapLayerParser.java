package believe.map.io;

import believe.map.data.LayerData;
import believe.map.tiled.Layer;

/**
 * Parser for interpreting a {@link believe.map.tiled.Layer} into {@link LayerData} usable in a
 * Believe application.
 */
interface TiledMapLayerParser {
  /**
   * Parses a {@link believe.map.tiled.Layer} into {@link LayerData}.
   *
   * @param layer the layer to be parsed.
   */
  LayerData parseLayer(Layer layer);
}
