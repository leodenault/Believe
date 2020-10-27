package believe.animation.testing

import believe.animation.Animation
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.core.Updatable
import believe.core.display.Graphics
import believe.core.display.RenderableV2
import believe.gui.testing.FakeImage
import com.google.common.truth.ExpectFailure
import com.google.common.truth.ExpectFailure.SimpleSubjectBuilderCallback
import com.google.common.truth.ExpectFailure.assertThat
import com.google.common.truth.SimpleSubjectBuilder
import org.junit.jupiter.api.Test

internal class TruthTest {
    @Test
    fun generatesFrames_framesMatch_passes() {
        val animation1 = fakeAnimation(IterationMode.LINEAR, true, 5, 34, 12)
        val animation2 = fakeAnimation(IterationMode.LINEAR, true, 10, 23, 44)
        var invocationCount = 0
        val updatable = FakeUpdatable({ if (invocationCount < 3) animation1 else animation2 },
            { invocationCount++ })

        assertThat(updatable).updating {
            updatable.animation
        }.generatesFrames(animation1.frames() + animation2.frames())
    }

    @Test
    fun generatesFrames_framesDoNotMatch_fails() {
        val animation = fakeAnimation(IterationMode.LINEAR, true, 5, 34, 12)
        val updatable = FakeUpdatable({ animation })
        val actualFrames = animation.frames().slice(0..1)
        val expectedFrames = listOf(
            FrameData(index = 0, duration = 12, image = FakeImage()),
            FrameData(index = 1, duration = 20, image = FakeImage())
        )

        val assertionError = expectUpdatableFailure { whenTesting ->
            whenTesting.that(updatable).updating {
                updatable.animation
            }.generatesFrames(expectedFrames)
        }

        assertThat(assertionError).factValue("generated frames").contains(
            """
            ${actualFrames[0]}
            ${actualFrames[1]}
        """.trimIndent()
        )
        assertThat(
            assertionError
        ).factValue("expected frames").contains(
            """
            ${expectedFrames[0]}
            ${expectedFrames[1]}
        """.trimIndent()
        )
    }

    @Test
    fun generatesDurations_durationsMatch_passes() {
        val animation1 = fakeAnimation(IterationMode.LINEAR, true, 5, 34, 12)
        val animation2 = fakeAnimation(IterationMode.LINEAR, true, 10, 23, 44)
        var invocationCount = 0
        val updatable = FakeUpdatable({ if (invocationCount < 3) animation1 else animation2 },
            { invocationCount++ })

        assertThat(updatable).updating {
            updatable.animation
        }.generatesDurations(animation1.frames().durations() + animation2.frames().durations())
    }

    @Test
    fun generatesDurations_durationsDoNotMatch_fails() {
        val animation = fakeAnimation(IterationMode.LINEAR, true, 5, 34, 12)
        val updatable = FakeUpdatable({ animation })
        val actualDurations = animation.frames().slice(0..1).durations()
        val expectedDurations = listOf(12, 20)

        val assertionError = expectUpdatableFailure { whenTesting ->
            whenTesting.that(updatable).updating {
                updatable.animation
            }.generatesDurations(expectedDurations)
        }

        assertThat(assertionError).factValue("generated durations").contains(
            """
            ${actualDurations[0]}
            ${actualDurations[1]}
        """.trimIndent()
        )
        assertThat(
            assertionError
        ).factValue("expected durations").contains(
            """
            ${expectedDurations[0]}
            ${expectedDurations[1]}
        """.trimIndent()
        )
    }

    @Test
    fun rendersFrames_framesMatch_passes() {
        val animation1 = fakeAnimation(IterationMode.LINEAR, true, 5, 34, 12)
        val animation2 = fakeAnimation(IterationMode.LINEAR, true, 10, 23, 44)
        var invocationCount = 0
        val updatable = FakeUpdatable({ if (invocationCount < 3) animation1 else animation2 },
            { invocationCount++ })

        assertThat(updatable).rendersFrames(animation1.frames() + animation2.frames())
    }

    @Test
    fun rendersFrames_durationsDoNotMatch_fails() {
        val animation = fakeAnimation(IterationMode.LINEAR, true, 5, 34, 12)
        val updatable = FakeUpdatable({ animation })
        val actualImages = animation.frames().slice(0..1).images()
        val expectedFrames = listOf(
            FrameData(index = 0, duration = 12, image = FakeImage()),
            FrameData(index = 1, duration = 20, image = FakeImage())
        )
        val expectedImages = expectedFrames.images()

        val assertionError = expectRenderableFailure<FakeUpdatable> { whenTesting ->
            whenTesting.that(updatable).rendersFrames(expectedFrames)
        }

        assertThat(assertionError).factValue("rendered images").contains(
            """
            ${actualImages[0]}
            ${actualImages[1]}
        """.trimIndent()
        )
        assertThat(
            assertionError
        ).factValue("expected images").contains(
            """
            ${expectedImages[0]}
            ${expectedImages[1]}
        """.trimIndent()
        )
    }

    private class FakeUpdatable(
        val provideAnimation: () -> Animation, private val animationUpdated: () -> Unit = {}
    ) : Updatable, RenderableV2 {

        val animation: Animation
            get() = provideAnimation()

        override fun update(delta: Long) {
            animation.update(delta)
            animationUpdated()
        }

        override fun render(g: Graphics) {
            g.drawAnimation(animation, 0f, 0f)
        }
    }

    private fun expectUpdatableFailure(
        callback: (SimpleSubjectBuilder<AnimationWrapperSubject, Updatable>) -> Unit
    ): AssertionError {
        return ExpectFailure.expectFailureAbout(
            AnimationWrapperSubject.animationWrappers(),
            SimpleSubjectBuilderCallback<AnimationWrapperSubject, Updatable>(callback)
        )
    }

    private fun <R> expectRenderableFailure(
        callback: (SimpleSubjectBuilder<RenderingAnimationWrapperSubject<R>, R>) -> Unit
    ): AssertionError where R : Updatable, R : RenderableV2 {
        return ExpectFailure.expectFailureAbout(
            RenderingAnimationWrapperSubject.renderingAnimationWrappers<R>(),
            SimpleSubjectBuilderCallback<RenderingAnimationWrapperSubject<R>, R>(callback)
        )
    }
}
