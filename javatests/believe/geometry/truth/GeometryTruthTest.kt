package believe.geometry.truth

import believe.geometry.Rectangle
import believe.geometry.truth.RectangleSubject.Companion.rectangles
import believe.geometry.truth.Truth.assertThat
import com.google.common.truth.ExpectFailure
import com.google.common.truth.ExpectFailure.SimpleSubjectBuilderCallback
import com.google.common.truth.SimpleSubjectBuilder
import org.junit.jupiter.api.Test
import java.lang.AssertionError

class GeometryTruthTest {
    @Test
    fun within_fieldsAreWithinTolerance_passes() {
        assertThat(Rectangle(1f, 2f, 3f, 4f)).isWithin(1f).of(Rectangle(2f, 3f, 2f, 4f))
    }

    @Test
    fun within_fieldIsNotWithinTolerance_fails() {
        val rectangle = Rectangle(1f, 2f, 3f, 4f)

        val xAssertionError: AssertionError = expectFailure { whenTesting ->
            whenTesting.that(rectangle).isWithin(0.1f).of(Rectangle(1.5f, 2f, 3f, 4f))
        }
        val yAssertionError: AssertionError = expectFailure { whenTesting ->
            whenTesting.that(rectangle).isWithin(0.1f).of(Rectangle(1f, 1.7f, 3f, 4f))
        }
        val widthAssertionError: AssertionError = expectFailure { whenTesting ->
            whenTesting.that(rectangle).isWithin(0.1f).of(Rectangle(1f, 2f, 3.2f, 4f))
        }
        val heightAssertionError: AssertionError = expectFailure { whenTesting ->
            whenTesting.that(rectangle).isWithin(0.1f).of(Rectangle(1f, 2f, 3f, 4.101f))
        }

        ExpectFailure.assertThat(xAssertionError).factValue("expected").isEqualTo("1.5")
        ExpectFailure.assertThat(xAssertionError).factValue("but was").isEqualTo("1.0")
        ExpectFailure.assertThat(xAssertionError).factValue("outside tolerance").isEqualTo("0.1")

        ExpectFailure.assertThat(yAssertionError).factValue("expected").isEqualTo("1.7")
        ExpectFailure.assertThat(yAssertionError).factValue("but was").isEqualTo("2.0")
        ExpectFailure.assertThat(yAssertionError).factValue("outside tolerance").isEqualTo("0.1")

        ExpectFailure.assertThat(widthAssertionError).factValue("expected").isEqualTo("3.2")
        ExpectFailure.assertThat(widthAssertionError).factValue("but was").isEqualTo("3.0")
        ExpectFailure.assertThat(widthAssertionError).factValue("outside tolerance")
            .isEqualTo("0.1")

        ExpectFailure.assertThat(heightAssertionError).factValue("expected").isEqualTo("4.101")
        ExpectFailure.assertThat(heightAssertionError).factValue("but was").isEqualTo("4.0")
        ExpectFailure.assertThat(heightAssertionError).factValue("outside tolerance")
            .isEqualTo("0.1")
    }

    private fun expectFailure(
        callback: (SimpleSubjectBuilder<RectangleSubject, Rectangle>) -> Unit
    ) = ExpectFailure.expectFailureAbout(rectangles(),
        SimpleSubjectBuilderCallback<RectangleSubject, Rectangle> { callback(it) })
}