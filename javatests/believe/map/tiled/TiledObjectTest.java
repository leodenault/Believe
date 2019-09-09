package believe.map.tiled;

import static com.google.common.truth.Truth8.assertThat;

import believe.map.tiled.testing.FakeTiledMap;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/** Unit tests for {@link TiledObject}. */
final class TiledObjectTest {
  @Test
  void getProperty_keyNotFound_returnsAbsent() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithDefaultPropertyValues();

    TiledObject object =
        TiledObject.create(
            tiledMap,
            EntityType.NONE,
            /* x= */ 1,
            /* y= */ 2,
            /* width= */ 3,
            /* height= */ 4,
            /* layerId= */ 12,
            /* objectId= */ 65);

    assertThat(object.getProperty("doesn't exist")).isEmpty();
  }

  @Test
  void getProperty_keyExists_returnsAssociatedValue() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithObjectPropertyValue("existing value");

    TiledObject object =
        TiledObject.create(
            tiledMap,
            EntityType.NONE,
            /* x= */ 1,
            /* y= */ 2,
            /* width= */ 3,
            /* height= */ 4,
            /* layerId= */ 12,
            /* objectId= */ 65);

    Optional<String> actualValue = object.getProperty("exists");
    assertThat(actualValue).isPresent();
    assertThat(actualValue).hasValue("existing value");
  }
}
