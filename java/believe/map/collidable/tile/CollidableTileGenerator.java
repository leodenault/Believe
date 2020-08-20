package believe.map.collidable.tile;

import believe.map.data.GeneratedMapEntityData;
import believe.map.data.TileData;
import believe.map.io.TileParser;
import believe.map.data.EntityType;
import believe.map.tiled.TiledMap;
import dagger.Reusable;
import javax.inject.Inject;

@Reusable
final class CollidableTileGenerator implements TileParser {
  private final CollidableTileCollisionHandler collidableTileCollisionHandler;

  @Inject
  CollidableTileGenerator(CollidableTileCollisionHandler collidableTileCollisionHandler) {
    this.collidableTileCollisionHandler = collidableTileCollisionHandler;
  }

  @Override
  public void parseTile(
      TileData tileData,
      GeneratedMapEntityData.Builder generatedMapEntityDataBuilder) {
    if (tileData.getEntityType() != EntityType.COLLIDABLE_TILE) {
      return;
    }

    generatedMapEntityDataBuilder.addPhysicsManageable(
        new CollidableTile(tileData, collidableTileCollisionHandler));
  }
}
