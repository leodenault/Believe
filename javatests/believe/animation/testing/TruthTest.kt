package believe.animation.testing

import believe.animation.Animation
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.core.Updatable
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

        val assertionError = expectFailure { whenTesting ->
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

    private class FakeUpdatable(
        val provideAnimation: () -> Animation, private val animationUpdated: () -> Unit = {}
    ) : Updatable {

        val animation: Animation
            get() = provideAnimation()

        override fun update(delta: Long) {
            animation.update(delta)
            animationUpdated()
        }
    }

    private fun expectFailure(
        callback: (SimpleSubjectBuilder<UpdatableAnimationWrapperSubject, Updatable>) -> Unit
    ): AssertionError {
        return ExpectFailure.expectFailureAbout(
            UpdatableAnimationWrapperSubject.updatableAnimationWrappers(),
            SimpleSubjectBuilderCallback<UpdatableAnimationWrapperSubject, Updatable>(callback)
        )
    }
}
