package believe.io

import java.io.InputStream
import java.io.OutputStream
import java.net.URL

/**
 * Wrapper around [org.newdawn.slick.util.ResourceLoader] for easy injection and added
 * functionality.
 */
interface ResourceManager {
    /** Returns an [InputStream] streaming the contents of the resource found at [ref]. */
    fun getResourceAsStream(ref: String): InputStream?

    /** Returns true if a resource exists at [ref]. */
    fun resourceExists(ref: String): Boolean

    /** Returns a [URL] to the resource located at [ref]. */
    fun getResource(ref: String): URL?

    /** Returns an [OutputStream] to [ref] for writing data to a file on disk. */
    fun getOutputStreamToFileResource(ref: String) : OutputStream?
}