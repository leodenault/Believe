package believe.map.tiled

import believe.gui.testing.FakeImage
import believe.map.tiled.Tile.GridTileFactory
import believe.map.tiled.Tile.SingleImageTileParser
import believe.map.tiled.testing.fakeGridTile
import believe.map.tiled.testing.fakeGridTileSet
import believe.map.tiled.testing.fakeMultiImageTileSet
import believe.map.tiled.testing.fakeSingleImageTile
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class PartialTileSetTest {
    private val gridTileFactory = GridTileFactory()
    private val singleImageTileParser = SingleImageTileParser { null }
    private val partialTileSetParser = PartialTileSet.Parser(
        gridTileFactory, singleImageTileParser, {
            FakeImage(
                width = GRID_TILE_COLUMNS * GRID_TILE_WIDTH,
                height = GRID_TILE_ROWS * GRID_TILE_HEIGHT
            )
        }, isHeadless = false
    )
    private val headlessPartialTileSetParser = PartialTileSet.Parser(
        gridTileFactory, singleImageTileParser, { null }, isHeadless = true
    )

    @Test
    fun parse_isGridTileSet_correctlyParsesTileSet() {
        val tileSet: PartialTileSet = partialTileSetParser.parse(
            "/the/top/dir", fakeGridTileSet(
                name = "the tile set",
                tileWidth = GRID_TILE_WIDTH,
                tileHeight = GRID_TILE_HEIGHT,
                columns = GRID_TILE_COLUMNS,
                tileCount = GRID_TILE_COUNT,
                children = arrayOf(
                    fakeGridTile(id = 0, type = "tile type 1"),
                    fakeGridTile(id = 1, type = "tile type 2")
                )
            )
        ).also { it.firstGid = 123 }

        with(tileSet) {
            assertThat(name).isEqualTo("the tile set")
            assertThat(firstGid).isEqualTo(123)
            assertThat(contains(123)).isTrue()
            assertThat(contains(124)).isTrue()
        }
        val tileList = (123 until GRID_TILE_COUNT + 123).map { tileSet[it]!! }
        with(tileList[0]) {
            assertThat(id).isEqualTo(0)
            assertThat(width).isEqualTo(44)
            assertThat(height).isEqualTo(55)
            assertThat(type).isEqualTo("tile type 1")
        }
        with(tileList[1]) {
            assertThat(id).isEqualTo(1)
            assertThat(width).isEqualTo(44)
            assertThat(height).isEqualTo(55)
            assertThat(type).isEqualTo("tile type 2")
        }
        tileList.forEachIndexed { id, tile ->
            assertThat(tile.id).isEqualTo(id)
            assertThat(tile.width).isEqualTo(44)
            assertThat(tile.height).isEqualTo(55)
        }
    }

    @Test
    fun parse_isHeadlessGridTileSet_correctlyParsesTileSet() {
        val tileSet: PartialTileSet = headlessPartialTileSetParser.parse(
            "/the/top/dir", fakeGridTileSet(
                name = "the tile set",
                tileWidth = GRID_TILE_WIDTH,
                tileHeight = GRID_TILE_HEIGHT,
                columns = GRID_TILE_COLUMNS,
                tileCount = GRID_TILE_COUNT,
                children = arrayOf(
                    fakeGridTile(id = 0, type = "tile type 1"),
                    fakeGridTile(id = 1, type = "tile type 2")
                )
            )
        ).also { it.firstGid = 123 }

        with(tileSet) {
            assertThat(name).isEqualTo("the tile set")
            assertThat(firstGid).isEqualTo(123)
            assertThat(contains(123)).isTrue()
            assertThat(contains(124)).isTrue()
        }
        val tileList = (123 until GRID_TILE_COUNT + 123).map { tileSet[it]!! }
        with(tileList[0]) {
            assertThat(id).isEqualTo(0)
            assertThat(width).isEqualTo(44)
            assertThat(height).isEqualTo(55)
            assertThat(type).isEqualTo("tile type 1")
        }
        with(tileList[1]) {
            assertThat(id).isEqualTo(1)
            assertThat(width).isEqualTo(44)
            assertThat(height).isEqualTo(55)
            assertThat(type).isEqualTo("tile type 2")
        }
        tileList.forEachIndexed { id, tile ->
            assertThat(tile.id).isEqualTo(id)
            assertThat(tile.width).isEqualTo(44)
            assertThat(tile.height).isEqualTo(55)
        }
    }

    @Test
    fun parse_isGridTileSet_attributesAreEmpty_usesDefaultValues() {
        val tileSet: PartialTileSet = partialTileSetParser.parse(
            "/the/top/dir", fakeGridTileSet()
        )

        with(tileSet) {
            assertThat(name).isEqualTo("")
            assertThat(firstGid).isEqualTo(0)
            assertThat(get(0)).isNull()
        }
    }

    @Test
    fun parse_isMultiImageTileSet_correctlyParsesTileSet() {
        val tileSet: PartialTileSet = partialTileSetParser.parse(
            "/the/top/dir", fakeMultiImageTileSet(
                name = "the tile set", children = arrayOf(
                    fakeSingleImageTile(id = 0, type = "tile type 1"),
                    fakeSingleImageTile(id = 4, type = "tile type 2")
                )
            )
        ).also { it.firstGid = 456 }

        with(tileSet) {
            assertThat(name).isEqualTo("the tile set")
            assertThat(firstGid).isEqualTo(456)
            assertThat(contains(456)).isTrue()
            assertThat(contains(460)).isTrue()
            with(get(456)!!) {
                assertThat(id).isEqualTo(0)
                assertThat(type).isEqualTo("tile type 1")
            }
            with(get(460)!!) {
                assertThat(id).isEqualTo(4)
                assertThat(type).isEqualTo("tile type 2")
            }
        }
    }

    @Test
    fun parse_isHeadlessMultiImageTileSet_correctlyParsesTileSet() {
        val tileSet: PartialTileSet = headlessPartialTileSetParser.parse(
            "/the/top/dir", fakeMultiImageTileSet(
                name = "the tile set", children = arrayOf(
                    fakeSingleImageTile(id = 0, type = "tile type 1"),
                    fakeSingleImageTile(id = 4, type = "tile type 2")
                )
            )
        ).also { it.firstGid = 456 }

        with(tileSet) {
            assertThat(name).isEqualTo("the tile set")
            assertThat(firstGid).isEqualTo(456)
            assertThat(contains(456)).isTrue()
            assertThat(contains(460)).isTrue()
            with(get(456)!!) {
                assertThat(id).isEqualTo(0)
                assertThat(type).isEqualTo("tile type 1")
            }
            with(get(460)!!) {
                assertThat(id).isEqualTo(4)
                assertThat(type).isEqualTo("tile type 2")
            }
        }
    }

    @Test
    fun parse_isMultiImageTileSet_attributesAreEmpty_usesDefaultValues() {
        val tileSet: PartialTileSet = partialTileSetParser.parse(
            "/the/top/dir", fakeMultiImageTileSet()
        )

        with(tileSet) {
            assertThat(name).isEqualTo("")
            assertThat(firstGid).isEqualTo(0)
            assertThat(get(0)).isNull()
        }
    }

    companion object {
        private const val GRID_TILE_WIDTH = 44
        private const val GRID_TILE_HEIGHT = 55
        private const val GRID_TILE_COLUMNS = 10
        private const val GRID_TILE_ROWS = 6
        private const val GRID_TILE_COUNT = GRID_TILE_COLUMNS * GRID_TILE_ROWS
    }
}
