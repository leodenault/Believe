package believe.map.tiled;

import believe.core.PropertyProvider;
import com.google.auto.value.AutoValue;

import java.util.Optional;

/** An object from a Tiled map. */
@AutoValue
public abstract class TiledObject {
  abstract PropertyProvider propertyProvider();

  /** The type of entity parsed from the object. */
  public abstract EntityType entityType();

  /** The x pixel position of the object within the map. */
  public abstract int x();

  /** The y pixel position of the object within the map. */
  public abstract int y();

  /** The width of the object. */
  public abstract int width();

  /** The height of the object. */
  public abstract int height();

  /**
   * Instantiates a {@link TiledObject}.
   *
   * @param tiledMap the {@link TiledMap} associated from which the object was parsed.
   * @param entityType the {@link EntityType} associated with the object.
   * @param x the x pixel position of the object within the map.
   * @param y the y pixel position of the object within the map.
   * @param width the width of the object.
   * @param height the height of the object.
   * @param layerId the ID of the layer containing the object.
   * @param objectId the ID of the object.
   */
  public static TiledObject create(
      TiledMap tiledMap,
      EntityType entityType,
      int x,
      int y,
      int width,
      int height,
      int layerId,
      int objectId) {
    return create(
        TiledMapObjectPropertyProvider.create(tiledMap, layerId, objectId),
        entityType,
        x,
        y,
        width,
        height);
  }

  /**
   * Instantiates a {@link TiledObject}.
   *
   * @param propertyProvider the {@link PropertyProvider} used by the created {@link TiledObject}.
   * @param entityType the {@link EntityType} associated with the object.
   */
  public static TiledObject create(PropertyProvider propertyProvider, EntityType entityType) {
    return create(
        propertyProvider, entityType, /* x= */ 0, /* y= */ 0, /* width= */ 0, /* height= */ 0);
  }

  private static TiledObject create(
      PropertyProvider propertyProvider,
      EntityType entityType,
      int x,
      int y,
      int width,
      int height) {
    return new AutoValue_TiledObject(propertyProvider, entityType, x, y, width, height);
  }

  /** Returns the value of a property for {@code key}. */
  public Optional<String> getProperty(String key) {
    return propertyProvider().getProperty(key);
  }
}
