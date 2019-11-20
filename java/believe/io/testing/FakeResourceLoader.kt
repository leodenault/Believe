package believe.io.testing

import believe.io.ResourceLoader
import java.io.InputStream
import java.net.URL

/** A fake [ResourceLoader] used in testing. */
class FakeResourceLoader : ResourceLoader {
    override fun getResourceAsStream(ref: String): InputStream {
        return object : InputStream() {
            override fun read(): Int = -1
        }
    }

    override fun resourceExists(ref: String): Boolean = false

    override fun getResource(ref: String): URL = URL("invalid")
}