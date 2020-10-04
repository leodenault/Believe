package believe.animation

import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.*
import believe.gui.testing.FakeImage
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class AnimationTest {
    @Test
    fun animation_correctlyConstructsAnimation() {
        val spriteSheet = FakeSpriteSheet(numRows = 4, numColumns = 3)

        val animation = animation(
            spriteSheet,
            frameRange = 2..9,
            frameDuration = 50,
            iterationMode = IterationMode.LINEAR,
            isLooping = false
        )

        val frames = animation.frames(iterations = 1)
        assertThat(frames.durations()).containsExactlyElementsIn((0 until 8).map { 50 })
        assertThat(animation.width).isEqualTo(spriteSheet.imageAt(0).width)
        assertThat(animation.height).isEqualTo(spriteSheet.imageAt(0).height)
        assertThat(
            frames.images()
        ).containsExactlyElementsIn(spriteSheet.imagesBetween(2..9)).inOrder()
    }

    @Test
    fun update_displaysFramesOnTime() {
        val animation = animation(
            frames = List(3) { FakeImage() },
            frameDurations = listOf(4, 11, 56),
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )

        val frameData = animation.frames(iterations = 5)
        val frames = frameData.durations().map {
            val frame = animation.currentFrameData
            animation.update(it.toLong())
            frame
        }

        assertThat(frames).containsExactlyElementsIn(frameData).inOrder()
    }

    @Test
    fun addAnimationEndedListener_linear_doesNotLoop_callsAtEnd() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..4,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = false
        )
        var calls = 0
        val animationTime = animation.frames().durations().sum().toLong()

        animation.addAnimationEndedListener { calls++ }
        animation.update(animationTime)
        animation.restart()
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(1)
        animation.update(1)

        assertThat(calls).isEqualTo(2)
    }

    @Test
    fun addAnimationEndedListener_pingPong_doesNotLoop_callsAtEnd() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..4,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = false
        )
        var calls = 0
        val animationTime = animation.frames().durations().sum().toLong()

        animation.addAnimationEndedListener { calls++ }
        animation.update(animationTime)
        animation.restart()
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(1)
        animation.update(1)

        assertThat(calls).isEqualTo(2)
    }

    @Test
    fun addAnimationEndedListener_linear_isLooping_callsAtEndOfLoop() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..4,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )
        val animationTime = animation.frames(iterations = 10).durations().sum().toLong()
        var calls = 0

        animation.addAnimationEndedListener { calls++ }
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(9)
        animation.update(1)
        assertThat(calls).isEqualTo(10)
    }

    @Test
    fun addAnimationEndedListener_linear_isLooping_emptyAnimation_neverCalls() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = IntRange.EMPTY,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )
        var executed = false

        animation.addAnimationEndedListener { executed = true }
        animation.update(1000L)

        assertThat(executed).isFalse()
    }

    @Test
    fun addAnimationEndedListener_linear_isLooping_singleFrameAnimation_callsAtEndOfLoop() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..2,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )
        var calls = 0
        val animationTime = animation.frames(iterations = 10).durations().sum().toLong()

        animation.addAnimationEndedListener { calls++ }
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(9)
        animation.update(1)
        assertThat(calls).isEqualTo(10)
    }

    @Test
    fun addAnimationEndedListener_pingPongs_callsAtEnd() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..4,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = true
        )
        val animationTime = animation.frames(iterations = 10).durations().sum().toLong()
        var calls = 0

        animation.addAnimationEndedListener { calls++ }
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(9)
        animation.update(1)
        assertThat(calls).isEqualTo(10)
    }

    @Test
    fun addAnimationEndedListener_pingPong_isLooping_emptyAnimation_neverCalls() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = IntRange.EMPTY,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = true
        )
        var executed = false

        animation.addAnimationEndedListener { executed = true }
        animation.update(1000L)

        assertThat(executed).isFalse()
    }

    @Test
    fun addAnimationEndedListener_pingPong_isLooping_singleFrameAnimation_callsAtEndOfLoop() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..2,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = true
        )
        var calls = 0
        val animationTime = animation.frames(iterations = 10).durations().sum().toLong()

        animation.addAnimationEndedListener { calls++ }
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(9)
        animation.update(1)
        assertThat(calls).isEqualTo(10)
    }

    @Test
    fun restart_linear_isNotLooping_resetsAnimationToFirstFrame() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = false
        )

        animation.update(animation.frames().durations().sum().toLong() - 30)
        animation.restart()

        assertThat(animation.currentFrameData).isEqualTo(animation.frameAt(0))
        animation.update(animation.frameAt(0).duration.toLong())
        assertThat(animation.currentFrameData).isEqualTo(animation.frameAt(1))

    }

    @Test
    fun restart_linear_isNotLooping_resetsListeners() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = false
        )
        val animationTime = animation.frames().durations().sum().toLong()
        var calls = 0
        animation.addAnimationEndedListener { calls++ }

        animation.restart()
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(0)
        animation.update(1)
        assertThat(calls).isEqualTo(1)
    }

    @Test
    fun restart_linear_isLooping_resetsAnimationToFirstFrame() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )

        animation.update(animation.frames().durations().sum().toLong() - 30)
        animation.restart()

        assertThat(animation.currentFrameData).isEqualTo(animation.frameAt(0))
        animation.update(animation.frameAt(0).duration.toLong())
        assertThat(animation.currentFrameData).isEqualTo(animation.frameAt(1))
    }

    @Test
    fun restart_linear_isLooping_resetsListeners() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )
        val animationTime = animation.frames().durations().sum().toLong()
        var calls = 0
        animation.addAnimationEndedListener { calls++ }

        animation.restart()
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(0)
        animation.update(1)
        assertThat(calls).isEqualTo(1)
    }

    @Test
    fun restart_pingPong_isNotLooping_resetsAnimationToFirstFrame() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = false
        )

        animation.update(animation.frames().durations().sum().toLong() - 30)
        animation.restart()

        assertThat(animation.currentFrameData).isEqualTo(animation.frameAt(0))
        animation.update(animation.frameAt(0).duration.toLong())
        assertThat(animation.currentFrameData).isEqualTo(animation.frameAt(1))
    }

    @Test
    fun restart_pingPong_isNotLooping_resetsListeners() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = false
        )
        val animationTime = animation.frames().durations().sum().toLong()
        var calls = 0
        animation.addAnimationEndedListener { calls++ }

        animation.restart()
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(0)
        animation.update(1)
        assertThat(calls).isEqualTo(1)
    }

    @Test
    fun restart_pingPong_isLooping_resetsAnimationToFirstFrame() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = true
        )

        animation.update(animation.frames().durations().sum().toLong() - 30)
        animation.restart()

        assertThat(animation.currentFrameData).isEqualTo(animation.frameAt(0))
        animation.update(animation.frameAt(0).duration.toLong())
        assertThat(animation.currentFrameData).isEqualTo(animation.frameAt(1))
    }

    @Test
    fun restart_pingPong_isLooping_resetsListeners() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = true
        )
        val animationTime = animation.frames().durations().sum().toLong()
        var calls = 0
        animation.addAnimationEndedListener { calls++ }

        animation.restart()
        animation.update(animationTime - 1)
        assertThat(calls).isEqualTo(0)
        animation.update(1)
        assertThat(calls).isEqualTo(1)
    }

    companion object {
        private val SPRITE_SHEET = FakeSpriteSheet(numRows = 3, numColumns = 3)
    }
}
