package believe.app

import org.newdawn.slick.util.Log
import javax.inject.Inject

/** Utilities used at the application level.  */
class EnvironmentConfigurator @Inject internal constructor(
    @NativeLibraryPaths private val nativeLibraryPaths: Set<String>
) {

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun configure() {
        val javaLibraryPathField = ClassLoader::class.java.getDeclaredField("usr_paths")
        javaLibraryPathField.isAccessible = true
        // Safe cast by contract.
        @Suppress("UNCHECKED_CAST") val javaLibraryPaths =
            javaLibraryPathField[null] as Array<String>
        val fullPathsList: Set<String> = nativeLibraryPaths + javaLibraryPaths
        Log.info("Resetting java.library.path to contain natives.")
        javaLibraryPathField[null] = fullPathsList.toTypedArray()
    }
}