package believe.map.tiled;

import static com.google.common.truth.Truth8.assertThat;

import believe.map.tiled.testing.FakeTiledMap;
import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/** Unit tests for {@link Tile}. */
public final class TileTest {
  @Test
  void getProperty_keyNotFound_returnsAbsent() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithDefaultPropertyValues();

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
}
