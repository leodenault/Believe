package believe.map.tiled.testing;

import static com.google.common.truth.Truth8.assertThat;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link FakeTiledMap}. */
public final class FakeTiledMapTest {
  @Test
  void getTileProperty_noValue_returnsEmpty() {
    assertThat(
            FakeTiledMap.tiledMapWithDefaultPropertyValues()
                .getTileProperty(123, "some property"))
        .isEmpty();
  }

  @Test
  void getObjectProperty_noValue_returnsEmpty() {
    assertThat(
            FakeTiledMap.tiledMapWithDefaultPropertyValues()
                .getObjectProperty(123, 123, "some property"))
        .isEmpty();
  }

  @Test
  void getTileProperty_valueExists_returnsExistingValue() {
    assertThat(
            FakeTiledMap.tiledMapWithTilePropertyValue("existing property value")
                .getTileProperty(123, "some property"))
        .hasValue("existing property value");
  }

  @Test
  void getObjectProperty_valueExists_returnsExistingValue() {
    assertThat(
            FakeTiledMap.tiledMapWithObjectPropertyValue("existing property value")
                .getObjectProperty(123, 123, "some property"))
        .hasValue("existing property value");
  }
}
