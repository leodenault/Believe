package believe.geometry.truth

import believe.geometry.Rectangle
import com.google.common.truth.FailureMetadata
import com.google.common.truth.StandardSubjectBuilder
import com.google.common.truth.Subject
import com.google.common.truth.Subject.Factory
import com.google.common.truth.Truth

object Truth {
    @JvmStatic
    fun assertThat(actual: Rectangle): RectangleSubject =
        Truth.assertAbout(RectangleSubject.rectangles()).that(actual)
}

class RectangleSubject private constructor(
    failureMetadata: FailureMetadata, private val actual: Rectangle
) : Subject<RectangleSubject, Rectangle>(failureMetadata, actual) {

    fun isWithin(tolerance: Float) = ExpectRectangleComparator(tolerance, actual, check())

    class ExpectRectangleComparator internal constructor(
        private val tolerance: Float,
        private val actual: Rectangle,
        private val check: StandardSubjectBuilder
    ) {
        fun of(expected: Rectangle) {
            check.withMessage("expected rectangle X position to be within tolerance")
                .that(actual.x).isWithin(tolerance).of(expected.x)
            check.withMessage("expected rectangle Y position to be within tolerance")
                .that(actual.y).isWithin(tolerance).of(expected.y)
            check.withMessage("expected rectangle width to be within tolerance")
                .that(actual.width).isWithin(tolerance).of(expected.width)
            check.withMessage("expected rectangle height to be within tolerance")
                .that(actual.height).isWithin(tolerance).of(expected.height)
        }
    }

    companion object {
        @JvmStatic
        fun rectangles(): Factory<RectangleSubject, Rectangle> {
            return Factory<RectangleSubject, Rectangle> { failureMetadata, actual ->
                RectangleSubject(
                    failureMetadata, actual
                )
            }
        }
    }
}
