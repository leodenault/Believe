package believe.map.collidable.tile;

import static believe.map.tiled.testing.TiledFakes.fakeTiledObject;
import static com.google.common.truth.Truth.assertThat;

import believe.map.data.EntityType;
import believe.map.data.GeneratedMapEntityData;
import believe.physics.manager.PhysicsManageable;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.Set;

/** Unit tests for {@link CollidableTileParser}. */
@InstantiateMocksIn
final class CollidableTileParserTest {
  private final GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
      GeneratedMapEntityData.newBuilder();

  private CollidableTileParser generator;

  @Mock private CollidableTileCollisionHandler collidableTileCollisionHandler;

  @BeforeEach
  void setUp() {
    generator = new CollidableTileParser(collidableTileCollisionHandler);
  }

  @Test
  void parseTile_entityIsNotTile_doesNothing() {
    generator.parseObject(EntityType.ENEMY, fakeTiledObject(""), generatedMapEntityDataBuilder);

    assertThat(generatedMapEntityDataBuilder.build().physicsManageables()).isEmpty();
  }

  @Test
  void parseTile_entityIsTile_generatesValidTile() {
    generator.parseObject(
        EntityType.COLLIDABLE_TILE, fakeTiledObject(""), generatedMapEntityDataBuilder);

    Set<PhysicsManageable> physicsManageables =
        generatedMapEntityDataBuilder.build().physicsManageables();
    assertThat(physicsManageables).hasSize(1);
    PhysicsManageable physicsManageable = physicsManageables.stream().findFirst().get();
    assertThat(physicsManageable).isInstanceOf(CollidableTile.class);
  }
}
