package believe.map.data;

import believe.core.PropertyProvider;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

/** A tile within the context of a [TiledMap]. */
@AutoValue
public abstract class TileData implements PropertyProvider {
  abstract PropertyProvider propertyProvider();

  public abstract EntityType getEntityType();

  public abstract int getPixelX();

  public abstract int getPixelY();

  public abstract int getWidth();

  public abstract int getHeight();

  @Override
  @Nullable
  public String getProperty(String key) {
    return propertyProvider().getProperty(key);
  }

  /**
   * Create a new instance of a {@link TileData}.
   *
   * @param propertyProvider the {@link PropertyProvider} containing the tile's properties.
   * @param entityType the x pixel position of the tile within the map.
   * @param pixelX the X position, in pixels, of the tile.
   * @param pixelY the Y position, in pixels, of the tile.
   * @param width the width of the tile.
   * @param height the height of the tile.
   */
  public static TileData create(
      PropertyProvider propertyProvider,
      EntityType entityType,
      int pixelX,
      int pixelY,
      int width,
      int height) {
    return new AutoValue_TileData(propertyProvider, entityType, pixelX, pixelY, width, height);
  }

  /**
   * Create a new instance of a {@link TileData}.
   *
   * @param entityType the x pixel position of the tile within the map.
   * @param pixelX the X position, in pixels, of the tile.
   * @param pixelY the Y position, in pixels, of the tile.
   * @param width the width of the tile.
   * @param height the height of the tile.
   */
  public static TileData create(
      EntityType entityType, int pixelX, int pixelY, int width, int height) {
    return new AutoValue_TileData(key -> null, entityType, pixelX, pixelY, width, height);
  }

  /**
   * Create a new instance of a {@link TileData}.
   *
   * @param tiledTile the internal {@link believe.map.tiled.Tile} instance backing this one.
   * @param entityType the x pixel position of the tile within the map.
   */
  public static TileData create(believe.map.tiled.Tile tiledTile, EntityType entityType) {
    return create(key -> null, entityType, 0, 0, tiledTile.getWidth(), tiledTile.getHeight());
  }
}
