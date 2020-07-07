package believe.character

import believe.core.display.Bindable
import org.newdawn.slick.Animation
import javax.inject.Inject

internal class CharacterStateMachine private constructor(
    private val orientationStateMachine: OrientationStateMachine,
    private val movementStateMachine: MovementStateMachine
) : Bindable {

    internal val animation: Animation
        get() = orientationStateMachine.data.chooseAnimationDirection(
            movementStateMachine.bidirectionalAnimation
        )
    internal val horizontalMovementSpeed: Float
        get() = movementStateMachine.horizontalMovementSpeed * orientationStateMachine.data.movementMultiplier

    override fun bind() {
        orientationStateMachine.bind()
        movementStateMachine.bind()
    }

    override fun unbind() {
        orientationStateMachine.unbind()
        movementStateMachine.unbind()
    }

    internal fun jump() = movementStateMachine.jump()

    internal fun land() = movementStateMachine.land()

    internal fun isJumping() = movementStateMachine.isJumping()

    internal class Factory @Inject internal constructor(
        private val orientationStateMachineFactory: OrientationStateMachine.Factory,
        private val movementStateMachineFactory: MovementStateMachine.Factory
    ) {
        internal fun create(
            idleAnimation: BidirectionalAnimation,
            movementAnimation: BidirectionalAnimation,
            jumpingAnimation: BidirectionalAnimation,
            horizontalMovementSpeed: Float
        ) = CharacterStateMachine(
            orientationStateMachineFactory.create(), movementStateMachineFactory.create(
                idleAnimation, movementAnimation, jumpingAnimation, horizontalMovementSpeed
            )
        )
    }
}
