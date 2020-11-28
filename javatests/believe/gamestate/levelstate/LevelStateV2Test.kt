package believe.gamestate.levelstate

import believe.core.display.Graphics
import believe.gamestate.GameStateRunner
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class LevelStateV2Test {
    private val stateRunner = mock<GameStateRunner>()
    private val stateController = mock<LevelStateController>()
    private val state = LevelStateV2(stateRunner, stateController, LEVEL_NAME)

    @Test
    fun enter_entersRunningGameState() {
        state.enter()

        verify(stateController).navigateToRunningGameState(LEVEL_NAME)
    }

    @Test
    fun update_updatesLevelRunner() {
        state.update(123)

        verify(stateRunner).update(123)
    }

    @Test
    fun render_rendersLevelRunner() {
        val graphics = mock<Graphics>()

        state.render(graphics)

        verify(stateRunner).render(graphics)
    }

    companion object {
        private const val LEVEL_NAME = "some level"
    }
}
