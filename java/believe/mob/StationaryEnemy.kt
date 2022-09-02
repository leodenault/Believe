package believe.mob

import believe.animation.Animation
import believe.animation.CompoundAnimation
import believe.animation.compoundAnimation
import believe.character.Faction
import believe.core.display.Graphics
import believe.mob.proto.MobAnimationDataProto.DamageFrame
import believe.physics.damage.DamageBoxFactory
import believe.physics.manager.PhysicsManager
import believe.scene.SceneElement
import dagger.Reusable
import javax.inject.Inject

internal class StationaryEnemy private constructor(
    override var x: Float,
    override var y: Float,
    private val physicsManager: PhysicsManager,
    private val damageBoxFactory: DamageBoxFactory,
    idleAnimation: Animation,
    private val attackAnimation: Animation,
    private val attackDamageFrames: List<DamageFrame>
) : SceneElement {

    private val animation: CompoundAnimation =
        compoundAnimation(true, 2 to idleAnimation, 1 to attackAnimation)

    override fun bind() {
        attackDamageFrames.map {
            it.frameIndex to with(it.dimensions) {
                damageBoxFactory.create(
                    Faction.BAD,
                    this@StationaryEnemy.x.toInt() + x,
                    this@StationaryEnemy.y.toInt() + y,
                    width,
                    height,
                    0.1f
                )
            }
        }.forEach { pair ->
            val (frameIndex, damageBox) = pair
            animation.addFrameListener(attackAnimation, frameIndex) {
                enterFrame = { physicsManager.addCollidable(damageBox) }
                leaveFrame = { physicsManager.removeCollidable(damageBox) }
            }
        }
    }

    override fun unbind() {}

    override fun update(delta: Long) = animation.update(delta)

    override fun render(g: Graphics) = g.drawAnimation(animation, x, y)

    @Reusable
    class Factory @Inject internal constructor(
        private val physicsManager: PhysicsManager,
        private val damageBoxFactory: DamageBoxFactory
    ) {
        fun create(
            x: Float,
            y: Float,
            idleAnimation: Animation,
            attackAnimation: Animation,
            damageFrames: List<DamageFrame>
        ) = StationaryEnemy(
            x, y, physicsManager, damageBoxFactory, idleAnimation, attackAnimation, damageFrames
        )
    }
}
