package believe.gamestate

import believe.core.Updatable
import believe.core.display.Renderable
import believe.gamestate.transition.GameStateTransition

/** Runs a game state, changing the active state when instructed. */
interface GameStateRunner : Updatable, Renderable {
    /**
     * Transitions the current state to [gameState] by first running through [leaveTransition] then
     * [enterTransition].
     *
     * It is expected that a state controller will make the appropriate calls to this method.
     */
    fun transitionTo(
        gameState: GameState,
        leaveTransition: GameStateTransition,
        enterTransition: GameStateTransition
    )
}