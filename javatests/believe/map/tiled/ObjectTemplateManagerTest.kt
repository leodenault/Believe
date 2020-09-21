package believe.map.tiled

import believe.io.ResourceManager
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.map.tiled.testing.fakeElement
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.w3c.dom.Document
import org.xml.sax.SAXParseException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilder

internal class ObjectTemplateManagerTest {
    private val resourceManager = mock<ResourceManager> {
        on { getResourceAsStream(any()) } doAnswer { invocation ->
            when (invocation.arguments[0]) {
                UNLOADABLE_TEMPLATE_LOCATION -> null
                UNPARSABLE_TEMPLATE_LOCATION -> UNPARSABLE_DOCUMENT_STREAM
                INVALID_TEMPLATE_LOCATION -> INVALID_DOCUMENT_STREAM
                else -> VALID_DOCUMENT_STREAM
            }
        }
    }
    private val documentBuilder = mock<DocumentBuilder> {
        on { parse(any<InputStream>()) } doAnswer { invocation ->
            when (invocation.arguments[0]) {
                UNPARSABLE_DOCUMENT_STREAM -> throw SAXParseException("", "", "", 0, 0)
                INVALID_DOCUMENT_STREAM -> INVALID_DOCUMENT
                else -> VALID_DOCUMENT
            }
        }
    }
    private val manager = ObjectTemplateManager(
        resourceManager, documentBuilder, PartialTiledObject.Parser(TileSetGroup {}), MAP_LOCATION
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
        assertThat(manager.getDataFor(UNLOADABLE_TEMPLATE_RELATIVE_LOCATION)).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.ERROR
        )
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
        private val VALID_DOCUMENT_STREAM = ByteArrayInputStream(byteArrayOf(1, 2, 3))
        private val VALID_ELEMENT = fakeElement(
            tagName = "template", children = arrayOf(
                fakeElement(
                    tagName = "object",
                    attributes = arrayOf("width" to "32", "height" to "96", "type" to "a type")
                )
            )
        )
        private val VALID_DOCUMENT = mock<Document> {
            on { documentElement } doReturn VALID_ELEMENT
        }

        private const val UNLOADABLE_TEMPLATE_RELATIVE_LOCATION = "$TEMPLATE_DIRECTORY/io_error.tx"
        private const val UNLOADABLE_TEMPLATE_LOCATION =
            "$MAP_DIRECTORY/$UNLOADABLE_TEMPLATE_RELATIVE_LOCATION"

        private const val UNPARSABLE_TEMPLATE_RELATIVE_LOCATION =
            "$TEMPLATE_DIRECTORY/unparsable.tx"
        private const val UNPARSABLE_TEMPLATE_LOCATION =
            "$MAP_DIRECTORY/$UNPARSABLE_TEMPLATE_RELATIVE_LOCATION"
        private val UNPARSABLE_DOCUMENT_STREAM = ByteArrayInputStream(byteArrayOf(2, 3, 4))

        private const val INVALID_TEMPLATE_RELATIVE_LOCATION = "$TEMPLATE_DIRECTORY/invalid.tx"
        private const val INVALID_TEMPLATE_LOCATION =
            "$MAP_DIRECTORY/$INVALID_TEMPLATE_RELATIVE_LOCATION"
        private val INVALID_DOCUMENT_STREAM = ByteArrayInputStream(byteArrayOf(3, 4, 5))
        private val INVALID_ELEMENT = fakeElement(tagName = "template")
        private val INVALID_DOCUMENT = mock<Document> {
            on { documentElement } doReturn INVALID_ELEMENT
        }
    }
}
