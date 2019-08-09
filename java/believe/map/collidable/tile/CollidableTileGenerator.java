package believe.map.collidable.tile;

import believe.map.data.GeneratedMapEntityData;
import believe.map.io.TileParser;
import believe.map.tiled.EntityType;
import believe.map.tiled.Tile;
import believe.map.tiled.TiledMap;
import dagger.Reusable;
import javax.inject.Inject;

import java.util.Optional;

@Reusable
final class CollidableTileGenerator implements TileParser {
  private final CollidableTileCollisionHandler collidableTileCollisionHandler;

  @Inject
  CollidableTileGenerator(CollidableTileCollisionHandler collidableTileCollisionHandler) {
    this.collidableTileCollisionHandler = collidableTileCollisionHandler;
  }

  @Override
  public void parseTile(
      TiledMap map, Tile tile, GeneratedMapEntityData.Builder generatedMapEntityDataBuilder) {
    if (tile.entityType() != EntityType.COLLIDABLE_TILE) {
      return;
    }

    generatedMapEntityDataBuilder.addPhysicsManageable(
        new CollidableTile(tile, collidableTileCollisionHandler));
  }
}
