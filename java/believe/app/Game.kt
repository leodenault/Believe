package believe.app

import believe.gamestate.GameStateRunner
import believe.gamestate.StateController
import dagger.Lazy
import dagger.Reusable
import org.newdawn.slick.Game
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import javax.inject.Inject

/** The base class for a game.  */
@Reusable
class Game @Inject constructor(
    @ApplicationTitle
    private val applicationTitle: String, private val gameStateRunner: GameStateRunner,
    private val stateController: Lazy<StateController>,
    private val stateSelector: StateSelector
) : Game {

    override fun init(container: GameContainer) = stateSelector.selectState(stateController.get())

    override fun update(container: GameContainer, delta: Int) = gameStateRunner.update(delta)

    override fun render(container: GameContainer, g: Graphics) = gameStateRunner.render(g)

    override fun closeRequested(): Boolean = true

    override fun getTitle(): String = applicationTitle
}