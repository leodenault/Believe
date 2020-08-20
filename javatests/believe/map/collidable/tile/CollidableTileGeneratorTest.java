package believe.map.collidable.tile;

import static com.google.common.truth.Truth.assertThat;

import believe.map.data.EntityType;
import believe.map.data.GeneratedMapEntityData;
import believe.map.data.TileData;
import believe.physics.manager.PhysicsManageable;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Set;

/** Unit tests for {@link CollidableTileGenerator}. */
@InstantiateMocksIn
final class CollidableTileGeneratorTest {
  private final GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
      GeneratedMapEntityData.newBuilder();

  private CollidableTileGenerator generator;

  @Mock private CollidableTileCollisionHandler collidableTileCollisionHandler;

  @BeforeEach
  void setUp() {
    generator = new CollidableTileGenerator(collidableTileCollisionHandler);
  }

  @Test
  void parseTile_entityIsNotTile_doesNothing() {
    generator.parseTile(
        TileData.create(
            EntityType.ENEMY,
            /* tileX= */ 0,
            /* tileY= */ 0,
            /* tileWidth= */ 0,
            /* tileHeight= */ 0),
        generatedMapEntityDataBuilder);

    assertThat(generatedMapEntityDataBuilder.build().physicsManageables()).isEmpty();
  }

  @Test
  void parseTile_entityIsTile_generatesValidTile() {
    generator.parseTile(
        TileData.create(
            EntityType.COLLIDABLE_TILE,
            /* tileX= */ 0,
            /* tileY= */ 0,
            /* tileWidth= */ 0,
            /* tileHeight= */ 0),
        generatedMapEntityDataBuilder);

    Set<PhysicsManageable> physicsManageables =
        generatedMapEntityDataBuilder.build().physicsManageables();
    assertThat(physicsManageables).hasSize(1);
    PhysicsManageable physicsManageable = physicsManageables.stream().findFirst().get();
    assertThat(physicsManageable).isInstanceOf(CollidableTile.class);
  }
}
