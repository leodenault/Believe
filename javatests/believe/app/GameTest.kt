package believe.app

import believe.gamestate.GameStateRunner
import believe.testing.FakeGameContainer
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.jupiter.api.Test
import org.newdawn.slick.Graphics

internal class GameTest {
    private val gameStateRunner: GameStateRunner = mock()
    private val graphics: Graphics = mock()
    private val game: Game = Game(GAME_TITLE, gameStateRunner)
    private val fakeGameContainer: FakeGameContainer = FakeGameContainer(game)

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