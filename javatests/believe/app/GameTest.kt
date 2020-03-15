package believe.app

import believe.gamestate.GameStateRunner
import believe.gamestate.StateController
import believe.testing.FakeGameContainer
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import dagger.Lazy
import org.junit.jupiter.api.Test
import org.newdawn.slick.Graphics

internal class GameTest {
    private val gameStateRunner: GameStateRunner = mock()
    private val graphics: Graphics = mock()
    private val stateController: StateController = mock()
    private val stateSelector: StateSelector = mock()
    private val game: Game =
        Game(GAME_TITLE, gameStateRunner, Lazy { stateController }, stateSelector)
    private val fakeGameContainer: FakeGameContainer = FakeGameContainer(game)

    @Test
    internal fun init_runsStateSelector() {
        game.init(fakeGameContainer)

        verify(stateSelector).selectState(stateController)
    }

    @Test
    internal fun update_updatesGameStateRunner() {
        game.update(fakeGameContainer, delta = 123)

        verify(gameStateRunner).update(/* delta= */ 123)
        verifyNoMoreInteractions(gameStateRunner)
    }

    @Test
    internal fun render_rendersGameStateRunner() {
        game.render(fakeGameContainer, graphics)

        verify(gameStateRunner).render(graphics)
        verifyNoMoreInteractions(gameStateRunner)
    }

    @Test
    internal fun closeRequested_returnsTrue() {
        assertThat(game.closeRequested()).isTrue()
    }

    @Test
    internal fun title_returnsTitle() {
        assertThat(game.title).isEqualTo(GAME_TITLE)
    }

    companion object {
        private val GAME_TITLE = "Game Title"
    }
}