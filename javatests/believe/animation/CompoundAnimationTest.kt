package believe.animation

import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.FakeSpriteSheet
import believe.animation.testing.assertThat
import believe.animation.testing.durations
import believe.animation.testing.frames
import believe.animation.testing.images
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class CompoundAnimationTest {
    private val animation1 = animation(
        SPRITE_SHEET,
        frameRange = FRAME_RANGE_1,
        frameDuration = FRAME_DURATION_1,
        iterationMode = IterationMode.LINEAR,
        isLooping = false
    )
    private val animation2 = animation(
        SPRITE_SHEET,
        frameRange = FRAME_RANGE_2,
        frameDuration = FRAME_DURATION_2,
        iterationMode = IterationMode.PING_PONG,
        isLooping = false
    )
    private val animation3 = animation(
        SPRITE_SHEET,
        frameRange = FRAME_RANGE_3,
        frameDuration = FRAME_DURATION_3,
        iterationMode = IterationMode.LINEAR,
        isLooping = true
    )
    private val animation1ForTesting = animation(
        SPRITE_SHEET,
        frameRange = FRAME_RANGE_1,
        frameDuration = FRAME_DURATION_1,
        iterationMode = IterationMode.LINEAR,
        isLooping = true
    )
    private val animation2ForTesting = animation(
        SPRITE_SHEET,
        frameRange = FRAME_RANGE_2,
        frameDuration = FRAME_DURATION_2,
        iterationMode = IterationMode.PING_PONG,
        isLooping = true
    )
    private val compoundAnimation = compoundAnimation(
        true,
        NUM_ITERATIONS_1 to animation1,
        NUM_ITERATIONS_2 to animation2,
        NUM_ITERATIONS_3 to animation3
    )

    @Test
    fun compoundAnimation_correctlyConstructsAnimation() {
        val actualFrames = compoundAnimation.frames(iterations = 1)
        val expectedFrames =
            animation1ForTesting.frames(NUM_ITERATIONS_1) + animation2ForTesting.frames(
                NUM_ITERATIONS_2
            ) + animation3.frames(NUM_ITERATIONS_3)
        with(compoundAnimation) {
            assertThat(width).isEqualTo(SPRITE_SHEET.imageAt(0).width)
            assertThat(height).isEqualTo(SPRITE_SHEET.imageAt(0).height)
            assertThat(numFrames).isEqualTo(actualFrames.size)
            assertThat(iterationMode).isEqualTo(IterationMode.LINEAR)
            assertThat(isLooping).isTrue()
        }
        assertThat(actualFrames.durations()).containsExactlyElementsIn(expectedFrames.durations())
            .inOrder()
        assertThat(actualFrames.images()).containsExactlyElementsIn(expectedFrames.images())
            .inOrder()
    }

    @Test
    fun update_displaysFramesOnTime() {
        assertThat(compoundAnimation).updating { compoundAnimation }
            .generatesFrames(compoundAnimation.frames())
    }

    @Test
    fun addFrameListener_withSubAnimation_correctlyAddsListener() {
        val frames = compoundAnimation.frames()
        val animation1StartIndex = 0
        val animation1EndIndex = animation1ForTesting.frames(NUM_ITERATIONS_1).size - 1
        val animation2StartIndex = animation1EndIndex + 1
        val animation2EndIndex =
            animation2StartIndex + animation2ForTesting.frames(NUM_ITERATIONS_2).size - 1
        val animation3StartIndex = animation2EndIndex + 1
        val animation3EndIndex = compoundAnimation.frames().count() - 1
        val animation1FirstFrameListener = FakeFrameListener()
        val animation1LastFrameListener = FakeFrameListener()
        val animation2FirstFrameListener = FakeFrameListener()
        val animation2LastFrameListener = FakeFrameListener()
        val animation3FirstFrameListener = FakeFrameListener()
        val animation3LastFrameListener = FakeFrameListener()
        compoundAnimation.addFrameListener(
            animation1,
            0,
            animation1FirstFrameListener.configureListeners()
        )
        compoundAnimation.addFrameListener(
            animation1, animation1.numFrames - 1, animation1LastFrameListener.configureListeners()
        )
        compoundAnimation.addFrameListener(
            animation2,
            0,
            animation2FirstFrameListener.configureListeners()
        )
        compoundAnimation.addFrameListener(
            animation2, animation2.numFrames - 1, animation2LastFrameListener.configureListeners()
        )
        compoundAnimation.addFrameListener(
            animation3,
            0,
            animation3FirstFrameListener.configureListeners()
        )
        compoundAnimation.addFrameListener(
            animation3, animation3.numFrames - 1, animation3LastFrameListener.configureListeners()
        )

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        // o-^
        compoundAnimation.update(frames[animation1StartIndex].duration.toLong())
        assertThat(animation1FirstFrameListener.enters).isEqualTo(1)
        assertThat(animation1FirstFrameListener.leaves).isEqualTo(1)

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //    o----------------^
        compoundAnimation.update(
            frames.slice(animation1StartIndex + 1 until animation1EndIndex).durations().sum()
                .toLong()
        )
        assertThat(animation1FirstFrameListener.enters).isEqualTo(2)
        assertThat(animation1FirstFrameListener.leaves).isEqualTo(2)
        assertThat(animation1LastFrameListener.enters).isEqualTo(2)
        assertThat(animation1LastFrameListener.leaves).isEqualTo(1)

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //                      o----^
        compoundAnimation.update(
            frames.slice(animation1EndIndex until animation2StartIndex).durations().sum().toLong()
        )
        assertThat(animation1LastFrameListener.leaves).isEqualTo(2)
        assertThat(animation2FirstFrameListener.enters).isEqualTo(1)
        assertThat(animation2FirstFrameListener.leaves).isEqualTo(0)

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //                            o----------------^
        compoundAnimation.update(
            frames.slice(animation2StartIndex until animation2EndIndex).durations().sum().toLong()
        )
        assertThat(animation2FirstFrameListener.enters).isEqualTo(4)
        assertThat(animation2FirstFrameListener.leaves).isEqualTo(3)
        assertThat(animation2LastFrameListener.enters).isEqualTo(3)
        assertThat(animation2LastFrameListener.leaves).isEqualTo(3)

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //                                              o----^
        compoundAnimation.update(
            frames.slice(animation2EndIndex until animation3StartIndex).durations().sum().toLong()
        )
        assertThat(animation2FirstFrameListener.leaves).isEqualTo(4)
        assertThat(animation3FirstFrameListener.enters).isEqualTo(1)
        assertThat(animation3FirstFrameListener.leaves).isEqualTo(0)

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //                                                    o-------------^
        compoundAnimation.update(
            frames.slice(animation3StartIndex until animation3EndIndex).durations().sum().toLong()
        )
        assertThat(animation3FirstFrameListener.leaves).isEqualTo(1)
        assertThat(animation3LastFrameListener.enters).isEqualTo(1)
        assertThat(animation3LastFrameListener.leaves).isEqualTo(0)

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        // ^                                                                 o---
        // |____________________________________________________________________|
        compoundAnimation.update(frames[animation3EndIndex].duration.toLong())
        assertThat(animation3FirstFrameListener.leaves).isEqualTo(1)
        assertThat(animation1FirstFrameListener.enters).isEqualTo(3)
    }

    @Test
    fun addFrameListener_withSubAnimation_indexOutOfBounds_throwsIndexOutOfBoundsException() {
        assertThrows<IndexOutOfBoundsException> {
            compoundAnimation.addFrameListener(animation1, -1) {}
        }
        assertThrows<IndexOutOfBoundsException> {
            compoundAnimation.addFrameListener(animation1, animation1.numFrames) {}
        }
        assertThrows<IndexOutOfBoundsException> {
            compoundAnimation.addFrameListener(animation2, -1) {}
        }
        assertThrows<IndexOutOfBoundsException> {
            compoundAnimation.addFrameListener(animation2, animation3.numFrames) {}
        }
        assertThrows<IndexOutOfBoundsException> {
            compoundAnimation.addFrameListener(animation3, -1) {}
        }
        assertThrows<IndexOutOfBoundsException> {
            compoundAnimation.addFrameListener(animation3, animation3.numFrames) {}
        }
    }

    companion object {
        private val SPRITE_SHEET = FakeSpriteSheet(numRows = 3, numColumns = 3)
        private val FRAME_RANGE_1 = 2..5
        private val FRAME_RANGE_2 = 7..8
        private val FRAME_RANGE_3 = 0..4
        private const val FRAME_DURATION_1 = 5
        private const val FRAME_DURATION_2 = 10
        private const val FRAME_DURATION_3 = 15
        private const val NUM_ITERATIONS_1 = 2
        private const val NUM_ITERATIONS_2 = 3
        private const val NUM_ITERATIONS_3 = 1
    }

    private class FakeFrameListener {
        var enters = 0
        var leaves = 0

        fun configureListeners(): FrameListener.Builder.() -> Unit = {
            enterFrame = { enters++ }
            leaveFrame = { leaves++ }
        }
    }
}
