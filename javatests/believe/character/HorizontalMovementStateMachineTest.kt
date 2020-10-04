package believe.character

import believe.animation.BidirectionalAnimation
import believe.animation.emptyAnimation
import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.newdawn.slick.Animation

internal class HorizontalMovementStateMachineTest {
    private val inputAdapter = FakeInputAdapter.create<CharacterMovementInputAction>()
    private val machine = HorizontalMovementStateMachine.Factory(inputAdapter)
        .create(IDLE_ANIMATION, MOVEMENT_ANIMATION, MOVEMENT_SPEED)

    @BeforeEach
    fun setUp() {
        machine.bind()
    }

    @Test
    fun new_initialStateIsIdle() {
        assertThat(machine.data.bidirectionalAnimation).isEqualTo(IDLE_ANIMATION)
        assertThat(machine.data.movementSpeed).isEqualTo(0f)
    }

    @Test
    fun moveLeftStart_fromIdle_newStateIsMovement() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(machine.data.bidirectionalAnimation).isEqualTo(MOVEMENT_ANIMATION)
        assertThat(machine.data.movementSpeed).isEqualTo(MOVEMENT_SPEED)
    }

    @Test
    fun moveRightStart_fromIdle_newStateIsMovement() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(machine.data.bidirectionalAnimation).isEqualTo(MOVEMENT_ANIMATION)
        assertThat(machine.data.movementSpeed).isEqualTo(MOVEMENT_SPEED)
    }

    @Test
    fun moveLeftEnd_fromIdle_newStateIsMovement() {
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(machine.data.bidirectionalAnimation).isEqualTo(MOVEMENT_ANIMATION)
        assertThat(machine.data.movementSpeed).isEqualTo(MOVEMENT_SPEED)
    }

    @Test
    fun moveRightEnd_fromIdle_newStateIsMovement() {
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(machine.data.bidirectionalAnimation).isEqualTo(MOVEMENT_ANIMATION)
        assertThat(machine.data.movementSpeed).isEqualTo(MOVEMENT_SPEED)
    }

    @Test
    fun moveLeftStart_fromMovement_newStateIsIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(machine.data.bidirectionalAnimation).isEqualTo(IDLE_ANIMATION)
        assertThat(machine.data.movementSpeed).isEqualTo(0f)
    }

    @Test
    fun moveRightStart_fromMovement_newStateIsIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(machine.data.bidirectionalAnimation).isEqualTo(IDLE_ANIMATION)
        assertThat(machine.data.movementSpeed).isEqualTo(0f)
    }

    @Test
    fun moveLeftEnd_fromMovement_newStateIsIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_LEFT)

        assertThat(machine.data.bidirectionalAnimation).isEqualTo(IDLE_ANIMATION)
        assertThat(machine.data.movementSpeed).isEqualTo(0f)
    }

    @Test
    fun moveRightEnd_fromMovement_newStateIsIdle() {
        inputAdapter.actionStarted(CharacterMovementInputAction.MOVE_LEFT)
        inputAdapter.actionEnded(CharacterMovementInputAction.MOVE_RIGHT)

        assertThat(machine.data.bidirectionalAnimation).isEqualTo(IDLE_ANIMATION)
        assertThat(machine.data.movementSpeed).isEqualTo(0f)
    }

    companion object {
        private val IDLE_ANIMATION = BidirectionalAnimation.from(emptyAnimation())
        private val MOVEMENT_ANIMATION = BidirectionalAnimation.from(emptyAnimation())
        private const val MOVEMENT_SPEED = 1f
    }
}