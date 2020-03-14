package believe.gamestate

import believe.gamestate.transition.GameStateTransition
import believe.gamestate.transition.GameStateTransition.Listener
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.newdawn.slick.Graphics

internal class GameStateRunnerImplTest {
    private val gameState: GameState = mock()
    private val leaveTransition: FakeTransition = spy()
    private val enterTransition: FakeTransition = spy()
    private val graphics: Graphics = mock()
    private val gameStateRunner = GameStateRunnerImpl()

    @BeforeEach
    internal fun setUp() {
        gameStateRunner.transitionTo(gameState, leaveTransition, enterTransition)
    }

    @Test
    fun update_leaveTransitionIsActive_updatesLeaveTransition() {
        gameStateRunner.update(123)

        verify(leaveTransition).update(123)
    }

    @Test
    fun update_enterTransitionIsActive_updatesEnterTransition() {
        leaveTransition.transitionEnded()
        gameStateRunner.update(123)

        verify(enterTransition).update(123)
    }

    @Test
    fun update_gameStateIsActive_updatesGameState() {
        leaveTransition.transitionEnded()
        enterTransition.transitionEnded()
        gameStateRunner.update(123)

        verify(gameState).update(123)
    }

    @Test
    fun render_leaveTransitionIsActive_rendersLeaveTransition() {
        gameStateRunner.render(graphics)

        verify(leaveTransition).render(graphics)
    }

    @Test
    fun render_enterTransitionIsActive_rendersEnterTransition() {
        leaveTransition.transitionEnded()
        gameStateRunner.render(graphics)

        verify(enterTransition).render(graphics)
    }

    @Test
    fun render_gameStateIsActive_rendersGameState() {
        leaveTransition.transitionEnded()
        enterTransition.transitionEnded()
        gameStateRunner.render(graphics)

        verify(gameState).render(graphics)
    }

    private abstract class FakeTransition : GameStateTransition {
        private val listeners: MutableList<Listener> = mutableListOf()

        override fun addListener(listener: Listener) {
            listeners.add(listener)
        }

        internal fun transitionEnded() = listeners.forEach(Listener::transitionEnded)
    }
}