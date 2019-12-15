package believe.io

import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.testing.temporaryfolder.TemporaryFolder
import believe.testing.temporaryfolder.UsesTemporaryFolder
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.newdawn.slick.util.FileSystemLocation
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL

internal class ResourceManagerImplTest {
    private val resourceManager = ResourceManagerImpl(FakeResourceLocation(), setOf())

    @Test
    internal fun getResourceAsStream_returnsValidInputStream() {
        assertThat(resourceManager.getResourceAsStream(LOCATION)?.read()).isEqualTo(STREAM_DATA)
    }

    @Test
    @VerifiesLoggingCalls
    internal fun getResourceAsStream_resourceNotFound_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(resourceManager.getResourceAsStream("not the right resource")).isNull()

        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Could not read resource at 'not the right resource'.")
            .hasSeverity(LogSeverity.ERROR).hasThrowable(RuntimeException::class.java)
    }

    @Test
    internal fun resourceExists_exists_returnsTrue() {
        assertThat(resourceManager.resourceExists(LOCATION)).isTrue()
    }

    @Test
    internal fun resourceExists_doesNotExist_returnsFalse() {
        assertThat(resourceManager.resourceExists("not the right resource")).isFalse()
    }

    @Test
    internal fun getResource_returnsValidUrl() {
        assertThat(resourceManager.getResource(LOCATION)).isEqualTo(RESOURCE_URL)
    }

    @Test
    @VerifiesLoggingCalls
    internal fun getResource_resourceNotFound_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(resourceManager.getResource("not the right resource")).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Could not find resource at 'not the right resource'.")
            .hasSeverity(LogSeverity.ERROR).hasThrowable(RuntimeException::class.java)
    }

    @Test
    @UsesTemporaryFolder
    internal fun getOutputStreamToFileResource_returnsValidOutputStream(folder: TemporaryFolder) {
        folder.writeToFile("/some/file.png").close()

        val outputStream =
            resourceManager.getOutputStreamToFileResource(folder.getPathToFile("/some/file.png"))
        outputStream?.write(123)
        outputStream?.close()

        assertThat(outputStream).isNotNull()
        val inputStream = folder.readFile("/some/file.png")
        assertThat(inputStream.read()).isEqualTo(123)
        assertThat(inputStream.read()).isEqualTo(-1)
        inputStream.close()
    }

    @Test
    @VerifiesLoggingCalls
    @UsesTemporaryFolder
    internal fun getOutputStreamToFileResource_errorWhenOpeningStream_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem, folder: TemporaryFolder
    ) {
        val fileLocation = folder.getPathToFile("/some_dir")
        File(fileLocation).mkdir()
        val outputStream = resourceManager.getOutputStreamToFileResource(fileLocation)

        assertThat(outputStream).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Could not write to resource found at '$fileLocation'.")
            .hasSeverity(LogSeverity.ERROR)
            .hasThrowable(FileNotFoundException::class.java)
    }

    companion object {
        private val LOCATION: String = "/some/location.png"
        private val STREAM_DATA: Int = 123
        private val INPUT_STREAM: InputStream = object : InputStream() {
            override fun read(): Int = STREAM_DATA
        }
        private val RESOURCE_URL = URL("file://$LOCATION")

        private class FakeResourceLocation : FileSystemLocation(null) {
            override fun getResourceAsStream(ref: String): InputStream? {
                return if (ref == LOCATION) INPUT_STREAM else null
            }

            override fun getResource(ref: String): URL? {
                return if (ref == LOCATION) RESOURCE_URL else null
            }
        }
    }
}