package believe.map.io;

import believe.map.data.GeneratedMapEntityData;
import believe.map.data.EntityType;
import believe.map.tiled.TiledObject;

/** Parses an object from a Tiled map. */
public interface ObjectParser {
  /**
   * Parses a Tiled object and inserts the parsed data into a {@link
   * GeneratedMapEntityData.Builder}.
   *
   * @param entityType the {@link EntityType} of the object being parsed.
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
