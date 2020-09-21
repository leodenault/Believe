package believe.map.collidable.tile;

import believe.map.data.EntityType;
import believe.map.data.GeneratedMapEntityData;
import believe.map.io.ObjectParser;
import believe.map.tiled.TiledObject;
import dagger.Reusable;
import javax.inject.Inject;

@Reusable
final class CollidableTileParser implements ObjectParser {
  private final CollidableTileCollisionHandler collidableTileCollisionHandler;

  @Inject
  CollidableTileParser(CollidableTileCollisionHandler collidableTileCollisionHandler) {
    this.collidableTileCollisionHandler = collidableTileCollisionHandler;
  }

  @Override
  public void parseObject(
      EntityType entityType,
      TiledObject tiledObject,
      GeneratedMapEntityData.Builder generatedMapEntityDataBuilder) {
    if (entityType != EntityType.COLLIDABLE_TILE) {
      return;
    }

    generatedMapEntityDataBuilder.addPhysicsManageable(
        new CollidableTile(tiledObject, collidableTileCollisionHandler));
  }
}
