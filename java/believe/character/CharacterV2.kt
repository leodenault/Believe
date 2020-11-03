package believe.character

import believe.animation.BidirectionalAnimation
import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.geometry.mutableRectangle
import believe.map.collidable.tile.CollidableTileCollisionHandler
import believe.map.collidable.tile.CollidableTileCollisionHandler.TileCollidable
import believe.physics.collision.Collidable
import believe.physics.collision.CollisionHandler
import believe.physics.damage.DamageBoxCollidable
import believe.physics.damage.DamageBoxCollisionHandler
import believe.physics.manager.PhysicsManageable
import believe.physics.manager.PhysicsManager
import believe.react.Observable
import believe.react.ObservableValue
import believe.react.Observer
import believe.scene.SceneElement
import dagger.Reusable
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

interface CharacterV2 : SceneElement, TileCollidable<CharacterV2>, DamageBoxCollidable<CharacterV2>,
    PhysicsManageable, Observable<Rectangle> {

    val observableFocus: Observable<Float>

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
        private val collidableTileCollisionHandler: CollidableTileCollisionHandler,
        private val damageBoxCollisionHandler: DamageBoxCollisionHandler
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
                    stateMachine,
                    verticalMovementHandlerFactory.create(
                        stateMachine, INITIAL_JUMP_VELOCITY, LANDED_VERTICAL_VELOCITY_TOLERANCE
                    ),
                    damageListener,
                    setOf(collidableTileCollisionHandler, damageBoxCollisionHandler),
                    x,
                    y,
                    faction
                )
            }
        }
    }

    companion object {
        private const val MOVEMENT_SPEED = 0.2f // Pixels per millisecond
        private const val INITIAL_JUMP_VELOCITY = -0.5f // Pixels per millisecond
        private const val LANDED_VERTICAL_VELOCITY_TOLERANCE = 0.1f // Pixels per millisecond
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
    private val internalObservableFocus = ObservableValue.of(CharacterV2.MAX_FOCUS)
    private var focus: Float
        get() = internalObservableFocus.get()
        set(value) = internalObservableFocus.setValue(value)
    private val bounds = mutableRectangle(
        initialX, initialY, stateMachine.animation.width, stateMachine.animation.height
    )
    private val observers = mutableListOf<Observer<Rectangle>>()

    override val observableFocus: Observable<Float> = internalObservableFocus
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

    override fun landed() {
        stateMachine.land()
        verticalMovementHandler.land()
    }

    override fun getVerticalSpeed() = verticalMovementHandler.verticalVelocity

    override fun getFloatX() = x

    override fun getFloatY() = y

    override fun setVerticalSpeed(speed: Float) {
        verticalMovementHandler.verticalVelocity = speed
    }

    override fun render(g: Graphics) = g.drawAnimation(stateMachine.animation, x, y)

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

    override fun update(delta: Long) {
        x += stateMachine.horizontalMovementSpeed * delta
        y += verticalSpeed * delta
        stateMachine.animation.update(delta)
    }

    override fun addToPhysicsManager(physicsManager: PhysicsManager) {
        physicsManager.addCollidable(this)
        physicsManager.addGravityObject(this)
    }

    override fun rect() = bounds

    override fun getFaction() = faction

    override fun addObserver(observer: Observer<Rectangle>): Observable<Rectangle> {
        observers.add(observer)
        return this
    }
}
