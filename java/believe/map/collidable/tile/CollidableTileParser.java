package believe.map.collidable.tile;

import believe.map.data.ObjectFactory;
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
  public ObjectFactory parseObject(TiledObject tiledObject) {
    return generatedMapEntityDataBuilder ->
        generatedMapEntityDataBuilder.addPhysicsManageable(
            new CollidableTile(tiledObject, collidableTileCollisionHandler));
  }
}
