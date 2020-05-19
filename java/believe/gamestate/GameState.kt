package believe.gamestate

import believe.core.Updatable
import believe.core.display.Renderable
import believe.core.display.RenderableV2

/**
 * A state within a game that can be updated and rendered. This includes transitions.
 *
 * This should represent a single screen with its own independent context.
 */
interface GameState : Updatable, RenderableV2 {
    /** Sets up the game state. */
    fun enter()

    /** Cleans up the game state, such as references to its components. */
    fun leave()
}