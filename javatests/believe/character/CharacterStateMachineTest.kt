package believe.character

import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.newdawn.slick.Animation

internal class CharacterStateMachineTest {
    private val inputAdapter: FakeInputAdapter<CharacterMovementInputAction> =
        FakeInputAdapter.create()
    private val stateMachine = CharacterStateMachine.Factory(
        OrientationStateMachine.Factory(inputAdapter), MovementStateMachine.Factory(
            inputAdapter, HorizontalMovementStateMachine.Factory(inputAdapter)
        )
    ).create(IDLE_ANIMATION, MOVEMENT_ANIMATION, JUMP_ANIMATION, HORIZONTAL_MOVEMENT_SPEED)

    @BeforeEach
    fun setUp() {
        stateMachine.bind()
    }

    @Test
    fun horizontalMovementSpeed_combinesSpeedAndOrientation() {
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)

        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(-HORIZONTAL_MOVEMENT_SPEED)

        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(-0f)

        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(HORIZONTAL_MOVEMENT_SPEED)
    }

    @Test
    fun animation_choosesCorrectAnimationOrientation() {
        assertThat(stateMachine.animation).isEqualTo(IDLE_ANIMATION.rightAnimation)

        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.animation).isEqualTo(MOVEMENT_ANIMATION.leftAnimation)

        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.animation).isEqualTo(IDLE_ANIMATION.leftAnimation)

        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.animation).isEqualTo(MOVEMENT_ANIMATION.rightAnimation)

        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)

        assertThat(stateMachine.animation).isEqualTo(JUMP_ANIMATION.rightAnimation)

        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_RIGHT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.animation).isEqualTo(JUMP_ANIMATION.leftAnimation)
    }

    companion object {
        private val IDLE_ANIMATION = BidirectionalAnimation.from(Animation())
        private val MOVEMENT_ANIMATION = BidirectionalAnimation.from(Animation())
        private val JUMP_ANIMATION = BidirectionalAnimation.from(Animation())
        private const val HORIZONTAL_MOVEMENT_SPEED = 12f
    }
}
