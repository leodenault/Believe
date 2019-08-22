package believe.map.io;

import believe.map.data.GeneratedMapEntityData;
import believe.map.tiled.Tile;
import believe.map.tiled.TiledMap;

/** Generates entities on a map based on specially-annotated tiles. */
public interface TileParser {
  /**
   * Generate an entity for the map based on {@code tile}.
   *
   * @param map the {@link TiledMap} from which to get data on {@code tile}.
   * @param tile the tile data that should be used to output the entity.
   * @param generatedMapEntityDataBuilder the {@link GeneratedMapEntityData.Builder} to which
   *     generated entities will be added.
   */
  void parseTile(
      TiledMap map, Tile tile, GeneratedMapEntityData.Builder generatedMapEntityDataBuilder);
}
