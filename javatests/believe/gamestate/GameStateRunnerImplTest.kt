package believe.gamestate

import believe.core.display.Graphics
import believe.gamestate.transition.GameStateTransition
import believe.gamestate.transition.GameStateTransition.Listener
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GameStateRunnerImplTest {
    private val nextState: GameState = mock()
    private val leaveTransition: FakeTransition = spy()
    private val enterTransition: FakeTransition = spy()
    private val graphics: Graphics = mock()
    private val gameStateRunner = GameStateRunnerImpl()

    @Test
    fun update_leaveTransitionIsActive_updatesLeaveTransition() {
        gameStateRunner.transitionTo(nextState, leaveTransition, enterTransition)
        gameStateRunner.update(123)
    }

    @Test
    fun update_enterTransitionIsActive_updatesEnterTransition() {
        gameStateRunner.transitionTo(nextState, leaveTransition, enterTransition)
        leaveTransition.transitionEnded()
        gameStateRunner.update(123)

        verify(enterTransition).update(123)
    }

    @Test
    fun update_nextStateIsActive_entersNextStateAndUpdatesNextState() {
        gameStateRunner.transitionTo(nextState, leaveTransition, enterTransition)
        leaveTransition.transitionEnded()
        enterTransition.transitionEnded()
        gameStateRunner.update(123)

        inOrder(nextState) {
            verify(nextState).enter()
            verify(nextState).update(123)
        }
    }

    @Test
    fun transitionTo_leavesPreviousState() {
        gameStateRunner.transitionTo(nextState, leaveTransition, enterTransition)
        leaveTransition.transitionEnded()
        enterTransition.transitionEnded()
        gameStateRunner.transitionTo(nextState, leaveTransition, enterTransition)

        verify(nextState).leave()
    }

    @Test
    fun render_leaveTransitionIsActive_rendersLeaveTransition() {
        gameStateRunner.transitionTo(nextState, leaveTransition, enterTransition)
        gameStateRunner.render(graphics)

        verify(leaveTransition).render(graphics)
    }

    @Test
    fun render_enterTransitionIsActive_rendersEnterTransition() {
        gameStateRunner.transitionTo(nextState, leaveTransition, enterTransition)
        leaveTransition.transitionEnded()
        gameStateRunner.render(graphics)

        verify(enterTransition).render(graphics)
    }

    @Test
    fun render_gameStateIsActive_rendersGameState() {
        gameStateRunner.transitionTo(nextState, leaveTransition, enterTransition)
        leaveTransition.transitionEnded()
        enterTransition.transitionEnded()
        gameStateRunner.render(graphics)

        verify(nextState).render(graphics)
    }

    @Test
    fun exitCurrentState_exitsCurrentStateAndAllowsEnteringNewState() {
        gameStateRunner.exitCurrentState(leaveTransition)
        gameStateRunner.update(123)
        leaveTransition.transitionEnded()
        gameStateRunner.transitionTo(nextState, leaveTransition, enterTransition)
        gameStateRunner.update(234)
        leaveTransition.transitionEnded()
        gameStateRunner.update(345)
        enterTransition.transitionEnded()
        gameStateRunner.update(456)

        inOrder(leaveTransition, enterTransition, nextState) {
            verify(leaveTransition).update(123)
            verify(leaveTransition).update(234)
            verify(enterTransition).update(345)
            verify(nextState).enter()
            verify(nextState).update(456)
        }
    }

    private abstract class FakeTransition : GameStateTransition {
        private val listeners: MutableList<Listener> = mutableListOf()

        override fun addListener(listener: Listener) {
            listeners.add(listener)
        }

        internal fun transitionEnded() = listeners.forEach(Listener::transitionEnded)
    }
}