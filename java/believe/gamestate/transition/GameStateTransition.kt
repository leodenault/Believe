package believe.gamestate.transition

import believe.core.Updatable
import believe.core.display.RenderableV2

/** A transition effect executed either after leaving a state or before entering a state. */
interface GameStateTransition : Updatable, RenderableV2 {
    /** An object that listens for the end of a [GameStateTransition]. */
    interface Listener {
        /** Notifies this listener that the transition has ended. */
        fun transitionEnded()
    }

    /**
     * Adds a [Listener] to this transition that will be notified once the transition has ended.
     */
    fun addListener(listener: Listener)
}