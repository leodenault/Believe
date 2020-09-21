package believe.map.tiled

import believe.gui.testing.FakeImage
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.map.tiled.Tile.GridTileFactory
import believe.map.tiled.Tile.SingleImageTileParser
import believe.map.tiled.testing.Truth
import believe.map.tiled.testing.fakeElement
import believe.map.tiled.testing.fakeProperties
import com.google.common.truth.Correspondence
import com.google.common.truth.Correspondence.BinaryPredicate
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet
import kotlin.reflect.jvm.jvmName

internal class TileTest {
    private val loadImage = mock<(String) -> Image?>()
    private val gridTileParser = GridTileFactory()
    private val singleImageTileParser = SingleImageTileParser(loadImage)

    @Test
    fun parseGridTile_spriteSheetExists_correctlyParsesTilesInAGrid() {
        val spriteSheet = mock<SpriteSheet> {
            on { horizontalCount } doReturn 3
            on { verticalCount } doReturn 2
        }

        val tiles: List<Tile> = gridTileParser.create(
            tileWidth = 10, tileHeight = 20, spriteSheet = spriteSheet, element = fakeElement(
                children = arrayOf(
                    fakeElement(
                        tagName = "tile",
                        attributes = arrayOf("id" to "1", "type" to "super type"),
                        children = arrayOf(
                            fakeProperties("prop1" to "val1", "prop2" to "val2")
                        )
                    ), fakeElement(
                        tagName = "tile", attributes = arrayOf("id" to "2", "type" to "mega type")
                    )
                )
            )
        )
        tiles.forEach { it.render(12f, 13f) }

        assertThat(tiles).comparingElementsUsing(TILE_CORRESPONDENCE)
            .displayingDiffsPairedBy { it?.id }.containsExactly(
                fakeTile(id = 0, width = 10, height = 20),
                fakeTile(
                    id = 1,
                    type = "super type",
                    width = 10,
                    height = 20,
                    properties = Properties.fromMap(
                        mapOf("prop1" to "val1", "prop2" to "val2")
                    )
                ),
                fakeTile(id = 2, type = "mega type", width = 10, height = 20),
                fakeTile(id = 3, width = 10, height = 20),
                fakeTile(id = 4, width = 10, height = 20),
                fakeTile(id = 5, width = 10, height = 20)
            )
        inOrder(spriteSheet) {
            verify(spriteSheet).renderInUse(12, 13, 0, 0)
            verify(spriteSheet).renderInUse(12, 13, 1, 0)
            verify(spriteSheet).renderInUse(12, 13, 2, 0)
            verify(spriteSheet).renderInUse(12, 13, 0, 1)
            verify(spriteSheet).renderInUse(12, 13, 1, 1)
            verify(spriteSheet).renderInUse(12, 13, 2, 1)
        }
    }

    @Test
    fun parseHeadlessGridTile_spriteSheetIsNull_correctlyParsesTilesInAGrid() {
        val tiles: List<Tile> = gridTileParser.createHeadless(
            tileWidth = 10, tileHeight = 20, tileCount = 6, element = fakeElement(
                children = arrayOf(
                    fakeElement(
                        tagName = "tile",
                        attributes = arrayOf("id" to "1", "type" to "super type"),
                        children = arrayOf(
                            fakeProperties("prop1" to "val1", "prop2" to "val2")
                        )
                    )
                )
            )
        )

        assertThat(tiles).comparingElementsUsing(TILE_CORRESPONDENCE)
            .displayingDiffsPairedBy { it?.id }.containsExactly(
                fakeTile(id = 0, width = 10, height = 20),
                fakeTile(
                    id = 1,
                    type = "super type",
                    width = 10,
                    height = 20,
                    properties = Properties.fromMap(
                        mapOf("prop1" to "val1", "prop2" to "val2")
                    )
                ),
                fakeTile(id = 2, width = 10, height = 20),
                fakeTile(id = 3, width = 10, height = 20),
                fakeTile(id = 4, width = 10, height = 20),
                fakeTile(id = 5, width = 10, height = 20)
            )
    }

