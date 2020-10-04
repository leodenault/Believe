package believe.animation.testing

import believe.animation.Animation
import believe.animation.animation
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.gui.testing.FakeImage
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AnimationFakesTest {
    @Test
    fun fakeAnimation_correctlyGeneratesAnimation() {
        val animation = fakeAnimation(IterationMode.LINEAR, false, 12, 12, 12, 12, 12)

        with(animation.asSlickAnimation()) {
            assertThat(frameCount).isEqualTo(5)
            assertThat(durations).asList().containsExactly(12, 12, 12, 12, 12)
        }
    }

    @Test
    fun fakeAnimation_updatesCorrectly() {
        val animation = fakeAnimation(IterationMode.LINEAR, false, 10, 10, 10, 10, 10)

        with(animation.asSlickAnimation()) {
            assertThat(currentFrame).isEqualTo(getImage(0))
            animation.update(41)
            assertThat(currentFrame).isEqualTo(getImage(4))
        }
    }

    @Test
    fun frameAt_returnsCorrectFrame() {
        val animation = fakeAnimation(IterationMode.LINEAR, false, 2, 3, 4)
        val slickAnimation = animation.asSlickAnimation()

        assertThat(animation.frameAt(0)).isEqualTo(
            FrameData(
                0, slickAnimation.getImage(0), slickAnimation.getDuration(0)
            )
        )
        assertThat(animation.frameAt(1)).isEqualTo(
            FrameData(
                1, slickAnimation.getImage(1), slickAnimation.getDuration(1)
            )
        )
        assertThat(animation.frameAt(2)).isEqualTo(
            FrameData(
                2, slickAnimation.getImage(2), slickAnimation.getDuration(2)
            )
        )
    }

    @Test
    fun frameAt_indexOutOfBounds_throwsIndexOutOfBoundsException() {
        val animation = fakeAnimation(IterationMode.LINEAR, false, 2, 3, 4)

        assertThrows<IndexOutOfBoundsException> { animation.frameAt(-1) }
        assertThrows<IndexOutOfBoundsException> { animation.frameAt(3) }
    }

    @Test
    fun currentFrameData_returnsCorrectData() {
        val animation: Animation = fakeAnimation(IterationMode.LINEAR, true, 1, 2, 3, 4)
        val frames = animation.frames(1)

        assertThat(animation.currentFrameData).isEqualTo(frames[0])
        animation.update(1)
        assertThat(animation.currentFrameData).isEqualTo(frames[1])
        animation.update(2)
        assertThat(animation.currentFrameData).isEqualTo(frames[2])
        animation.update(3)
        assertThat(animation.currentFrameData).isEqualTo(frames[3])
        animation.update(4)
        assertThat(animation.currentFrameData).isEqualTo(frames[0])
    }

    @Test
    fun frames_linear_noLoop_correctlyGeneratesFrameData() {
        val emptyAnimation = fakeAnimation(IterationMode.LINEAR, false)
        val singleFrameAnimation = fakeAnimation(IterationMode.LINEAR, false, 2)
        val longAnimation = fakeAnimation(IterationMode.LINEAR, false, 2, 4, 3)

        assertThat(emptyAnimation.frames(iterations = 5)).isEmpty()
        assertThat(singleFrameAnimation.frames(iterations = 5)).containsExactly(
            singleFrameAnimation.frameAt(0)
        )
        with(longAnimation) {
            assertThat(frames(iterations = 0)).isEmpty()
            assertThat(frames(iterations = 1)).containsExactly(
                frameAt(0), frameAt(1), frameAt(2)
            ).inOrder()
            assertThat(longAnimation.frames(iterations = 2)).containsExactly(
                frameAt(0), frameAt(1), frameAt(2)
            ).inOrder()
        }
    }

    @Test
    fun frames_pingPong_noLoop_correctlyGeneratesFrameData() {
        val emptyAnimation = fakeAnimation(IterationMode.PING_PONG, false)
        val singleFrameAnimation = fakeAnimation(IterationMode.PING_PONG, false, 2)
        val longAnimation = fakeAnimation(IterationMode.PING_PONG, false, 2, 4, 3)

        assertThat(emptyAnimation.frames(iterations = 5)).isEmpty()
        assertThat(singleFrameAnimation.frames(iterations = 5)).containsExactly(
            singleFrameAnimation.frameAt(0)
        )
        with(longAnimation) {
            assertThat(frames(iterations = 0)).isEmpty()
            assertThat(frames(iterations = 1)).containsExactly(
                frameAt(0), frameAt(1), frameAt(2), frameAt(1), frameAt(0)
            ).inOrder()
            assertThat(frames(iterations = 2)).containsExactly(
                frameAt(0), frameAt(1), frameAt(2), frameAt(1), frameAt(0)
            ).inOrder()
        }
    }

    @Test
    fun frames_linear_loop_correctlyGeneratesFrameData() {
        val emptyAnimation = fakeAnimation(IterationMode.LINEAR, true)
        val singleFrameAnimation = fakeAnimation(IterationMode.LINEAR, true, 2)
        val longAnimation = fakeAnimation(IterationMode.LINEAR, true, 2, 4, 3)

        assertThat(emptyAnimation.frames(iterations = 5)).isEmpty()
        assertThat(
            singleFrameAnimation.frames(iterations = 5)
        ).containsExactlyElementsIn((0 until 5).map {
            singleFrameAnimation.frameAt(0)
        })
        with(longAnimation) {
            assertThat(frames(iterations = 0)).isEmpty()
            assertThat(frames(iterations = 1)).containsExactly(
                frameAt(0), frameAt(1), frameAt(2)
            ).inOrder()
            assertThat(frames(iterations = 2)).containsExactlyElementsIn(
                frames(iterations = 1) + frames(iterations = 1)
            ).inOrder()
        }
    }

    @Test
    fun frames_pingPong_loop_correctlyGeneratesFrameData() {
        val emptyAnimation = fakeAnimation(IterationMode.PING_PONG, true)
        val singleFrameAnimation = fakeAnimation(IterationMode.PING_PONG, true, 2)
        val longAnimation = fakeAnimation(IterationMode.PING_PONG, true, 2, 4, 3)

        assertThat(emptyAnimation.frames(iterations = 5)).isEmpty()
        assertThat(
            singleFrameAnimation.frames(iterations = 5)
        ).containsExactlyElementsIn((0 until 5).map {
            singleFrameAnimation.frameAt(0)
        })
        with(longAnimation) {
            assertThat(frames(iterations = 0)).isEmpty()
            assertThat(frames(iterations = 1)).containsExactly(
                frameAt(0), frameAt(1), frameAt(2), frameAt(1), frameAt(0)
            ).inOrder()
            assertThat(frames(iterations = 2)).containsExactly(
                frameAt(0),
                frameAt(1),
                frameAt(2),
                frameAt(1),
                frameAt(0),
                frameAt(1),
                frameAt(2),
                frameAt(1),
                frameAt(0)
            ).inOrder()
            assertThat(frames(iterations = 3)).containsExactly(
                frameAt(0),
                frameAt(1),
                frameAt(2),
                frameAt(1),
                frameAt(0),
                frameAt(1),
                frameAt(2),
                frameAt(1),
                frameAt(0),
                frameAt(1),
                frameAt(2),
                frameAt(1),
                frameAt(0)
            ).inOrder()
        }
    }

    @Test
    fun durations_returnsListWithCorrectDurations() {
        assertThat(
            listOf(
                FrameData(0, FakeImage(), 20),
                FrameData(1, FakeImage(), 30),
                FrameData(2, FakeImage(), 10),
                FrameData(3, FakeImage(), 40),
                FrameData(4, FakeImage(), 50)
            ).durations()
        ).containsExactly(20, 30, 10, 40, 50).inOrder()
    }

    @Test
    fun images_returnsListWithCorrectImages() {
        val image1 = FakeImage()
        val image2 = FakeImage()
        val image3 = FakeImage()

        assertThat(
            listOf(
                FrameData(0, image1, 20), FrameData(1, image2, 20), FrameData(2, image3, 20)
            ).images()
        ).containsExactly(image1, image2, image3).inOrder()
    }
}
