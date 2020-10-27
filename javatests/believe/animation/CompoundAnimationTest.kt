package believe.animation

import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.*
import believe.animation.testing.assertThat
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IndexOutOfBoundsException

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
        val animation1FirstFrameListener = mock<() -> Unit>()
        val animation1LastFrameListener = mock<() -> Unit>()
        val animation2FirstFrameListener = mock<() -> Unit>()
        val animation2LastFrameListener = mock<() -> Unit>()
        val animation3FirstFrameListener = mock<() -> Unit>()
        val animation3LastFrameListener = mock<() -> Unit>()
        compoundAnimation.addFrameListener(animation1, 0, animation1FirstFrameListener)
        compoundAnimation.addFrameListener(
            animation1, animation1.numFrames - 1, animation1LastFrameListener
        )
        compoundAnimation.addFrameListener(animation2, 0, animation2FirstFrameListener)
        compoundAnimation.addFrameListener(
            animation2, animation2.numFrames - 1, animation2LastFrameListener
        )
        compoundAnimation.addFrameListener(animation3, 0, animation3FirstFrameListener)
        compoundAnimation.addFrameListener(
            animation3, animation3.numFrames - 1, animation3LastFrameListener
        )

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        // o--^
        compoundAnimation.update(frames[animation1StartIndex].duration.toLong())
        verify(animation1FirstFrameListener, never()).invoke()

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //    o-----------------^
        compoundAnimation.update(
            frames.slice(animation1StartIndex + 1 until animation1EndIndex).durations().sum()
                .toLong()
        )
        verify(animation1FirstFrameListener, times(1)).invoke()
        verify(animation1LastFrameListener, times(2)).invoke()

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //                      o-----^
        compoundAnimation.update(
            frames.slice(animation1EndIndex until animation2StartIndex).durations().sum().toLong()
        )
        verify(animation2FirstFrameListener, times(1)).invoke()

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //                            o-----------------^
        compoundAnimation.update(
            frames.slice(animation2StartIndex until animation2EndIndex).durations().sum().toLong()
        )
        verify(animation2FirstFrameListener, times(4)).invoke()
        verify(animation2LastFrameListener, times(3)).invoke()

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //                                              o-----^
        compoundAnimation.update(
            frames.slice(animation2EndIndex until animation3StartIndex).durations().sum().toLong()
        )
        verify(animation3FirstFrameListener, times(1)).invoke()

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4  5
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19 20
        //                                                    o--------------^
        compoundAnimation.update(
            frames.slice(animation3StartIndex until animation3EndIndex).durations().sum().toLong()
        )
        verify(animation3LastFrameListener, times(1)).invoke()

        // 0  1  2  3  0  1  2  3  |  0  1  0  1  0  1  0  |  0  1  2  3  4
        // 0  1  2  3  4  5  6  7     8  9  10 11 12 13 14    15 16 17 18 19
        // ^                                                              o---
        // |_________________________________________________________________|
        compoundAnimation.update(frames[animation3EndIndex].duration.toLong())
        verify(animation1FirstFrameListener, times(2)).invoke()
    }

    @Test
    fun addFrameListener_withSubAnimation_indexOutOfBoundsForAnimation_throwsIndexOutOfBoundsException() {
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
}
