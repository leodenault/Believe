package believe.animation

import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.FakeSpriteSheet
import believe.animation.testing.currentFrameData
import believe.animation.testing.durations
import believe.animation.testing.frameAt
import believe.animation.testing.frames
import believe.animation.testing.images
import believe.gui.testing.FakeImage
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
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
        assertThat(animation.numFrames).isEqualTo(8)
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
    fun addFrameListener_linear_doesNotLoop_callsWhenFrameIsReached() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..4,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = false
        )
        var frame2enters = 0
        var frame2leaves = 0
        var frame4enters = 0
        var frame4leaves = 0
        val animationTime = animation.frames().durations().sum().toLong()

        // Frame: |2   3   4   |
        // Time:   0   10  20  30

        animation.addFrameListener(0) {
            enterFrame = { frame2enters++ }
            leaveFrame = { frame2leaves++ }
        }
        animation.addFrameListener(2) {
            enterFrame = { frame4enters++ }
            leaveFrame = { frame4leaves++ }
        }

        animation.update(1) // 1 out of 30
        assertThat(frame2enters).isEqualTo(1)
        assertThat(frame2leaves).isEqualTo(0)
        assertThat(frame4enters).isEqualTo(0)
        assertThat(frame4leaves).isEqualTo(0)

        animation.update(animationTime - 2) // 29 out of 30
        assertThat(frame2enters).isEqualTo(1)
        assertThat(frame2leaves).isEqualTo(1)
        assertThat(frame4enters).isEqualTo(1)
        assertThat(frame4leaves).isEqualTo(0)

        animation.update(1) // 30 out of 30
        assertThat(frame2enters).isEqualTo(1)
        assertThat(frame2leaves).isEqualTo(1)
        assertThat(frame4enters).isEqualTo(1)
        assertThat(frame4leaves).isEqualTo(0)

        animation.update(animationTime * 10) // 300 out of 30
        assertThat(frame2enters).isEqualTo(1)
        assertThat(frame2leaves).isEqualTo(1)
        assertThat(frame4enters).isEqualTo(1)
        assertThat(frame4leaves).isEqualTo(0)

        animation.restart()
        animation.update(animationTime - 1) // 29 out of 30
        assertThat(frame2enters).isEqualTo(2)
        assertThat(frame2leaves).isEqualTo(2)
        assertThat(frame4enters).isEqualTo(2)
        assertThat(frame4leaves).isEqualTo(0)

        animation.update(1) // 30 out of 30
        assertThat(frame2enters).isEqualTo(2)
        assertThat(frame2leaves).isEqualTo(2)
        assertThat(frame4enters).isEqualTo(2)
        assertThat(frame4leaves).isEqualTo(0)
    }

    @Test
    fun addFrameListener_pingPong_doesNotLoop_callsWhenFrameIsReached() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..4,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = false
        )
        var frame2Enters = 0
        var frame2Leaves = 0
        var frame4Enters = 0
        var frame4Leaves = 0
        val animationTime = animation.frames().durations().sum().toLong()

        // Frame: |2   3   4   3   2   |
        // Time:   0   10  20  30  40  50

        animation.addFrameListener(0) {
            enterFrame = { frame2Enters++ }
            leaveFrame = { frame2Leaves++ }
        }
        animation.addFrameListener(2) {
            enterFrame = { frame4Enters++ }
            leaveFrame = { frame4Leaves++ }
        }

        animation.update(1) // 1 out of 50
        assertThat(frame2Enters).isEqualTo(1)
        assertThat(frame2Leaves).isEqualTo(0)
        assertThat(frame4Enters).isEqualTo(0)
        assertThat(frame4Leaves).isEqualTo(0)

        animation.update(animationTime - 12) // 39 out of 50
        assertThat(frame2Enters).isEqualTo(1)
        assertThat(frame2Leaves).isEqualTo(1)
        assertThat(frame4Enters).isEqualTo(1)
        assertThat(frame4Leaves).isEqualTo(1)

        animation.update(1) // 40 out of 50
        assertThat(frame2Enters).isEqualTo(2)
        assertThat(frame2Leaves).isEqualTo(1)
        assertThat(frame4Enters).isEqualTo(1)
        assertThat(frame4Leaves).isEqualTo(1)

        animation.update(animationTime * 10) // 500 out of 50
        assertThat(frame2Enters).isEqualTo(2)
        assertThat(frame2Leaves).isEqualTo(1)
        assertThat(frame4Enters).isEqualTo(1)
        assertThat(frame4Leaves).isEqualTo(1)

        animation.restart()
        animation.update(animationTime - 11) // 39 out of 50
        assertThat(frame2Enters).isEqualTo(3)
        assertThat(frame2Leaves).isEqualTo(2)
        assertThat(frame4Enters).isEqualTo(2)
        assertThat(frame4Leaves).isEqualTo(2)

        animation.update(1) // 40 out of 50
        assertThat(frame2Enters).isEqualTo(4)
        assertThat(frame2Leaves).isEqualTo(2)
        assertThat(frame4Enters).isEqualTo(2)
        assertThat(frame4Leaves).isEqualTo(2)
    }

    @Test
    fun addFrameListener_linear_isLooping_callsWhenFrameIsReached() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..4,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )
        val animationTime = animation.frames(iterations = 10).durations().sum().toLong()
        var frame2Enters = 0
        var frame2Leaves = 0
        var frame4Enters = 0
        var frame4Leaves = 0

        // Frame: |2   3   4   2   ...   4    2 |
        // Time:   0   10  20  30  ...   290  300

        animation.addFrameListener(0) {
            enterFrame = { frame2Enters++ }
            leaveFrame = { frame2Leaves++ }
        }
        animation.addFrameListener(2) {
            enterFrame = { frame4Enters++ }
            leaveFrame = { frame4Leaves++ }
        }

        animation.update(1) // 1 out of 300
        assertThat(frame2Enters).isEqualTo(1)
        assertThat(frame2Leaves).isEqualTo(0)
        assertThat(frame4Enters).isEqualTo(0)
        assertThat(frame4Leaves).isEqualTo(0)

        animation.update(animationTime - 2) // 299 out of 300
        assertThat(frame2Enters).isEqualTo(10)
        assertThat(frame2Leaves).isEqualTo(10)
        assertThat(frame4Enters).isEqualTo(10)
        assertThat(frame4Leaves).isEqualTo(9)

        animation.update(1) // 300 out of 300
        assertThat(frame2Enters).isEqualTo(11)
        assertThat(frame2Leaves).isEqualTo(10)
        assertThat(frame4Enters).isEqualTo(10)
        assertThat(frame4Leaves).isEqualTo(10)

        animation.restart()
        animation.update(animationTime) // 300 out of 300
        assertThat(frame2Enters).isEqualTo(22)
        assertThat(frame2Leaves).isEqualTo(20)
        assertThat(frame4Enters).isEqualTo(20)
        assertThat(frame4Leaves).isEqualTo(20)
    }

    @Test
    fun addFrameListener_linear_isLooping_emptyAnimation_neverCalls() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = IntRange.EMPTY,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )
        val listener = mock<() -> Unit>()

        animation.addFrameListener(0) {
            enterFrame = listener
            leaveFrame = listener
        }
        animation.update(Long.MAX_VALUE)

        verify(listener, never()).invoke()
    }

    @Test
    fun addFrameListener_linear_isLooping_singleFrameAnimation_callsWhenFrameIsReached() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..2,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )
        var enters = 0
        var leaves = 0
        val animationTime = animation.frames(iterations = 10).durations().sum().toLong()
        animation.addFrameListener(0) {
            enterFrame = { enters++ }
            leaveFrame = { leaves++ }
        }

        // Frame: |2   2   ...   2   2 |
        // Time:   0   10  ...   90  100

        animation.update(1) // 1 out of 100
        assertThat(enters).isEqualTo(1)
        assertThat(leaves).isEqualTo(0)

        animation.update(9) // 10 out of 100
        assertThat(enters).isEqualTo(2)
        assertThat(leaves).isEqualTo(1)

        animation.update(animationTime - 11) // 99 out of 100
        assertThat(enters).isEqualTo(10)
        assertThat(leaves).isEqualTo(9)

        animation.update(1) // 100 out of 100
        assertThat(enters).isEqualTo(11)
        assertThat(leaves).isEqualTo(10)
    }

    @Test
    fun addFrameListener_pingPongs_isLooping_callsWhenFrameIsReached() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..4,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = true
        )
        val animationTime = animation.frames(iterations = 10).durations().sum().toLong()
        var frame2Enters = 0
        var frame2Leaves = 0
        var frame3Enters = 0
        var frame3Leaves = 0
        var frame4Enters = 0
        var frame4Leaves = 0

        // Frame: |2   3   4   3   2   3   4   3   ...   3    2    3 |
        // Time:   0   10  20  30  40  50  60  70  ...   390  400  410
        //
        // Frame | Total executions
        // ------------------------
        //   2   |        11
        //   3   |        20
        //   4   |        10

        animation.addFrameListener(0) {
            enterFrame = { frame2Enters++ }
            leaveFrame = { frame2Leaves++ }
        }
        animation.addFrameListener(1) {
            enterFrame = { frame3Enters++ }
            leaveFrame = { frame3Leaves++ }
        }
        animation.addFrameListener(2) {
            enterFrame = { frame4Enters++ }
            leaveFrame = { frame4Leaves++ }
        }

        animation.update(1) // 1 out of 410
        assertThat(frame2Enters).isEqualTo(1)
        assertThat(frame2Leaves).isEqualTo(0)
        assertThat(frame3Enters).isEqualTo(0)
        assertThat(frame3Leaves).isEqualTo(0)
        assertThat(frame4Enters).isEqualTo(0)
        assertThat(frame4Leaves).isEqualTo(0)

        animation.update(animationTime - 12) // 399 out of 410
        assertThat(frame2Enters).isEqualTo(10)
        assertThat(frame2Leaves).isEqualTo(10)
        assertThat(frame3Enters).isEqualTo(20)
        assertThat(frame3Leaves).isEqualTo(19)
        assertThat(frame4Enters).isEqualTo(10)
        assertThat(frame4Leaves).isEqualTo(10)

        animation.update(1) // 400 out of 410
        assertThat(frame2Enters).isEqualTo(11)
        assertThat(frame2Leaves).isEqualTo(10)
        assertThat(frame3Enters).isEqualTo(20)
        assertThat(frame3Leaves).isEqualTo(20)
        assertThat(frame4Enters).isEqualTo(10)
        assertThat(frame4Leaves).isEqualTo(10)

        animation.update(10) // 410 out of 410
        assertThat(frame2Enters).isEqualTo(11)
        assertThat(frame2Leaves).isEqualTo(11)
        assertThat(frame3Enters).isEqualTo(21)
        assertThat(frame3Leaves).isEqualTo(20)
        assertThat(frame4Enters).isEqualTo(10)
        assertThat(frame4Leaves).isEqualTo(10)

        animation.restart()
        animation.update(animationTime) // 410 out of 410
        assertThat(frame2Enters).isEqualTo(22)
        assertThat(frame2Leaves).isEqualTo(22)
        assertThat(frame3Enters).isEqualTo(42)
        assertThat(frame3Leaves).isEqualTo(40)
        assertThat(frame4Enters).isEqualTo(20)
        assertThat(frame4Leaves).isEqualTo(20)
    }

    @Test
    fun addFrameListener_pingPong_isLooping_emptyAnimation_neverCalls() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = IntRange.EMPTY,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = true
        )
        val listener = mock<() -> Unit>()

        animation.addFrameListener(0) {
            enterFrame = listener
            leaveFrame = listener
        }
        animation.update(Long.MAX_VALUE)

        verify(listener, never()).invoke()
    }

    @Test
    fun addFrameListener_pingPong_isLooping_singleFrameAnimation_callsWhenFrameIsReached() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 2..2,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = true
        )
        var enters = 0
        var leaves = 0
        val animationTime = animation.frames(iterations = 10).durations().sum().toLong()
        animation.addFrameListener(0) {
            enterFrame = { enters++ }
            leaveFrame = { leaves++ }
        }

        // Frame: |2   2   ...   2   2 |
        // Time:   0   10  ...   90  100

        animation.update(animationTime - 1) // 99 out of 100
        assertThat(enters).isEqualTo(10)
        assertThat(leaves).isEqualTo(9)

        animation.update(1) // 100 out of 100
        assertThat(enters).isEqualTo(11)
        assertThat(leaves).isEqualTo(10)
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
    fun restart_linear_isNotLooping_callsListenersAppropriately() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = false
        )
        val animationTime = animation.frames().durations().sum().toLong()
        var enters = 0
        var leaves = 0
        animation.addFrameListener(0) {
            enterFrame = { enters++ }
            leaveFrame = { leaves++ }
        }
        // Frame: |0   1   2   3   4   5   6   |
        // Time:   0   10  20  30  40  50  60  70

        animation.update(2) // 2 out of 70
        assertThat(enters).isEqualTo(1)
        assertThat(leaves).isEqualTo(0)

        animation.restart()
        animation.update(animationTime - 1) // 69 out of 70
        assertThat(enters).isEqualTo(2)
        assertThat(leaves).isEqualTo(1)

        animation.update(1) // 70 out of 70
        assertThat(enters).isEqualTo(2)
        assertThat(leaves).isEqualTo(1)
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
    fun restart_linear_isLooping_callsListenersAppropriately() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.LINEAR,
            isLooping = true
        )
        val animationTime = animation.frames(iterations = 2).durations().sum().toLong()
        var enters = 0
        var leaves = 0
        animation.addFrameListener(0) {
            enterFrame = { enters++ }
            leaveFrame = { leaves++ }
        }
        // Frame: |0   1   2   3   4   5   6   0   1   2   3    4    5    6    0 |
        // Time:   0   10  20  30  40  50  60  70  80  90  100  110  120  130  140

        animation.update(2) // 2 out of 140
        assertThat(enters).isEqualTo(1)
        assertThat(leaves).isEqualTo(0)

        animation.restart()
        animation.update(animationTime - 1) // 139 out of 140
        assertThat(enters).isEqualTo(3)
        assertThat(leaves).isEqualTo(2)

        animation.update(1) // 140 out of 140
        assertThat(enters).isEqualTo(4)
        assertThat(leaves).isEqualTo(2)
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
    fun restart_pingPong_isNotLooping_callsListenersAppropriately() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = false
        )
        val animationTime = animation.frames().durations().sum().toLong()
        var enters = 0
        var leaves = 0
        animation.addFrameListener(0) {
            enterFrame = { enters++ }
            leaveFrame = { leaves++ }
        }

        // Frame: |0   1   2   3   4   6   5   4   3   2   1    0    |
        // Time:   0   10  20  30  40  50  60  70  80  90  100  110  120

        animation.update(2) // 2 out of 120
        assertThat(enters).isEqualTo(1)
        assertThat(leaves).isEqualTo(0)

        animation.restart()
        animation.update(animationTime - 11) // 109 out of 120
        assertThat(enters).isEqualTo(2)
        assertThat(leaves).isEqualTo(1)

        animation.update(1) // 110 out of 120
        assertThat(enters).isEqualTo(3)
        assertThat(leaves).isEqualTo(1)

        animation.update(10) // 120 out of 120
        assertThat(enters).isEqualTo(3)
        assertThat(leaves).isEqualTo(1)
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
    fun restart_pingPong_isLooping_callsListenersAppropriately() {
        val animation = animation(
            SPRITE_SHEET,
            frameRange = 0..6,
            frameDuration = 10,
            iterationMode = IterationMode.PING_PONG,
            isLooping = true
        )
        val animationTime = animation.frames(iterations = 2).durations().sum().toLong()
        var enters = 0
        var leaves = 0
        animation.addFrameListener(0) {
            enterFrame = { enters++ }
            leaveFrame = { leaves++ }
        }

        // Frame: |0   1   2   3   4   6   5   4   3   2   1
        // Time:   0   10  20  30  40  50  60  70  80  90  100
        //
        // Frame (cont'd): 0    1    2    3    4    6    5    4    3    2    1    0    1 |
        // Time: (cont'd): 110  120  130  140  150  160  170  180  190  200  210  220  230

        animation.update(2) // 2 out of 230
        assertThat(enters).isEqualTo(1)
        assertThat(leaves).isEqualTo(0)

        animation.restart()
        animation.update(animationTime - 11) // 219 out of 230
        assertThat(enters).isEqualTo(3)
        assertThat(leaves).isEqualTo(2)

        animation.update(1) // 220 out of 230
        assertThat(enters).isEqualTo(4)
        assertThat(leaves).isEqualTo(2)

        animation.update(10) // 230 out of 230
        assertThat(enters).isEqualTo(4)
        assertThat(leaves).isEqualTo(3)
    }

    companion object {
        private val SPRITE_SHEET = FakeSpriteSheet(numRows = 3, numColumns = 3)
    }
}
