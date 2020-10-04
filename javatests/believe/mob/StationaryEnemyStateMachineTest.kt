package believe.mob

import believe.animation.animation
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.assertThat
import believe.animation.testing.frames
import believe.gui.testing.FakeImage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StationaryEnemyStateMachineTest {
    private val idleAnimation = animation(
        frames = List(size = 5, init = { FakeImage() }),
        frameDurations = listOf(10, 12, 5, 78, 33),
        iterationMode = IterationMode.PING_PONG,
        isLooping = true
    )
    private val attackAnimation = animation(
        frames = List(size = 3, init = { FakeImage() }),
        frameDurations = listOf(11, 13, 6),
        iterationMode = IterationMode.PING_PONG,
        isLooping = true
    )

    private val stateMachine = StationaryEnemyStateMachine(idleAnimation, attackAnimation)

    @BeforeEach
    fun setUp() {
        stateMachine.bind()
    }

    @Test
    fun animation_updatesFramesCorrectly() {
        val frames = (0 until 2).flatMap {
            idleAnimation.frames(iterations = 2) + attackAnimation.frames(
                iterations = 1
            )
        }

        assertThat(stateMachine).updating { stateMachine.animation }.generatesFrames(frames)
    }
}
