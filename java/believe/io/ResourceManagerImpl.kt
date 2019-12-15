package believe.io

import org.newdawn.slick.util.FileSystemLocation
import org.newdawn.slick.util.Log
import org.newdawn.slick.util.ResourceLoader
import org.newdawn.slick.util.ResourceLocation
import java.io.*
import java.lang.RuntimeException
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ResourceManagerImpl @Inject internal constructor(
    fileResourceLocation: FileSystemLocation,
    resourceLocations: Set<@JvmSuppressWildcards ResourceLocation>
) : ResourceManager {

    init {
        ResourceLoader.removeAllResourceLocations()
        ResourceLoader.addResourceLocation(fileResourceLocation)
        resourceLocations.forEach { ResourceLoader.addResourceLocation(it) }
    }

    override fun getResourceAsStream(ref: String): InputStream? {
        return try {
            ResourceLoader.getResourceAsStream(ref)
        } catch (e: RuntimeException) {
            Log.error("Could not read resource at '$ref'.", e)
            null
        }
    }

    override fun resourceExists(ref: String): Boolean {
        return ResourceLoader.resourceExists(ref)
    }

    override fun getResource(ref: String): URL? {
        return try {
            ResourceLoader.getResource(ref)
        } catch (e: RuntimeException) {
            Log.error("Could not find resource at '$ref'.", e)
            null
        }
    }

    override fun getOutputStreamToFileResource(ref: String): OutputStream? {
        val file = File(ref)
        return try {
            FileOutputStream(file)
        } catch (e: IOException) {
            Log.error("Could not write to resource found at '$ref'.", e)
            null
        }
    }
}
