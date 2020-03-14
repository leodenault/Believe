package believe.gamestate

import believe.core.Updatable
import believe.core.display.Renderable

/**
 * A state within a game that can be updated and rendered. This includes transitions.
 *
 * This should represent a single screen with its own independent context.
 */
interface GameState : Updatable, Renderable