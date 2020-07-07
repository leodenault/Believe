package believe.character

import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.newdawn.slick.Animation

internal class OrientationStateMachineTest {
    private val inputAdapter = FakeInputAdapter.create<CharacterMovementInputAction>()
    private val stateMachine = OrientationStateMachine.Factory(inputAdapter).create()

    @BeforeEach
    fun setUp() {
        stateMachine.bind()
    }

    @Test
    fun new_isFacingRightIdle() {
        assertThat(stateMachine.data.movementMultiplier).isEqualTo(1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(RIGHT_ANIMATION)
    }

    @Test
    fun moveLeftStart_fromFacingRightIdle_isFacingLeftMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(-1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(LEFT_ANIMATION)
    }

    @Test
    fun moveRightEnd_fromFacingRightIdle_isFacingLeftMoving() {
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(-1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(LEFT_ANIMATION)
    }

    @Test
    fun moveRightStart_fromFacingRightIdle_isFacingRightMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(RIGHT_ANIMATION)
    }

    @Test
    fun moveLeftEnd_fromFacingRightIdle_isFacingRightMoving() {
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(RIGHT_ANIMATION)
    }

    @Test
    fun moveRightEnd_fromFacingRightMoving_isFacingRightIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(RIGHT_ANIMATION)
    }

    @Test
    fun moveLeftStart_fromFacingRightMoving_isFacingRightIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(RIGHT_ANIMATION)
    }

    @Test
    fun moveLeftEnd_fromFacingLeftMoving_isFacingLeftIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(-1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(LEFT_ANIMATION)
    }

    @Test
    fun moveRightStart_fromFacingLeftMoving_isFacingLeftIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(-1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(LEFT_ANIMATION)
    }

    @Test
    fun moveLeftStart_fromFacingLeftIdle_isFacingLeftMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(-1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(LEFT_ANIMATION)
    }

    @Test
    fun moveRightEnd_fromFacingLeftIdle_isFacingLeftMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(-1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(LEFT_ANIMATION)
    }

    @Test
    fun moveRightStart_fromFacingLeftIdle_isFacingRightMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(RIGHT_ANIMATION)
    }

    @Test
    fun moveLeftEnd_fromFacingLeftIdle_isFacingRightMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.data.movementMultiplier).isEqualTo(1)
        assertThat(
            stateMachine.data.chooseAnimationDirection(BIDIRECTIONAL_ANIMATION)
        ).isEqualTo(RIGHT_ANIMATION)
    }

    companion object {
        private val LEFT_ANIMATION = Animation()
        private val RIGHT_ANIMATION = Animation()
        private val BIDIRECTIONAL_ANIMATION =
            BidirectionalAnimation(LEFT_ANIMATION, RIGHT_ANIMATION)
    }
}