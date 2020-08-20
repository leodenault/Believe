package believe.map.tiled

import believe.io.ResourceManager
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.xml.sax.SAXParseException
import java.io.IOException
import java.io.InputStream

internal class ObjectTemplateManagerTest {
    private val resourceManager = mock<ResourceManager> {
        on { getResourceAsStream(VALID_TEMPLATE_LOCATION) } doReturn """
            <?xml version="1.0" encoding="UTF-8"?>
            <template>
             <object type="a type" width="32" height="96"/>
            </template>
        """.trimIndent().byteInputStream()
        on { getResourceAsStream(IO_ERROR_TEMPLATE_LOCATION) } doReturn object : InputStream() {
            override fun read(): Int = throw IOException()
        }
        on { getResourceAsStream(UNPARSABLE_TEMPLATE_LOCATION) } doReturn """
            o372423q5bqt7ob587qct3o45q837t45oc8q3 NOPE
        """.trimIndent().byteInputStream()
        on { getResourceAsStream(INVALID_TEMPLATE_LOCATION) } doReturn """
            <?xml version="1.0" encoding="UTF-8"?>
            <template>
             <notanobject height="96"/>
            </template>
        """.trimIndent().byteInputStream()
    }
    private val manager = ObjectTemplateManager(
        resourceManager, PartialTiledObject.Parser(TileSetGroup {}), MAP_LOCATION
    )

    @Test
    fun getDataFor_returnsCorrespondingObject() {
        with(manager.getDataFor(VALID_TEMPLATE_RELATIVE_LOCATION)!!) {
            assertThat(width).isEqualTo(32f)
            assertThat(height).isEqualTo(96f)
            assertThat(type).isEqualTo("a type")
        }
    }

    @Test
    fun getDataFor_dataAlreadyLoaded_returnsCachedInstance() {
        manager.getDataFor(VALID_TEMPLATE_RELATIVE_LOCATION)
        manager.getDataFor(VALID_TEMPLATE_RELATIVE_LOCATION)

        verify(resourceManager, times(1)).getResourceAsStream(VALID_TEMPLATE_LOCATION)
    }

    @Test
    @VerifiesLoggingCalls
    fun getDataFor_documentFailsToLoad_returnsNullAndLogsError(logSystem: VerifiableLogSystem) {
        assertThat(manager.getDataFor(IO_ERROR_TEMPLATE_RELATIVE_LOCATION)).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.ERROR
        ).hasThrowable(IOException::class.java)
    }

    @Test
    @VerifiesLoggingCalls
    fun getDataFor_documentFailsToParse_returnsNullAndLogsError(logSystem: VerifiableLogSystem) {
        assertThat(manager.getDataFor(UNPARSABLE_TEMPLATE_RELATIVE_LOCATION)).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.ERROR
        ).hasThrowable(SAXParseException::class.java)
    }

    @Test
    @VerifiesLoggingCalls
    fun getDataFor_documentContentsFailToParse_returnsNullAndLogsError(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(manager.getDataFor(INVALID_TEMPLATE_RELATIVE_LOCATION)).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.ERROR
        ).hasPattern(Regex.escape("Could not parse contents of $INVALID_TEMPLATE_LOCATION."))
    }

    companion object {
        private const val MAP_DIRECTORY = "/some/map"
        private const val MAP_LOCATION = "$MAP_DIRECTORY/location.tmx"
        private const val TEMPLATE_DIRECTORY = "templates"

        private const val VALID_TEMPLATE_NAME = "valid.tx"
        private const val VALID_TEMPLATE_RELATIVE_LOCATION =
            "$TEMPLATE_DIRECTORY/$VALID_TEMPLATE_NAME"
        private const val VALID_TEMPLATE_LOCATION =
            "$MAP_DIRECTORY/$VALID_TEMPLATE_RELATIVE_LOCATION"

        private const val IO_ERROR_TEMPLATE_RELATIVE_LOCATION = "$TEMPLATE_DIRECTORY/io_error.tx"
        private const val IO_ERROR_TEMPLATE_LOCATION =
            "$MAP_DIRECTORY/$IO_ERROR_TEMPLATE_RELATIVE_LOCATION"

        private const val UNPARSABLE_TEMPLATE_RELATIVE_LOCATION =
            "$TEMPLATE_DIRECTORY/unparsable.tx"
        private const val UNPARSABLE_TEMPLATE_LOCATION =
            "$MAP_DIRECTORY/$UNPARSABLE_TEMPLATE_RELATIVE_LOCATION"

        private const val INVALID_TEMPLATE_RELATIVE_LOCATION = "$TEMPLATE_DIRECTORY/invalid.tx"
        private const val INVALID_TEMPLATE_LOCATION =
            "$MAP_DIRECTORY/$INVALID_TEMPLATE_RELATIVE_LOCATION"
    }
}
