package believe.map.tiled.testing;

import believe.map.tiled.EntityType;
import believe.map.tiled.TiledObject;

import java.util.Optional;

/** Creates fake instances of {@link believe.map.tiled.TiledObject} */
public abstract class FakeTiledObjectFactory {
  /**
   * Instantiates a fake {@link TiledObject}.
   *
   * @param entityType the {@link EntityType} associated with the object.
   */
  public static TiledObject create(EntityType entityType) {
    return TiledObject.create(
        entityType,
        key -> Optional.empty(),
        /* x= */ 0,
        /* y= */ 0,
        /* width= */ 0,
        /* height= */ 0);
  }
}
