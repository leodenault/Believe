package believe.character

import believe.animation.BidirectionalAnimation
import believe.core.display.Bindable
import believe.input.InputAdapter
import dagger.Reusable
import javax.inject.Inject

internal class MovementStateMachine private constructor(
    private val inputAdapter: InputAdapter<CharacterMovementInputAction>,
    private val horizontalMovementStateMachine: HorizontalMovementStateMachine,
    private val jumpAnimation: BidirectionalAnimation
) : Bindable {

    private var resolveAnimation: () -> BidirectionalAnimation = this::getLandedAnimation

    internal val horizontalMovementSpeed: Float
        get() = horizontalMovementStateMachine.data.movementSpeed
    internal val bidirectionalAnimation: BidirectionalAnimation
        get() = resolveAnimation()

    override fun bind() {
        inputAdapter.addActionStartListener(CharacterMovementInputAction.JUMP, this::jump)
        inputAdapter.addActionStartListener(CharacterMovementInputAction.LAND, this::land)
        horizontalMovementStateMachine.bind()
    }

    override fun unbind() {
        inputAdapter.removeActionStartListener(CharacterMovementInputAction.JUMP, this::jump)
        inputAdapter.removeActionStartListener(CharacterMovementInputAction.LAND, this::land)
        horizontalMovementStateMachine.unbind()
    }

    internal fun jump() {
        resolveAnimation = { jumpAnimation }
    }

    internal fun land() {
        resolveAnimation = this::getLandedAnimation
    }

    internal fun isJumping() = resolveAnimation() == jumpAnimation

    private fun getLandedAnimation() = horizontalMovementStateMachine.data.bidirectionalAnimation

    @Reusable
    internal class Factory @Inject internal constructor(
        private val inputAdapter: InputAdapter<CharacterMovementInputAction>,
        private val horizontalMovementStateMachineFactory: HorizontalMovementStateMachine.Factory
    ) {
        fun create(
            idleAnimation: BidirectionalAnimation,
            movementAnimation: BidirectionalAnimation,
            jumpingAnimation: BidirectionalAnimation,
            horizontalMovementSpeed: Float
        ) = MovementStateMachine(
            inputAdapter, horizontalMovementStateMachineFactory.create(
                idleAnimation, movementAnimation, horizontalMovementSpeed
            ), jumpingAnimation
        )
    }
}
