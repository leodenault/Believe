package believe.gui

import believe.geometry.Rectangle
import believe.geometry.rectangle
import believe.gui.TextAlignment.Horizontal
import believe.gui.TextAlignment.Vertical
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class TextAlignmentTest {
    @Test
    fun calculateXPosition_centered_returnsCorrectPosition() {
        assertThat(
            Horizontal.CENTERED.calculateXPosition(200, rectangle(100f, 200f, 500f, 1000f))
        ).isEqualTo(250f)
    }

    @Test
    fun calculateXPosition_centered_containerIsTooSmall_returnsCorrectPosition() {
        assertThat(
            Horizontal.CENTERED.calculateXPosition(200, rectangle(100f, 200f, 50f, 1000f))
        ).isEqualTo(25f)
    }

    @Test
    fun calculateXPosition_left_returnsCorrectPosition() {
        assertThat(
            Horizontal.LEFT.calculateXPosition(200, rectangle(100f, 200f, 500f, 1000f))
        ).isEqualTo(100f)
    }

    @Test
    fun calculateYPosition_middle_returnsCorrectPosition() {
        assertThat(
            Vertical.MIDDLE.calculateYPosition(500, rectangle(100f, 200f, 500f, 600f))
        ).isEqualTo(250f)
    }

    @Test
    fun calculateYPosition_middle_containerIsTooSmall_returnsCorrectPosition() {
        assertThat(
            Vertical.MIDDLE.calculateYPosition(500, rectangle(100f, 200f, 500f, 300f))
        ).isEqualTo(100f)
    }

    @Test
    fun calculateYPosition_top_returnsCorrectPosition() {
        assertThat(
            Vertical.TOP.calculateYPosition(500, rectangle(100f, 200f, 500f, 600f))
        ).isEqualTo(200f)
    }
}
