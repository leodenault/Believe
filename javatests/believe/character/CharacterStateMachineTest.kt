package believe.character

import believe.animation.BidirectionalAnimation
import believe.animation.emptyAnimation
import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CharacterStateMachineTest {
    private val inputAdapter: FakeInputAdapter<CharacterMovementInputAction> =
        FakeInputAdapter.create()
    private val stateMachine = CharacterStateMachine.Factory(
        OrientationStateMachine.Factory(inputAdapter), MovementStateMachine.Factory(
            inputAdapter, HorizontalMovementStateMachine.Factory(inputAdapter)
        ), VulnerabilityStateMachine(100, 5, MAX_FOCUS, FOCUS_RECHARGE_TIME)
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

    @Test
    fun inflictDamage_reducesFocusAndMakesInvulnerable() {
        var focus = 0f
        stateMachine.focus.addObserver { newValue -> focus = newValue }

        stateMachine.inflictDamage(0.3f)

        assertThat(stateMachine.isAnimationVisible).isFalse()
        assertThat(focus).isEqualTo(MAX_FOCUS - 0.3f)
    }

    @Test
    fun update_updatesUnderlyingConstructs() {
        var focus = 0f
        stateMachine.focus.addObserver { newValue -> focus = newValue }
        stateMachine.inflictDamage(0.3f)

        stateMachine.update(FOCUS_RECHARGE_TIME / 10)

        assertThat(focus).isEqualTo((11 * MAX_FOCUS / 10) - 0.3f)
    }

    companion object {
        private val IDLE_ANIMATION = BidirectionalAnimation.from(emptyAnimation())
        private val MOVEMENT_ANIMATION = BidirectionalAnimation.from(emptyAnimation())
        private val JUMP_ANIMATION = BidirectionalAnimation.from(emptyAnimation())
        private const val HORIZONTAL_MOVEMENT_SPEED = 12f
        private const val MAX_FOCUS = 1f
        private const val FOCUS_RECHARGE_TIME = 10L
    }
}
