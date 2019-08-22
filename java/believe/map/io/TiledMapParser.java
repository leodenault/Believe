package believe.map.io;

import believe.map.data.BackgroundSceneData;
import believe.map.data.MapData;
import believe.map.tiled.TiledMap;

import java.util.List;

/** Parser for extracting data out of a Tiled map. */
public interface TiledMapParser {
  /**
   * Parses a map based on the contents for {@code tiledMap}.
   *
   * @param tiledMap the metadata containing about the map.
   * @param backgroundSceneData the set of {@link BackgroundSceneData} instances describing the
   *     background that will be rendered behind the map.
   * @return a {@link MapData} containing the parsed information on the map.
   */
  MapData parseMap(TiledMap tiledMap, List<BackgroundSceneData> backgroundSceneData);
}
