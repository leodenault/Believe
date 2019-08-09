package believe.map.tiled;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link EntityType}. */
final class EntityTypeTest {
  @Test
  void forName_nameCorrespondsToEnumValue_returnsCorrectValue() {
    assertThat(EntityType.forName("cOlLiDaBlE_tIlE")).isEqualTo(EntityType.COLLIDABLE_TILE);
  }

  @Test
  void forName_nameDoesNotCorrespondToEnumValue_returnsUnknown() {
    assertThat(EntityType.forName("NOT_REAL")).isEqualTo(EntityType.UNKNOWN);
  }
}
