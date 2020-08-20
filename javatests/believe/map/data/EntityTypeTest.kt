package believe.map.data

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/** Unit tests for [EntityType].  */
internal class EntityTypeTest {
    @Test
    fun forName_nameCorrespondsToEnumValue_returnsCorrectValue() {
        assertThat(EntityType.forName("cOlLiDaBlE_tIlE")).isEqualTo(EntityType.COLLIDABLE_TILE)
    }

    @Test
    fun forName_nameDoesNotCorrespondToEnumValue_returnsUnknown() {
        assertThat(EntityType.forName("NOT_REAL")).isEqualTo(EntityType.UNKNOWN)
    }
}