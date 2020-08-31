package believe.character

import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.geometry.mutableRectangle
import believe.geometry.rectangle
import believe.map.collidable.tile.CollidableTileCollisionHandler
import believe.map.collidable.tile.CollidableTileCollisionHandler.TileCollidable
import believe.physics.collision.Collidable
import believe.physics.collision.CollisionHandler
import believe.physics.damage.DamageBoxCollidable
import believe.physics.manager.PhysicsManageable
import believe.physics.manager.PhysicsManager
import believe.react.Observable
import believe.react.Observer
import believe.scene.SceneElement
import dagger.Reusable
import org.newdawn.slick.util.Log
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

interface CharacterV2 : SceneElement, TileCollidable<CharacterV2>, DamageBoxCollidable<CharacterV2>,
    PhysicsManageable, Observable<Rectangle> {

    val focus: Float

    fun heal(health: Float)

    interface DamageListener {
        fun damageInflicted(currentFocus: Float, inflictor: Faction)

        companion object {
            val NONE = object : DamageListener {
                override fun damageInflicted(currentFocus: Float, inflictor: Faction) {}
            }
        }
    }

    // Open for mocking.
    @Reusable
    open class Factory @Inject internal constructor(
        private val stateMachineFactory: CharacterStateMachine.Factory,
        private val verticalMovementHandlerFactory: VerticalMovementHandler.Factory,
        private val collidableTileCollisionHandler: CollidableTileCollisionHandler
    ) {
        open fun create(
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
                return CharacterV2Impl(
                    stateMachine, verticalMovementHandlerFactory.create(
                        stateMachine, INITIAL_JUMP_VELOCITY, LANDED_VERTICAL_VELOCITY_TOLERANCE
                    ), damageListener, setOf(collidableTileCollisionHandler), x, y, faction
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

private class CharacterV2Impl constructor(
    private val stateMachine: CharacterStateMachine,
    private val verticalMovementHandler: VerticalMovementHandler,
    private val damageListener: CharacterV2.DamageListener,
    private val rightCompatibleHandlers: Set<CollisionHandler<out Collidable<*>, in CharacterV2>>,
    initialX: Float,
    initialY: Float,
    private val faction: Faction
) : CharacterV2 {
    private val bounds = mutableRectangle(
        initialX,
        initialY,
        stateMachine.animation.width.toFloat(),
        stateMachine.animation.height.toFloat()
    )
    private val observers = mutableListOf<Observer<Rectangle>>()

    override var focus = CharacterV2.MAX_FOCUS
        private set
    override var x: Float
        get() = bounds.x
        set(value) {
            bounds.x = value
            observers.forEach { it.valueChanged(bounds) }
        }
    override var y: Float
        get() = bounds.y
        set(value) {
            bounds.y = value
            observers.forEach { it.valueChanged(bounds) }
        }

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

    override fun heal(health: Float) {
        focus = min(CharacterV2.MAX_FOCUS, focus + health)
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

    override fun rect() = rectangle(
        x,
        y,
        stateMachine.animation.currentFrame.width.toFloat(),
        stateMachine.animation.currentFrame.height.toFloat()
    )

    override fun getFaction() = faction

    override fun addObserver(observer: Observer<Rectangle>): Observable<Rectangle> {
        observers.add(observer)
        return this
    }
}
