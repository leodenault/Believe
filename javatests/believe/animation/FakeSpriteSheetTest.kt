package believe.animation

import believe.animation.testing.FakeSpriteSheet
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class FakeSpriteSheetTest {
    private val fakeSpriteSheet = FakeSpriteSheet(numRows = 2, numColumns = 3)

    @Test
    fun horizontalCount_returnsNumberOfColumns() {
        assertThat(fakeSpriteSheet.horizontalCount).isEqualTo(3)
    }

    @Test
    fun verticalCount_returnsNumberOfRows() {
        assertThat(fakeSpriteSheet.verticalCount).isEqualTo(2)
    }

    @Test
    fun imageAt_returnsCorrectImage() {
        with(fakeSpriteSheet) {
            assertThat(imageAt(0)).isEqualTo(getSprite(0, 0))
            assertThat(imageAt(1)).isEqualTo(getSprite(1, 0))
            assertThat(imageAt(2)).isEqualTo(getSprite(2, 0))
            assertThat(imageAt(3)).isEqualTo(getSprite(0, 1))
            assertThat(imageAt(4)).isEqualTo(getSprite(1, 1))
            assertThat(imageAt(5)).isEqualTo(getSprite(2, 1))
        }
    }

    @Test
    fun fakeSpriteSheet_imageAt_outOfBounds_throwsIndexOutOfBoundException() {
        with(fakeSpriteSheet) {
            assertThrows<IndexOutOfBoundsException> { imageAt(-1) }
            assertThrows<IndexOutOfBoundsException> { imageAt(6) }
        }
    }

    @Test
    fun imagesBetween_returnsCorrectImageRange() {
        with(fakeSpriteSheet) {
            assertThat(imagesBetween(IntRange.EMPTY)).isEmpty()
            assertThat(imagesBetween(0..0)).containsExactly(imageAt(0))
            assertThat(imagesBetween(0 until 6)).containsExactly(
                imageAt(0), imageAt(1), imageAt(2), imageAt(3), imageAt(4), imageAt(5)
            )
        }
    }

    @Test
    fun fakeSpriteSheet_imagesBetween_outOfBounds_throwsIndexOutOfBoundException() {
        with(fakeSpriteSheet) {
            assertThrows<IndexOutOfBoundsException> { imagesBetween(-1..35) }
            assertThrows<IndexOutOfBoundsException> { imagesBetween(0..36) }
        }
    }
}
