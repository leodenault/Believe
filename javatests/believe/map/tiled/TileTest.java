package believe.map.tiled;

import static com.google.common.truth.Truth8.assertThat;

import believe.map.tiled.testing.FakeTiledMap;
import believe.testing.mockito.InstantiateMocksIn;
import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/** Unit tests for {@link Tile}. */
@InstantiateMocksIn
public final class TileTest {
  @Test
  void getProperty_keyNotFound_returnsAbsent() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithDefaultTilePropertyValue();

    Tile tile =
        Tile.create(
            tiledMap,
            EntityType.NONE,
            /* tileId= */ 12,
            /* x= */ 123,
            /* y= */ 321,
            /* width= */ 800,
            /* height= */ 1600,
            /* layerId= */ 12);

    assertThat(tile.getProperty("doesn't exist")).isEmpty();
  }

  @Test
  void getProperty_keyExists_returnsAssociatedValue() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithTilePropertyValue("existing value");

    Tile tile =
        Tile.create(
            tiledMap,
            EntityType.NONE,
            /* tileId= */ 12,
            /* x= */ 123,
            /* y= */ 321,
            /* width= */ 800,
            /* height= */ 1600,
            /* layerId= */ 12);

    Optional<String> actualValue = tile.getProperty("exists");
    assertThat(actualValue).isPresent();
    Truth.assertThat(actualValue.get()).isEqualTo("existing value");
  }
  
  @Test
  void hasLeftNeighbour_tileHasLeftNeighbour_returnsTrue() {

  }
  
  @Test
  void hasLeftNeighbour_tileDoesNotHaveLeftNeighbour_returnsFalse() {}
  
  @Test
  void hasUpNeighbour_tileHasUpNeighbour_returnsTrue() {}
  
  @Test
  void hasUpNeighbour_tileDoesNotHaveUpNeighbour_returnsFalse() {}
  
  @Test
  void hasRightNeighbour_tileHasRightNeighbour_returnsTrue() {}
  
  @Test
  void hasRightNeighbour_tileDoesNotHaveRightNeighbour_returnsFalse() {}
  
  @Test
  void hasDownNeighbour_tileHasDownNeighbour_returnsTrue() {}
  
  @Test
  void hasDownNeighbour_tileDoesNotHaveDownNeighbour_returnsFalse() {}
}
