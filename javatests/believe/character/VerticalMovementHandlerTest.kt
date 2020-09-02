package believe.character

import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.newdawn.slick.Animation

internal class VerticalMovementHandlerTest {
    private val inputAdapter = FakeInputAdapter.create<CharacterMovementInputAction>()
    private val stateMachine: CharacterStateMachine = CharacterStateMachine.Factory(
        OrientationStateMachine.Factory(inputAdapter), MovementStateMachine.Factory(
            inputAdapter, HorizontalMovementStateMachine.Factory(inputAdapter)
        )
    ).create(IDLE_ANIMATION, MOVEMENT_ANIMATION, JUMP_ANIMATION, HORIZONTAL_MOVEMENT_SPEED)
    private val handler: VerticalMovementHandler =
        VerticalMovementHandler.Factory(inputAdapter).create(
            stateMachine, INITIAL_JUMP_VELOCITY, MAXIMUM_LANDED_VERTICAL_VELOCITY_TOLERANCE
        )

    @BeforeEach
    fun setUp() {
        handler.bind()
    }

    @Test
    fun new_verticalVelocityIsZero() {
        assertThat(handler.verticalVelocity).isEqualTo(0f)
    }

    @Test
    fun bind_registersListeners() {
        assertThat(inputAdapter.startListeners).isNotEmpty()
    }

    @Test
    fun unbind_unregistersListeners() {
        handler.unbind()

        assertThat(inputAdapter.startListeners.values.flatten()).isEmpty()
    }

    @Test
    fun jump_isLanded_verticalVelocityIsInitialJumpVelocity() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)

        assertThat(handler.verticalVelocity).isEqualTo(INITIAL_JUMP_VELOCITY)
    }

    @Test
    fun jump_isJumping_verticalVelocityRemainsTheSame() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        handler.verticalVelocity = -200f

        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)

        assertThat(handler.verticalVelocity).isEqualTo(-200f)
    }

    @Test
    fun land_isJumping_allowsJumpingAgain() {
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)

        handler.land()
        handler.verticalVelocity = -1f
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)

        assertThat(handler.verticalVelocity).isEqualTo(INITIAL_JUMP_VELOCITY)
    }

    @Test
    fun land_isLanded_doesNothing() {
        handler.land()

        assertThat(handler.verticalVelocity).isEqualTo(0f)
    }

    @Test
    fun setVerticalVelocity_setsVerticalVelocityCorrectly() {
        handler.verticalVelocity = 9.8f

        assertThat(handler.verticalVelocity).isEqualTo(9.8f)
    }

    @Test
    fun setVerticalVelocity_exceedsMaximumLandedTolerance_updatesCharacterStateMachineAndPreventsJump() {
        handler.verticalVelocity = 10.1f
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)

        assertThat(stateMachine.animation).isEqualTo(JUMP_ANIMATION.rightAnimation)
        assertThat(handler.verticalVelocity).isEqualTo(10.1f)

        stateMachine.land()
        handler.land()
        handler.verticalVelocity = -10.1f

        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)
        assertThat(stateMachine.animation).isEqualTo(JUMP_ANIMATION.rightAnimation)
        assertThat(handler.verticalVelocity).isEqualTo(-10.1f)
    }

    @Test
    fun jump_previouslyLandedMultipleTimesBeforeExceedingMaximumTolerance_ignores() {
        handler.land()
        handler.land()

        handler.verticalVelocity = 10.1f
        inputAdapter.actionStarted(CharacterMovementInputAction.JUMP)

        assertThat(handler.verticalVelocity).isEqualTo(10.1f)
    }

    companion object {
        private val IDLE_ANIMATION = BidirectionalAnimation.from(Animation())
        private val MOVEMENT_ANIMATION = BidirectionalAnimation.from(Animation())
        private val JUMP_ANIMATION = BidirectionalAnimation.from(Animation())
        private const val HORIZONTAL_MOVEMENT_SPEED = 12f
        private const val INITIAL_JUMP_VELOCITY = -500f
        private const val MAXIMUM_LANDED_VERTICAL_VELOCITY_TOLERANCE = 10f
    }
}
