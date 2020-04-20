package believe.geometry

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class RectangleTest {
    @Test
    fun intersectionShouldReturnIntersectionOfTwoRectangles() {
        with(Rectangle(10, 10, 50, 30).intersection(Rectangle(0, 10, 20, 10))) {
            assertThat(x).isEqualTo(10f)
            assertThat(y).isEqualTo(10f)
            assertThat(width).isEqualTo(10f)
            assertThat(height).isEqualTo(10f)
        }
        with(Rectangle(10, 20, 50, 30).intersection(Rectangle(5, 26, 100, 30))) {
            assertThat(x).isEqualTo(10f)
            assertThat(y).isEqualTo(26f)
            assertThat(width).isEqualTo(50f)
            assertThat(height).isEqualTo(24f)
        }
    }

    @Test
    fun intersectionShouldReturnSmallestRectangleIfCompletelyInLargerOne() {
        with(Rectangle(0, 0, 100, 100).intersection(Rectangle(0, 0, 50, 50))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(50f)
            assertThat(height).isEqualTo(50f)
        }
        with(Rectangle(0, 0, 100, 100).intersection(Rectangle(0, 0, 100, 100))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(100f)
            assertThat(height).isEqualTo(100f)
        }
        with(Rectangle(265, 15, 490, 100).intersection(Rectangle(265, 15, 490, 100))) {
            assertThat(x).isEqualTo(265f)
            assertThat(y).isEqualTo(15f)
            assertThat(width).isEqualTo(490f)
            assertThat(height).isEqualTo(100f)
        }
        with(Rectangle(0, 0, 100, 100).intersection(Rectangle(10, 10, 50, 50))) {
            assertThat(x).isEqualTo(10f)
            assertThat(y).isEqualTo(10f)
            assertThat(width).isEqualTo(50f)
            assertThat(height).isEqualTo(50f)
        }
        with(Rectangle(10, 10, 50, 50).intersection(Rectangle(0, 0, 100, 100))) {
            assertThat(x).isEqualTo(10f)
            assertThat(y).isEqualTo(10f)
            assertThat(width).isEqualTo(50f)
            assertThat(height).isEqualTo(50f)
        }
    }

    @Test
    fun intersectionShouldReturnZeroWidthHeightRectangleIfNoIntersection() {
        with(Rectangle(0, 0, 10, 15).intersection(Rectangle(100, 1000, 10, 15))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(0f)
            assertThat(height).isEqualTo(
                0f
            )
        }
        with(Rectangle(0, 0, 10, 10).intersection(Rectangle(10, 0, 10, 10))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(0f)
            assertThat(height).isEqualTo(
                0f
            )
        }
        with(Rectangle(0, 0, 10, 10).intersection(Rectangle(0, 10, 10, 10))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(0f)
            assertThat(height).isEqualTo(
                0f
            )
        }
    }

    @Test
    fun horizontalCollisionDirectionShouldReturnTrueIfRightOfCenter() {
        val rect1 = Rectangle(0, 0, 20, 20)
        var rect2 = Rectangle(10, 0, 20, 20)

        assertThat(rect1.horizontalCollisionDirection(rect2)).isTrue()
        rect2 = Rectangle(1, 20, 20, 20)
        assertThat(rect1.horizontalCollisionDirection(rect2)).isTrue()
    }

    @Test
    fun horizontalCollisionDirectionShouldReturnFalseIfLeftOfCenter() {
        val rect1 = Rectangle(0, 0, 20, 20)
        var rect2 = Rectangle(-1, 0, 20, 20)

        assertThat(rect1.horizontalCollisionDirection(rect2)).isFalse()
        rect2 = Rectangle(0, 20, 20, 20)
        assertThat(rect1.horizontalCollisionDirection(rect2)).isFalse()
    }

    @Test
    fun verticalCollisionDirectionShouldReturnTrueIfBelowCenter() {
        val rect1 = Rectangle(0, 0, 20, 20)
        var rect2 = Rectangle(-1, 550, 20, 20)

        assertThat(rect1.verticalCollisionDirection(rect2)).isTrue()
        rect2 = Rectangle(0, 1, 20, 20)
        assertThat(rect1.verticalCollisionDirection(rect2)).isTrue()
    }

    @Test
    fun verticalCollisionDirectionShouldReturnFalseIfAboveCenter() {
        val rect1 = Rectangle(0, 0, 20, 20)
        var rect2 = Rectangle(-1, -42, 20, 20)

        assertThat(rect1.verticalCollisionDirection(rect2)).isFalse()
        rect2 = Rectangle(0, 0, 20, 20)
        assertThat(rect1.verticalCollisionDirection(rect2)).isFalse()
    }

    @Test
    fun intersectsShouldReturnFalseWhenTouchingSameLine() {
        val rect1 = Rectangle(0, 0, 20, 20)
        val rect2 = Rectangle(20, 0, 20, 20)
        val rect3 = Rectangle(0, 20, 20, 20)

        assertThat(rect1.intersects(rect2)).isFalse()
        assertThat(rect1.intersects(rect3)).isFalse()
    }
}