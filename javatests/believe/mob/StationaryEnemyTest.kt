package believe.mob

import believe.animation.animation
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.assertThat
import believe.animation.testing.durations
import believe.animation.testing.frames
import believe.geometry.Rectangle
import believe.geometry.rectangle
import believe.gui.testing.FakeImage
import believe.mob.proto.MobAnimationDataProto.DamageBoxDimensions
import believe.mob.proto.MobAnimationDataProto.DamageFrame
import believe.physics.damage.DamageBox
import believe.physics.damage.DamageBoxCollisionHandler
import believe.physics.damage.DamageBoxFactory
import believe.physics.manager.PhysicsManager
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StationaryEnemyTest {
    private val physicsManager = mock<PhysicsManager>()
    private val damageBoxFactory = DamageBoxFactory { DamageBoxCollisionHandler(physicsManager) }
    private val idleAnimation = animation(
        frames = List(size = 5, init = { FakeImage() }),
        frameDurations = listOf(10, 12, 5, 78, 33),
        iterationMode = IterationMode.PING_PONG,
        isLooping = true
    )
    private val attackAnimation = animation(
        frames = List(size = 3, init = { FakeImage() }),
        frameDurations = listOf(11, 13, 6),
        iterationMode = IterationMode.PING_PONG,
        isLooping = true
    )

    private val enemy = StationaryEnemy.Factory(physicsManager, damageBoxFactory).create(
        ANIMATION_X, ANIMATION_Y, idleAnimation, attackAnimation, listOf(
            DamageFrame.newBuilder().setFrameIndex(0).setDimensions(DAMAGE_BOX_1).build(),
            DamageFrame.newBuilder().setFrameIndex(2).setDimensions(DAMAGE_BOX_2).build()
        )
    )

    @BeforeEach
    fun setUp() {
        enemy.bind()
    }

    @Test
    fun animation_updatesFramesCorrectly() {
        val frames = (0 until 2).flatMap {
            idleAnimation.frames(iterations = 2) + attackAnimation.frames(
                iterations = 1
            )
        }

        assertThat(enemy).rendersFrames(frames)
    }

    @Test
    fun update_updatesDamageFrames() {
        val idleAnimationTime = idleAnimation.frames(iterations = 2).durations().sum().toLong()
        val attackFrameDurations = attackAnimation.frames().durations().map { it.toLong() }

        // Update in two parts, otherwise all of the delta is attributed to the first animation. It
        // might be worth addressing this issue by creating a compound animation concept.
        verify(physicsManager, never()).addCollidable(any())

        enemy.update(idleAnimationTime)
        verify(physicsManager).addCollidable(argForWhich<DamageBox> {
            rect() == POSITIONED_DAMAGE_BOX_1
        })

        enemy.update(attackFrameDurations[0])
        verify(physicsManager).removeCollidable(argForWhich<DamageBox> {
            rect() == POSITIONED_DAMAGE_BOX_1
        })
        verify(physicsManager, never()).addCollidable(argForWhich<DamageBox> {
            rect() == POSITIONED_DAMAGE_BOX_2
        })

        enemy.update(attackFrameDurations[1])
        verify(physicsManager).addCollidable(argForWhich<DamageBox> {
            rect() == POSITIONED_DAMAGE_BOX_2
        })

        enemy.update(attackFrameDurations[2])
        verify(physicsManager, times(1)).addCollidable(argForWhich<DamageBox> {
            rect() == POSITIONED_DAMAGE_BOX_1
        })
        verify(physicsManager).removeCollidable(argForWhich<DamageBox> {
            rect() == POSITIONED_DAMAGE_BOX_2
        })

        enemy.update(attackFrameDurations[1])
        verify(physicsManager, times(2)).addCollidable(argForWhich<DamageBox> {
            rect() == POSITIONED_DAMAGE_BOX_1
        })

        enemy.update(attackFrameDurations[0])
        verify(physicsManager, times(2)).removeCollidable(argForWhich<DamageBox> {
            rect() == POSITIONED_DAMAGE_BOX_1
        })
    }

    companion object {
        private const val ANIMATION_X = 123f
        private const val ANIMATION_Y = 234f
        private val DAMAGE_BOX_1 = rectangle(x = 1f, y = 2f, width = 3f, height = 4f)
        private val DAMAGE_BOX_2 = rectangle(x = 5f, y = 6f, width = 7f, height = 8f)
        private val POSITIONED_DAMAGE_BOX_1 = rectangle(
            x = ANIMATION_X + DAMAGE_BOX_1.x,
            y = ANIMATION_Y + DAMAGE_BOX_1.y,
            width = DAMAGE_BOX_1.width,
            height = DAMAGE_BOX_1.height
        )
        private val POSITIONED_DAMAGE_BOX_2 = rectangle(
            x = ANIMATION_X + DAMAGE_BOX_2.x,
            y = ANIMATION_Y + DAMAGE_BOX_2.y,
            width = DAMAGE_BOX_2.width,
            height = DAMAGE_BOX_2.height
        )
    }
}

private fun DamageFrame.Builder.setDimensions(rectangle: Rectangle) = setDimensions(
    DamageBoxDimensions.newBuilder().setX(rectangle.x.toInt()).setY(rectangle.y.toInt())
        .setWidth(rectangle.width.toInt()).setHeight(rectangle.height.toInt()).build()
)
