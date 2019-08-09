package believe.map.collidable.tile;

import static com.google.common.truth.Truth.assertThat;

import believe.geometry.Rectangle;
import believe.map.tiled.EntityType;
import believe.map.tiled.Tile;
import believe.map.tiled.testing.FakeTiledMap;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/** Unit tests for {@link CollidableTile}. */
@InstantiateMocksIn
final class CollidableTileTest {
  private CollidableTile collidableTile;

  @Mock private CollidableTileCollisionHandler collidableTileCollisionHandler;

  @BeforeEach
  void setUp() {
    collidableTile =
        new CollidableTile(
            Tile.create(
                FakeTiledMap.tiledMapWithDefaultTilePropertyValue(),
                EntityType.COLLIDABLE_TILE,
                /* tileId= */ 123,
                /* tileX= */ 3,
                /* tileY= */ 5,
                /* tileWidth= */ 100,
                /* tileHeight= */ 100,
                /* layerId= */ 2),
            collidableTileCollisionHandler);
  }

  @Test
  void rect_containsTilePixelCoordinates() {
    Rectangle rect = collidableTile.rect();
    assertThat(rect.getX()).isWithin(0).of(300);
    assertThat(rect.getY()).isWithin(0).of(500);
    assertThat(rect.getWidth()).isWithin(0).of(100);
    assertThat(rect.getHeight()).isWithin(0).of(100);
  }

  @Test
  void leftCompatibleCollisionHandlers_returnsSetOfCollisionHandler() {
    assertThat(collidableTile.leftCompatibleHandlers())
        .containsExactly(collidableTileCollisionHandler);
  }

  @Test
  void rightCompatibleCollisionHandlers_returnsEmptySet() {
    assertThat(collidableTile.rightCompatibleHandlers()).isEmpty();
  }
}
