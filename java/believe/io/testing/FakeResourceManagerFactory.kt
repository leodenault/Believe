package believe.io.testing

import believe.io.ResourceManager
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

/** A factory for creating fake instances of [ResourceManager] used in testing. */
object FakeResourceManagerFactory {
    /** Creates a no-op implementation of [ResourceManager]. */
    @JvmStatic
    fun createNoOp(): ResourceManager {
        return object : ResourceManager {
            override fun getResourceAsStream(ref: String): InputStream? = null

            override fun resourceExists(ref: String): Boolean = false

            override fun getResource(ref: String): URL? = null

            override fun getOutputStreamToFileResource(ref: String): OutputStream? = null
        }
    }

    /**
     * Creates a [ResourceManager] that returns [inputStream] when calling
     * [ResourceManager.getResourceAsStream].
     */
    @JvmStatic
    fun managerReadingFrom(inputStream: InputStream): ResourceManager {
        return object : ResourceManager {
            override fun getResourceAsStream(ref: String): InputStream? = inputStream

            override fun resourceExists(ref: String): Boolean = true

            override fun getResource(ref: String): URL? = null

            override fun getOutputStreamToFileResource(ref: String): OutputStream? = null
        }
    }

    /**
     * Creates a [ResourceManager] that returns [outputStream] when calling
     * [ResourceManager.getOutputStreamToFileResource].
     */
    @JvmStatic
    fun managerWritingTo(outputStream: OutputStream): ResourceManager {
        return object : ResourceManager {
            override fun getResourceAsStream(ref: String): InputStream? = null

            override fun resourceExists(ref: String): Boolean = false

            override fun getResource(ref: String): URL? = null

            override fun getOutputStreamToFileResource(ref: String): OutputStream? = outputStream
        }
    }
}