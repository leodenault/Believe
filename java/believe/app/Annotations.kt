package believe.app

import javax.inject.Qualifier

/** Qualifier for the title of the application.  */
@Qualifier
annotation class ApplicationTitle

/**
 * Qualifies a [believe.gamestate.GameStateBase] instance as the first state that should be
 * run in the app.
 */
@Qualifier
annotation class FirstState

/**
 * Qualifies a set of [believe.gamestate.GameStateBase] instances that will be called at startup.
 */
@Qualifier
annotation class GameStates
