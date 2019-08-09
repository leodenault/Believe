package believe.map.tiled.testing;

import static com.google.common.truth.Truth8.assertThat;

import com.google.common.truth.Truth8;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link FakeTiledMap}. */
public final class FakeTiledMapTest {
  @Test
  void getTileProperty_noValue_returnsDefault() {
    assertThat(
            FakeTiledMap.tiledMapWithDefaultTilePropertyValue()
                .getTileProperty(123, "some property"))
        .isEmpty();
  }

  @Test
  void getTileProperty_valueExists_returnsExistingValue() {
    assertThat(
            FakeTiledMap.tiledMapWithTilePropertyValue("existing property value")
                .getTileProperty(123, "some property"))
        .hasValue("existing property value");
  }
}
