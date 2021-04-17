package believe.map.io;

import believe.map.data.ObjectFactory;
import believe.map.tiled.TiledObject;

/** Parses an object from a Tiled map. */
public interface ObjectParser {
  /**
   * Parses a Tiled object and returns an {@link ObjectFactory} that instantiates new instances of
   * the object based on its data. Calls to this method guarantee that the entity type of the Tiled
   * object being parsed corresponds to this parser.
   *
   * @param tiledObject the {@link TiledObject} containing the Tiled data on the object that will be
   *     parsed.
   */
  ObjectFactory parseObject(TiledObject tiledObject);
}
