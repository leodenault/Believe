package believe.map.tiled.testing;

import believe.map.tiled.EntityType;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledObject;

/** Creates fake instances of {@link believe.map.tiled.TiledObject} */
public abstract class FakeTiledObjectFactory {
  /**
   * Instantiates a fake {@link TiledObject}.
   *
   * @param entityType the {@link EntityType} associated with the object.
   */
  public static TiledObject create(EntityType entityType) {
    return create(FakeTiledMap.tiledMapWithDefaultPropertyValues(), entityType);
  }

  /**
   * Instantiates a fake {@link TiledObject}.
   *
   * @param tiledMap the {@link TiledMap} instance from which the generated {@link TiledObject} will
   *     retrive its data.
   * @param entityType the {@link EntityType} associated with the object.
   */
  public static TiledObject create(TiledMap tiledMap, EntityType entityType) {
    return TiledObject.create(
        tiledMap,
        entityType,
        /* x= */ 0,
        /* y= */ 0,
        /* width= */ 0,
        /* height= */ 0,
        /* layerId= */ 0,
        /* objectId,= */ 0);
  }
}
