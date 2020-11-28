package believe.app

import javax.inject.Qualifier

/** Qualifies a [GameStateRunner] that managers states at the application level. */
@Qualifier
annotation class ApplicationGameStateRunner

/** Annotates a set of strings as the paths to the native libraries used by the application. */
@Qualifier
annotation class NativeLibraryPaths {}