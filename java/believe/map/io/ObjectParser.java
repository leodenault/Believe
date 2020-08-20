package believe.map.io;

import believe.map.data.GeneratedMapEntityData;
import believe.map.data.EntityType;
import believe.map.tiled.TiledObject;

/** Parses an object from a Tiled map. */
public interface ObjectParser {
  /**
   * @param tiledObject the {@link TiledObject} containing the Tiled data on the object that will be
   *     parsed into {@code generatedMapEntityDataBuilder}.
   * @param generatedMapEntityDataBuilder the builder used to create a {@link
   *     GeneratedMapEntityData}.
   */
  void parseObject(
      EntityType entityType,
      TiledObject tiledObject,
      GeneratedMapEntityData.Builder generatedMapEntityDataBuilder);
}
