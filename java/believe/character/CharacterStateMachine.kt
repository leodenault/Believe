package believe.character

import believe.animation.Animation
import believe.animation.BidirectionalAnimation
import believe.core.Updatable
import believe.core.display.Bindable
import javax.inject.Inject

internal class CharacterStateMachine private constructor(
    private val orientationStateMachine: OrientationStateMachine,
    private val movementStateMachine: MovementStateMachine,
    private val vulnerabilityStateMachine: VulnerabilityStateMachine
) : Bindable, Updatable {

    val isAnimationVisible: Boolean
        get() = vulnerabilityStateMachine.isAnimationVisible
    val animation: Animation
        get() = orientationStateMachine.data.chooseAnimationDirection(
            movementStateMachine.bidirectionalAnimation
        )
    val horizontalMovementSpeed: Float
        get() = movementStateMachine.horizontalMovementSpeed * orientationStateMachine.data.movementMultiplier
    val focus = vulnerabilityStateMachine.focus

    override fun bind() {
        orientationStateMachine.bind()
        movementStateMachine.bind()
    }

    override fun unbind() {
        orientationStateMachine.unbind()
        movementStateMachine.unbind()
    }

    override fun update(delta: Long) {
        animation.update(delta)
        vulnerabilityStateMachine.update(delta)
    }

    fun jump() = movementStateMachine.jump()

    fun land() = movementStateMachine.land()

    fun inflictDamage(damage: Float) = vulnerabilityStateMachine.inflictDamage(damage)

    class Factory @Inject internal constructor(
        private val orientationStateMachineFactory: OrientationStateMachine.Factory,
        private val movementStateMachineFactory: MovementStateMachine.Factory,
        private val vulnerabilityStateMachine: VulnerabilityStateMachine
    ) {
        internal fun create(
            idleAnimation: BidirectionalAnimation,
            movementAnimation: BidirectionalAnimation,
            jumpingAnimation: BidirectionalAnimation,
            horizontalMovementSpeed: Float
        ) = CharacterStateMachine(
            orientationStateMachineFactory.create(), movementStateMachineFactory.create(
                idleAnimation, movementAnimation, jumpingAnimation, horizontalMovementSpeed
            ), vulnerabilityStateMachine
        )
    }
}
