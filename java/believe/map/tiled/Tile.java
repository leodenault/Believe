package believe.map.tiled;

import com.google.auto.value.AutoValue;

import java.util.Optional;

/** A tile within the context of a {@link TiledMap}. */
@AutoValue
public abstract class Tile {
  abstract TiledMap tiledMap();

  /** The type of entity parsed from the tile. */
  public abstract EntityType entityType();

  abstract int tileId();

  /** The x pixel position of the tile within the map. */
  public abstract int pixelX();

  /** The x tile position relative to other tiles of the tile within the map. */
  public abstract int tileX();

  /** The y pixel position of the tile within the map. */
  public abstract int pixelY();

  /** The y tile position relative to other tiles of the tile within the map. */
  public abstract int tileY();

  /** The width of the tile. */
  public abstract int width();

  /** The height of the tile. */
  public abstract int height();

  /** The ID of the layer containing this tile. */
  public abstract int layerId();

  public static Tile create(
      TiledMap tiledMap,
      EntityType entityType,
      int tileId,
      int x,
      int y,
      int width,
      int height,
      int layerId) {
    return new AutoValue_Tile(
        tiledMap, entityType, tileId, x * width, x, y * height, y, width, height, layerId);
  }

  /** Returns the value of a property for {@code key}. */
  public Optional<String> getProperty(String key) {
    return tiledMap().getTileProperty(tileId(), key);
  }
}
