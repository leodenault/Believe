package believe.character

import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.newdawn.slick.Animation

internal class MovementStateMachineTest {
    private val inputAdapter = FakeInputAdapter.create<CharacterMovementInputAction>()
    private val stateMachine = MovementStateMachine.Factory(
        inputAdapter, HorizontalMovementStateMachine.Factory(inputAdapter)
    ).create(
        idleAnimation = BidirectionalAnimation.from(Animation()),
        movementAnimation = BidirectionalAnimation.from(Animation()),
        jumpingAnimation = JUMPING_ANIMATION,
        horizontalMovementSpeed = HORIZONTAL_MOVEMENT_SPEED
    )

    @BeforeEach
    fun setUp() {
        stateMachine.bind()
    }

    @Test
    fun new_isIdleAndNotJumping() {
        assertThat(stateMachine.bidirectionalAnimation).isNotEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)
    }

    @Test
    fun neverJump_isNotJumping() {
        sequenceOf(
            CharacterMovementInputAction.MOVE_LEFT, CharacterMovementInputAction.MOVE_RIGHT
        ).flatMap {
            sequenceOf({ inputAdapter.actionStarted(it) }, { inputAdapter.actionEnded(it) })
        }.forEach {
            it()
            assertThat(stateMachine.bidirectionalAnimation).isNotEqualTo(JUMPING_ANIMATION)
        }
    }

    @Test
    fun jump_fromInput_isJumpingIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)
    }

    @Test
    fun jump_fromMethodCall_isJumpingIdle() {
        stateMachine.jump()

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)
    }

    @Test
    fun moveLeftStart_isJumpingIdle_isJumpingMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(HORIZONTAL_MOVEMENT_SPEED)
    }

    @Test
    fun moveRightStart_isJumpingIdle_isJumpingMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(HORIZONTAL_MOVEMENT_SPEED)
    }

    @Test
    fun moveLeftEnd_isJumpingIdle_isJumpingMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(HORIZONTAL_MOVEMENT_SPEED)
    }

    @Test
    fun moveRightEnd_isJumpingIdle_isJumpingMoving() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(HORIZONTAL_MOVEMENT_SPEED)
    }

    @Test
    fun moveLeftStart_isJumpingMoving_isJumpingIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)
    }

    @Test
    fun moveRightStart_isJumpingMoving_isJumpingIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)
    }

    @Test
    fun moveLeftEnd_isJumpingMoving_isJumpingIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)
    }

    @Test
    fun moveRightEnd_isJumpingMoving_isJumpingIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(stateMachine.bidirectionalAnimation).isEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)
    }

    @Test
    fun land_fromInput_isNotJumpingIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        inputAdapter.actionStarted(CharacterMovementInputAction.LAND)

        assertThat(stateMachine.bidirectionalAnimation).isNotEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)
    }

    @Test
    fun land_fromMethodCall_isNotJumpingIdle() {
        stateMachine.jump()
        stateMachine.land()

        assertThat(stateMachine.bidirectionalAnimation).isNotEqualTo(JUMPING_ANIMATION)
        assertThat(stateMachine.horizontalMovementSpeed).isEqualTo(0f)
    }

    @Test
    fun isJumping_stateIsLanded_returnsFalse() {
        assertThat(stateMachine.isJumping()).isFalse()
    }

    @Test
    fun isJumping_stateIsJumping_returnsTrue() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)

        assertThat(stateMachine.isJumping()).isTrue()
    }

    companion object {
        private val JUMPING_ANIMATION = BidirectionalAnimation.from(Animation())
        private const val HORIZONTAL_MOVEMENT_SPEED = 10f
    }
}
