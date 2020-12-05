package believe.gamestate.levelstate

import javax.inject.Qualifier

/** Qualifies the [LevelStateRunner] used to execute the substates of [LevelStateV2]. */
@Qualifier
annotation class LevelStateRunner

/** Qualifies a string representing the name of the level currently being loaded and played. */
@Qualifier
annotation class LevelName
