package believe.map.tiled

import believe.io.ResourceManager
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.map.tiled.Tile.GridTileFactory
import believe.map.tiled.Tile.SingleImageTileParser
import believe.map.tiled.testing.fakeMultiImageTileSet
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.w3c.dom.Document
import org.xml.sax.SAXParseException
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilder

internal class TileSetManagerTest {
    private val resourceManager = mock<ResourceManager> {
        on { getResourceAsStream(VALID_TILE_SET_LOCATION) } doReturn VALID_TILE_SET_STREAM
        on { getResourceAsStream(UNLOADABLE_TILE_SET_LOCATION) } doReturn null
        on { getResourceAsStream(UNPARSABLE_TILE_SET_LOCATION) } doReturn UNPARSABLE_TILE_SET_STREAM
    }
    private val documentBuilder = mock<DocumentBuilder> {
        on { parse(VALID_TILE_SET_STREAM) } doReturn VALID_TILE_SET_DOCUMENT
        on { parse(UNPARSABLE_TILE_SET_STREAM) } doThrow SAXParseException("", "", "", 0, 0)
    }
    private val loadImage = { _: String -> null }
    private val partialTileSetParser = PartialTileSet.Parser(
        GridTileFactory(), SingleImageTileParser(loadImage), loadImage, isHeadless = true
    )
    private val tileSetManager =
        TileSetManager(resourceManager, documentBuilder, partialTileSetParser)

    @Test
    fun getDataFor_correctlyReturnsTileSet() {
        val tileSet: TileSet? = tileSetManager.getDataFor(VALID_TILE_SET_LOCATION)

        assertThat(tileSet?.name).isEqualTo(VALID_TILE_SET_NAME)
    }

    @Test
    fun getDataFor_cachesTileSets() {
        tileSetManager.getDataFor(VALID_TILE_SET_LOCATION)
        tileSetManager.getDataFor(VALID_TILE_SET_LOCATION)

        verify(resourceManager, times(1)).getResourceAsStream(any())
    }

    @Test
    @VerifiesLoggingCalls
    fun getDataFor_tileSetFailsToLoad_logsErrorAndReturnsNull(logSystem: VerifiableLogSystem) {
        assertThat(tileSetManager.getDataFor(UNLOADABLE_TILE_SET_LOCATION)).isNull()

        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.ERROR
        ).containsExactly(
            "Failed to load tile set at '$UNLOADABLE_TILE_SET_LOCATION'."
        )
    }

    @Test
    @VerifiesLoggingCalls
    fun getDataFor_tileSetFailsToParse_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(tileSetManager.getDataFor(UNPARSABLE_TILE_SET_LOCATION)).isNull()

        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.ERROR
        ).containsExactly("Failed to parse tile set at '$UNPARSABLE_TILE_SET_LOCATION'.")
    }

    companion object {
        private const val VALID_TILE_SET_LOCATION = "/some/tile/set.tsx"
        private const val UNLOADABLE_TILE_SET_LOCATION = "/i/cannot/load/this.tsx"
        private const val UNPARSABLE_TILE_SET_LOCATION = "/i/cannot/parse/this.tsx"
        private val VALID_TILE_SET_STREAM = ByteArrayInputStream(byteArrayOf(1, 2, 3))
        private val UNPARSABLE_TILE_SET_STREAM = ByteArrayInputStream(byteArrayOf(2, 3, 4))
        private const val VALID_TILE_SET_NAME = "valid tile set name"
        private val VALID_TILE_SET_ELEMENT = fakeMultiImageTileSet(name = VALID_TILE_SET_NAME)
        private val UNREADABLE_TILE_SET_ELEMENT = fakeMultiImageTileSet(name = "cannot read")
        private val VALID_TILE_SET_DOCUMENT = mock<Document> {
            on { documentElement } doReturn VALID_TILE_SET_ELEMENT
        }
    }
}
