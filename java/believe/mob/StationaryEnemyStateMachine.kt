package believe.mob

import believe.animation.Animation
import believe.core.Updatable
import believe.core.display.Bindable

/**
 * A state machine that manages the state of a [StationaryEnemy].
 *
 * @param idleAnimation an [Animation] that whose [Animation.isLooping] value is assumed to be true.
 *   It is used for the enemy's idle stance.
 * @param attackAnimation an [Animation] used for the enemy's attack.
 */
internal class StationaryEnemyStateMachine(
    private val idleAnimation: Animation, private val attackAnimation: Animation
) : Updatable, Bindable {
    val animation: Animation
        get() = currentAnimation

    private var currentAnimation: Animation = idleAnimation
    private var idleIterations = 0

    override fun bind() {
        idleAnimation.addAnimationEndedListener {
            idleIterations = (idleIterations + 1) % 2
            if (idleIterations == 0) {
                idleAnimation.restart()
                currentAnimation = attackAnimation
            }
        }
        attackAnimation.addAnimationEndedListener {
            attackAnimation.restart()
            currentAnimation = idleAnimation
        }
    }

    override fun unbind() {}

    override fun update(delta: Long) {
        currentAnimation.update(delta)
    }
}
