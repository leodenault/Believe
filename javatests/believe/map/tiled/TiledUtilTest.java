package believe.map.tiled;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link TiledUtil}. */
final class TiledUtilTest {
  @Test
  void isEmpty_idIsZero_returnsTrue() {
    assertThat(TiledUtil.isEmptyId(0)).isTrue();
  }

  @Test
  void isEmpty_idIsNonZero_returnsFalse() {
    assertThat(TiledUtil.isEmptyId(1)).isFalse();
  }
}
