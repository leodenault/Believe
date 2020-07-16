package believe.geometry

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class RectangleTest {
    @Test
    fun rectangle_fromProperties_buildsCorrectRectangle() {
        with(rectangle(1.2f, 3.4f, 5.6f, 7.8f)) {
            assertThat(x).isEqualTo(1.2f)
            assertThat(y).isEqualTo(3.4f)
            assertThat(width).isEqualTo(5.6f)
            assertThat(height).isEqualTo(7.8f)
        }
    }

    @Test
    fun rectangle_fromSlickRectangle_buildsCorrectRectangle() {
        with(rectangle(org.newdawn.slick.geom.Rectangle(1.2f, 3.4f, 5.6f, 7.8f))) {
            assertThat(x).isEqualTo(1.2f)
            assertThat(y).isEqualTo(3.4f)
            assertThat(width).isEqualTo(5.6f)
            assertThat(height).isEqualTo(7.8f)
        }
    }
    @Test
    fun mutableRectangle_fromProperties_buildsCorrectRectangle() {
        with(mutableRectangle(1.2f, 3.4f, 5.6f, 7.8f)) {
            assertThat(x).isEqualTo(1.2f)
            assertThat(y).isEqualTo(3.4f)
            assertThat(width).isEqualTo(5.6f)
            assertThat(height).isEqualTo(7.8f)
        }
    }

    @Test
    fun mutableRectangle_fromSlickRectangle_buildsCorrectRectangle() {
        with(mutableRectangle(org.newdawn.slick.geom.Rectangle(1.2f, 3.4f, 5.6f, 7.8f))) {
            assertThat(x).isEqualTo(1.2f)
            assertThat(y).isEqualTo(3.4f)
            assertThat(width).isEqualTo(5.6f)
            assertThat(height).isEqualTo(7.8f)
        }
    }

    @Test
    fun asSlickRectangle_returnsCorrectSlickRectangle() {
        with(rectangle(1f, 2f, 3f, 4f).asSlickRectangle()) {
            assertThat(x).isEqualTo(1f)
            assertThat(y).isEqualTo(2f)
            assertThat(width).isEqualTo(3f)
            assertThat(height).isEqualTo(4f)
        }
    }

    @Test
    fun intersectionShouldReturnIntersectionOfTwoRectangles() {
        with(rectangle(10f, 10f, 50f, 30f).intersection(rectangle(0f, 10f, 20f, 10f))) {
            assertThat(x).isEqualTo(10f)
            assertThat(y).isEqualTo(10f)
            assertThat(width).isEqualTo(10f)
            assertThat(height).isEqualTo(10f)
        }
        with(rectangle(10f, 20f, 50f, 30f).intersection(rectangle(5f, 26f, 100f, 30f))) {
            assertThat(x).isEqualTo(10f)
            assertThat(y).isEqualTo(26f)
            assertThat(width).isEqualTo(50f)
            assertThat(height).isEqualTo(24f)
        }
    }

    @Test
    fun intersectionShouldReturnSmallestRectangleIfCompletelyInLargerOne() {
        with(rectangle(0f, 0f, 100f, 100f).intersection(rectangle(0f, 0f, 50f, 50f))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(50f)
            assertThat(height).isEqualTo(50f)
        }
        with(rectangle(0f, 0f, 100f, 100f).intersection(rectangle(0f, 0f, 100f, 100f))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(100f)
            assertThat(height).isEqualTo(100f)
        }
        with(rectangle(265f, 15f, 490f, 100f).intersection(rectangle(265f, 15f, 490f, 100f))) {
            assertThat(x).isEqualTo(265f)
            assertThat(y).isEqualTo(15f)
            assertThat(width).isEqualTo(490f)
            assertThat(height).isEqualTo(100f)
        }
        with(rectangle(0f, 0f, 100f, 100f).intersection(rectangle(10f, 10f, 50f, 50f))) {
            assertThat(x).isEqualTo(10f)
            assertThat(y).isEqualTo(10f)
            assertThat(width).isEqualTo(50f)
            assertThat(height).isEqualTo(50f)
        }
        with(rectangle(10f, 10f, 50f, 50f).intersection(rectangle(0f, 0f, 100f, 100f))) {
            assertThat(x).isEqualTo(10f)
            assertThat(y).isEqualTo(10f)
            assertThat(width).isEqualTo(50f)
            assertThat(height).isEqualTo(50f)
        }
    }

    @Test
    fun intersectionShouldReturnZeroWidthHeightRectangleIfNoIntersection() {
        with(rectangle(0f, 0f, 10f, 15f).intersection(rectangle(100f, 1000f, 10f, 15f))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(0f)
            assertThat(height).isEqualTo(0f)
        }
        with(rectangle(0f, 0f, 10f, 10f).intersection(rectangle(10f, 0f, 10f, 10f))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(0f)
            assertThat(height).isEqualTo(0f)
        }
        with(rectangle(0f, 0f, 10f, 10f).intersection(rectangle(0f, 10f, 10f, 10f))) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(0f)
            assertThat(height).isEqualTo(0f)
        }
    }

    @Test
    fun horizontalCollisionDirectionShouldReturnTrueIfRightOfCenter() {
        val rect1 = rectangle(0f, 0f, 20f, 20f)
        var rect2 = rectangle(10f, 0f, 20f, 20f)

        assertThat(rect1.horizontalCollisionDirection(rect2)).isTrue()
        rect2 = rectangle(1f, 20f, 20f, 20f)
        assertThat(rect1.horizontalCollisionDirection(rect2)).isTrue()
    }

    @Test
    fun horizontalCollisionDirectionShouldReturnFalseIfLeftOfCenter() {
        val rect1 = rectangle(0f, 0f, 20f, 20f)
        var rect2 = rectangle(-1f, 0f, 20f, 20f)

        assertThat(rect1.horizontalCollisionDirection(rect2)).isFalse()
        rect2 = rectangle(0f, 20f, 20f, 20f)
        assertThat(rect1.horizontalCollisionDirection(rect2)).isFalse()
    }

    @Test
    fun verticalCollisionDirectionShouldReturnTrueIfBelowCenter() {
        val rect1 = rectangle(0f, 0f, 20f, 20f)
        var rect2 = rectangle(-1f, 550f, 20f, 20f)

        assertThat(rect1.verticalCollisionDirection(rect2)).isTrue()
        rect2 = rectangle(0f, 1f, 20f, 20f)
        assertThat(rect1.verticalCollisionDirection(rect2)).isTrue()
    }

    @Test
    fun verticalCollisionDirectionShouldReturnFalseIfAboveCenter() {
        val rect1 = rectangle(0f, 0f, 20f, 20f)
        var rect2 = rectangle(-1f, -42f, 20f, 20f)

        assertThat(rect1.verticalCollisionDirection(rect2)).isFalse()
        rect2 = rectangle(0f, 0f, 20f, 20f)
        assertThat(rect1.verticalCollisionDirection(rect2)).isFalse()
    }

    @Test
    fun intersectsShouldReturnFalseWhenTouchingSameLine() {
        val rect1 = rectangle(0f, 0f, 20f, 20f)
        val rect2 = rectangle(20f, 0f, 20f, 20f)
        val rect3 = rectangle(0f, 20f, 20f, 20f)

        assertThat(rect1.intersects(rect2)).isFalse()
        assertThat(rect1.intersects(rect3)).isFalse()
    }

    @Test
    fun minX_returnsMinimumX() {
        assertThat(rectangle(10f, 20f, 100f, 1000f).minX).isEqualTo(10f)
    }

    @Test
    fun maxX_returnsMaximumX() {
        assertThat(rectangle(10f, 20f, 100f, 1000f).maxX).isEqualTo(110f)
    }

    @Test
    fun minY_returnsMinimumY() {
        assertThat(rectangle(10f, 20f, 100f, 1000f).minY).isEqualTo(20f)
    }

    @Test
    fun maxY_returnsMaximumY() {
        assertThat(rectangle(10f, 20f, 100f, 1000f).maxY).isEqualTo(1020f)
    }

    @Test
    fun centerX_returnsCenterX() {
        assertThat(rectangle(10f, 20f, 100f, 1000f).centerX).isEqualTo(60f)
    }

    @Test
    fun centerY_returnsCenterY() {
        assertThat(rectangle(10f, 20f, 100f, 1000f).centerY).isEqualTo(520f)
    }

    @Test
    fun center_returnsCenterCoordinates() {
        val rectangle = rectangle(10f, 10f, 100f, 100f)

        with(rectangle.center) {
            assertThat(x).isEqualTo(rectangle.centerX)
            assertThat(y).isEqualTo(rectangle.centerY)
        }
    }

    @Test
    fun center_rectangleIsMutated_returnsCenterCoordinates() {
        val rectangle = mutableRectangle(10f, 10f, 100f, 100f)
        rectangle.x = 100f

        with(rectangle.center) {
            assertThat(x).isEqualTo(rectangle.centerX)
            assertThat(y).isEqualTo(rectangle.centerY)
        }
    }
}