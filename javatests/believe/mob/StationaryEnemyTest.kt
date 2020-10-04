package believe.mob

import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.fakeAnimation
import believe.animation.testing.frameAt
import believe.core.display.Graphics
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StationaryEnemyTest {
    private val idleAnimation = fakeAnimation(IterationMode.LINEAR, true, 2, 2, 2)
    private val attackAnimation = fakeAnimation(IterationMode.PING_PONG, false, 3, 3, 3, 3)
    private val stationaryEnemy = StationaryEnemy(
        123f, 234f, StationaryEnemyStateMachine(idleAnimation, attackAnimation)
    )

    @BeforeEach
    fun setUp() {
        stationaryEnemy.bind()
    }

    @Test
    fun render_rendersCurrentAnimation() {
        val graphics = mock<Graphics>()

        stationaryEnemy.render(graphics)

        verify(graphics).drawAnimation(idleAnimation, 123f, 234f)
    }

    @Test
    fun update_updatesCurrentAnimation() {
        val graphics = mock<Graphics>()

        stationaryEnemy.update(5)
        stationaryEnemy.render(graphics)

        verify(graphics).drawAnimation(idleAnimation, 123f, 234f)

        stationaryEnemy.update(13)
        stationaryEnemy.render(graphics)

        verify(graphics).drawAnimation(attackAnimation, 123f, 234f)
    }
}
