package believe.app

import javax.inject.Qualifier

/** Annotates a set of strings as the paths to the native libraries used by the application. */
@Qualifier
annotation class NativeLibraryPaths {}