    @Test
    fun parseSingleImageTile_isNotHeadless_correctlyParsesTilesWithIndividualImages() {
        whenever(loadImage.invoke(any())) doReturn FakeImage()

        val tile = singleImageTileParser.parse(
            tileSetDirectory = "/some/directory", element = fakeElement(
                attributes = arrayOf("id" to "2", "type" to "mega type"), children = arrayOf(
                    fakeElement(
                        tagName = "image", attributes = arrayOf(
                            "width" to "100", "height" to "200", "source" to "some/place.png"
                        )
                    ), fakeProperties("prop1" to "val1", "prop2" to "val2")
                )
            )
        )

        with(tile) {
            assertThat(id).isEqualTo(2)
            assertThat(type).isEqualTo("mega type")
            assertThat(width).isEqualTo(100)
            assertThat(height).isEqualTo(200)
            Truth.assertThat(properties).containsAll("prop1" to "val1", "prop2" to "val2")
        }
        verify(loadImage).invoke("/some/directory/some/place.png")
    }

    @Test
    @VerifiesLoggingCalls
    fun parseSingleImageTile_isNotHeadless_imageFailsToLoad_logsError(
        logSystem: VerifiableLogSystem
    ) {
        whenever(loadImage.invoke(any())) doReturn null

        val tile = singleImageTileParser.parse(
            tileSetDirectory = "/some/directory", element = fakeElement(
                attributes = arrayOf("id" to "2", "type" to "mega type"), children = arrayOf(
                    fakeElement(
                        tagName = "image", attributes = arrayOf(
                            "width" to "100", "height" to "200", "source" to "some/place.png"
                        )
                    ), fakeProperties("prop1" to "val1", "prop2" to "val2")
                )
            )
        )

        with(tile) {
            assertThat(id).isEqualTo(2)
            assertThat(type).isEqualTo("mega type")
            assertThat(width).isEqualTo(100)
            assertThat(height).isEqualTo(200)
            Truth.assertThat(properties).containsAll("prop1" to "val1", "prop2" to "val2")
        }
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.ERROR
        ).containsExactly("Could not load tile image at '/some/directory/some/place.png'.")
    }

    @Test
    fun parseSingleImageTile_isHeadless_correctlyParsesTilesWithIndividualImages() {
        val tile = singleImageTileParser.parseHeadless(
            element = fakeElement(
                attributes = arrayOf("id" to "2", "type" to "mega type"), children = arrayOf(
                    fakeElement(
                        tagName = "image", attributes = arrayOf("width" to "100", "height" to "200")
                    ), fakeProperties("prop1" to "val1", "prop2" to "val2")
                )
            )
        )

        with(tile) {
            assertThat(id).isEqualTo(2)
            assertThat(type).isEqualTo("mega type")
            assertThat(width).isEqualTo(100)
            assertThat(height).isEqualTo(200)
            Truth.assertThat(properties).containsAll("prop1" to "val1", "prop2" to "val2")
        }
        verifyZeroInteractions(loadImage)
    }
}

private fun fakeTile(
    id: Int,
    type: String? = null,
    width: Int,
    height: Int,
    properties: Properties = Properties.empty()
) = object : Tile {
    override val id: Int = id
    override val type: String? = type
    override val width: Int = width
    override val height: Int = height
    override val properties: Properties = properties

    override fun render(x: Float, y: Float) {}
}

private val TILE_CORRESPONDENCE = Correspondence.from(
    BinaryPredicate<Tile, Tile> { a, b ->
        (a == null && b == null) || (a != null && b != null && a.id == b.id && a.width == b.width && a.height == b.height && a.type == b.type && a.properties == b.properties)
    }, "equals"
).formattingDiffsUsing { a, b ->
    """
    id: ${a?.id} -> ${b?.id}
    type: ${a?.type} -> ${b?.type}
    width: ${a?.width} -> ${b?.width}
    height: ${a?.height} -> ${b?.height}
    properties: ${a?.properties} -> ${b?.properties}
    """.trimIndent()
}
