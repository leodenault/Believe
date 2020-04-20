package believe.gui

import believe.geometry.Rectangle
import believe.gui.TextAlignment.Horizontal
import believe.gui.TextAlignment.Vertical
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class TextAlignmentTest {
    @Test
    fun calculateXPosition_centered_returnsCorrectPosition() {
        assertThat(
            Horizontal.CENTERED.calculateXPosition(200, Rectangle(100, 200, 500, 1000))
        ).isEqualTo(250)
    }

    @Test
    fun calculateXPosition_centered_containerIsTooSmall_returnsCorrectPosition() {
        assertThat(
            Horizontal.CENTERED.calculateXPosition(200, Rectangle(100, 200, 50, 1000))
        ).isEqualTo(25)
    }

    @Test
    fun calculateXPosition_left_returnsCorrectPosition() {
        assertThat(
            Horizontal.LEFT.calculateXPosition(200, Rectangle(100, 200, 500, 1000))
        ).isEqualTo(100)
    }

    @Test
    fun calculateYPosition_middle_returnsCorrectPosition() {
        assertThat(
            Vertical.MIDDLE.calculateYPosition(500, Rectangle(100, 200, 500, 600))
        ).isEqualTo(250)
    }

    @Test
    fun calculateYPosition_middle_containerIsTooSmall_returnsCorrectPosition() {
        assertThat(
            Vertical.MIDDLE.calculateYPosition(500, Rectangle(100, 200, 500, 300))
        ).isEqualTo(100)
    }

    @Test
    fun calculateYPosition_top_returnsCorrectPosition() {
        assertThat(
            Vertical.TOP.calculateYPosition(500, Rectangle(100, 200, 500, 600))
        ).isEqualTo(200)
    }
}
