package believe.character

import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.map.collidable.tile.CollidableTileCollisionHandler.TileCollidable
import believe.physics.collision.Collidable
import believe.physics.collision.CollisionHandler
import believe.physics.damage.DamageBoxCollidable
import believe.physics.manager.PhysicsManageable
import believe.physics.manager.PhysicsManager
import believe.scene.SceneElement
import dagger.Reusable
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class CharacterV2 private constructor(
    private val stateMachine: CharacterStateMachine,
    private val verticalMovementHandler: VerticalMovementHandler,
    private val damageListener: DamageListener,
    private val rightCompatibleHandlers: Set<CollisionHandler<out Collidable<*>, in CharacterV2>>,
    override var x: Float,
    override var y: Float,
    private val faction: Faction
) : SceneElement, TileCollidable<CharacterV2>, DamageBoxCollidable<CharacterV2>, PhysicsManageable {

    var focus = MAX_FOCUS
        private set

    override fun setLocation(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    override fun landed() = stateMachine.land()

    override fun getVerticalSpeed() = verticalMovementHandler.verticalVelocity

    override fun getFloatX() = x

    override fun getFloatY() = y

    override fun setVerticalSpeed(speed: Float) {
        verticalMovementHandler.verticalVelocity = speed
    }

    override fun render(g: Graphics) = g.drawImage(stateMachine.animation.currentFrame, x, y)

    override fun bind() {
        stateMachine.bind()
        verticalMovementHandler.bind()
    }

    override fun unbind() {
        stateMachine.unbind()
        verticalMovementHandler.unbind()
    }

    override fun leftCompatibleHandlers(): Set<CollisionHandler<in CharacterV2, out Collidable<*>>> =
        emptySet()

    override fun rightCompatibleHandlers(): Set<CollisionHandler<out Collidable<*>, in CharacterV2>> =
        rightCompatibleHandlers

    override fun inflictDamage(damage: Float, inflictor: Faction) {
        focus = max(0f, focus - damage)
        damageListener.damageInflicted(focus, inflictor)
    }

    fun heal(health: Float) {
        focus = min(MAX_FOCUS, focus + health)
    }

    override fun update(delta: Int) {
        x += stateMachine.horizontalMovementSpeed * delta
        y += verticalSpeed * delta
        stateMachine.animation.update(delta.toLong())
    }

    override fun addToPhysicsManager(physicsManager: PhysicsManager) {
        physicsManager.addCollidable(this)
        physicsManager.addGravityObject(this)
    }

    override fun rect() = Rectangle(
        x,
        y,
        stateMachine.animation.currentFrame.width.toFloat(),
        stateMachine.animation.currentFrame.height.toFloat()
    )

    override fun getFaction() = faction

    interface DamageListener {
        fun damageInflicted(currentFocus: Float, inflictor: Faction)

        companion object {
            val NONE = object : DamageListener {
                override fun damageInflicted(currentFocus: Float, inflictor: Faction) {}
            }
        }
    }

    @Reusable
    class Factory @Inject internal constructor(
        private val stateMachineFactory: CharacterStateMachine.Factory,
        private val verticalMovementHandlerFactory: VerticalMovementHandler.Factory
//        private val rightCompatibleHandlers: Set<CollisionHandler<out Collidable<*>, in CharacterV2>>
    ) {
        fun create(
            animations: Animations,
            damageListener: DamageListener,
            x: Float,
            y: Float,
            faction: Faction
        ): CharacterV2 {
            with(animations) {
                val stateMachine: CharacterStateMachine = stateMachineFactory.create(
                    BidirectionalAnimation.from(idleAnimation),
                    BidirectionalAnimation.from(movementAnimation),
                    BidirectionalAnimation.from(jumpingAnimation),
                    MOVEMENT_SPEED
                )
                return CharacterV2(
                    stateMachine, verticalMovementHandlerFactory.create(
                        stateMachine, INITIAL_JUMP_VELOCITY, LANDED_VERTICAL_VELOCITY_TOLERANCE
                    ), damageListener, emptySet(), x, y, faction
                )
            }
        }
    }

    companion object {
        private const val MOVEMENT_SPEED = 0.2f
        private const val INITIAL_JUMP_VELOCITY = -0.5f
        private const val LANDED_VERTICAL_VELOCITY_TOLERANCE = 0.1f
        const val MAX_FOCUS = 1.0f
    }
}