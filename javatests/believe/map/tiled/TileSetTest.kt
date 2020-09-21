package believe.map.tiled

import believe.datamodel.DataManager
import believe.map.tiled.testing.fakeElement
import believe.map.tiled.testing.fakeGridTileSet
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test

internal class TileSetTest {
    private val tileSetManager = mock<DataManager<PartialTileSet>> {
        on { getDataFor("$MAP_DIRECTORY/$TILE_SET_LOCATION") } doReturn PARTIAL_TILE_SET
    }
    private val loadImage: (String) -> Nothing? = { null }
    private val parseTileSet = TileSet.Parser(
        PartialTileSet.Parser(
            Tile.GridTileFactory(),
            Tile.SingleImageTileParser(loadImage),
            loadImage,
            isHeadless = true
        ), tileSetManager, tiledMapLocation = "$MAP_DIRECTORY/tiled_map.tmx"
    )

    @Test
    fun parse_sourceDoesNotExist_correctlyParsesEmbeddedTileSet() {
        val tileSet: TileSet? = parseTileSet(
            fakeGridTileSet(
                name = "coolest tile set ever",
                tileWidth = 32,
                tileHeight = 64,
                firstGid = 12,
                columns = 1,
                tileCount = 1,
                children = arrayOf(
                    fakeElement(tagName = "tile", attributes = arrayOf("id" to "2")), fakeElement(
                        tagName = "image", attributes = arrayOf("source" to "something.png")
                    )
                )
            )
        )

        with(tileSet!!) {
            assertThat(name).isEqualTo("coolest tile set ever")
            assertThat(firstGid).isEqualTo(12)
            assertThat(tileSet[12]!!.id).isEqualTo(0)
        }
    }

    @Test
    fun parse_sourceExists_correctlyParsesExternalTileSet() {
        val tileSet: TileSet? = parseTileSet(
            fakeElement(
                tagName = "tileset",
                attributes = arrayOf("firstgid" to "123", "source" to TILE_SET_LOCATION)
            )
        )

        assertThat(tileSet).isEqualTo(PARTIAL_TILE_SET)
        assertThat(tileSet!!.firstGid).isEqualTo(123)
    }

    companion object {
        private const val MAP_DIRECTORY = "/the/location/of/the"
        private const val TILE_SET_LOCATION = "some/tile/set.tsx"
        private val PARTIAL_TILE_SET = object : PartialTileSet {
            override var firstGid: Int = 0
            override val name: String = "a name"

            override fun get(gid: Int): Tile? = null
            override fun contains(gid: Int): Boolean = false
            override fun renderBatch(x: Float, y: Float, tileRenderingBatch: TileRenderingBatch) {}
        }
    }
}
