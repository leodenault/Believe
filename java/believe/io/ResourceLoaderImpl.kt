package believe.io

import org.newdawn.slick.util.ResourceLocation
import java.io.InputStream
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ResourceLoaderImpl @Inject internal constructor(
    resourceLocations: Set<ResourceLocation>
) : ResourceLoader {
    init {
        org.newdawn.slick.util.ResourceLoader.removeAllResourceLocations()
        resourceLocations.forEach { org.newdawn.slick.util.ResourceLoader.addResourceLocation(it) }
    }

    override fun getResourceAsStream(ref: String): InputStream {
        return org.newdawn.slick.util.ResourceLoader.getResourceAsStream(ref)
    }

    override fun resourceExists(ref: String): Boolean {
        return org.newdawn.slick.util.ResourceLoader.resourceExists(ref)
    }

    override fun getResource(ref: String): URL {
        return org.newdawn.slick.util.ResourceLoader.getResource(ref)
    }
}