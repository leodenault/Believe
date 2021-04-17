package believe.map.collidable.tile;

import static believe.map.data.testing.Truth.assertThat;
import static believe.map.tiled.testing.TiledFakes.fakeTiledObject;
import static com.google.common.truth.Truth.assertThat;

import believe.map.data.EntityType;
import believe.map.data.ObjectFactory;
import believe.map.data.testing.Truth;
import believe.scene.GeneratedMapEntityData;
import believe.physics.manager.PhysicsManageable;
import believe.testing.mockito.InstantiateMocksIn;
import com.google.common.truth.Correspondence;
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
  void parseTile_generatesValidTile() {
    ObjectFactory factory = generator.parseObject(fakeTiledObject(""));

    assertThat(factory).outputPhysicsManageableSet().hasSize(1);
  }
}
