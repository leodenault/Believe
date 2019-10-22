package believe.map.tiled;

import believe.core.PropertyProvider;
import com.google.auto.value.AutoValue;

import java.util.Optional;

/** An object from a Tiled map. */
@AutoValue
public abstract class TiledObject {

  /** The type of entity parsed from the object. */
  public abstract EntityType entityType();

  /** The {@link PropertyProvider} that exposes string properties corresponding to string keys. */
  public abstract PropertyProvider propertyProvider();

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
   * @param entityType the {@link EntityType} associated with the object.
   * @param propertyProvider the {@link PropertyProvider} used by the created {@link TiledObject}.
   * @param x the x pixel position of the object within the map.
   * @param y the y pixel position of the object within the map.
   * @param width the width of the object.
   * @param height the height of the object.
   */
  public static TiledObject create(
      EntityType entityType,
      PropertyProvider propertyProvider,
      int x,
      int y,
      int width,
      int height) {
    return new AutoValue_TiledObject(entityType, propertyProvider, x, y, width, height);
  }
}
