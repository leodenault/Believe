package believe.gamestate.transition

import believe.gamestate.transition.GameStateTransition.Listener
import org.newdawn.slick.Graphics

/**
 * A [GameStateTransition] that does nothing and that immediately notifies of the end of the
 * transition when [update] is called.
 */
class EmptyGameStateTransition : GameStateTransition {
    private val listeners: MutableList<Listener> = mutableListOf()

    override fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun update(delta: Int) {
        listeners.forEach(Listener::transitionEnded)
    }

    override fun render(g: Graphics?) {}
}