package believe.map.tiled

import dagger.Reusable
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet
import org.newdawn.slick.util.Log
import org.w3c.dom.Element
import javax.inject.Inject

/** A tile within the context of a [TiledMap]. */
interface Tile {
    /** The local ID assigned to the tile. */
    val id: Int

    /** The type of this tile, if any. */
    val type: String?

    /** The width of the [Tile]. */
    val width: Int

    /** The height of the [Tile]. */
    val height: Int

    /** The [Properties] associated with this instance. */
    val properties: Properties

    /** Renders the [Tile] at coordinates ([x], [y]). */
    fun render(x: Float, y: Float)

    @Reusable
    class GridTileFactory @Inject internal constructor() {
        internal fun create(
            tileWidth: Int, tileHeight: Int, spriteSheet: SpriteSheet, element: Element
        ): List<Tile> = createGridTiles(
            tileWidth, tileHeight, spriteSheet.horizontalCount * spriteSheet.verticalCount, element
        ) { id, x, y ->
            spriteSheet.renderInUse(
                x.toInt(),
                y.toInt(),
                id % spriteSheet.horizontalCount,
                id / spriteSheet.horizontalCount
            )
        }

        internal fun createHeadless(
            tileWidth: Int, tileHeight: Int, tileCount: Int, element: Element
        ): List<Tile> = createGridTiles(tileWidth, tileHeight, tileCount, element) { _, _, _ -> }

        private fun createGridTiles(
            tileWidth: Int,
            tileHeight: Int,
            tileCount: Int,
            element: Element,
            render: (Int, Float, Float) -> Unit
        ): List<Tile> {
            val tiles: Map<Int, TileBase> = PARSE_GRID_TILES(element)

            return (0 until tileCount).map { index ->
                tiles.getOrElse(index) { TileBase() }.apply {
                    id = index
                    width = tileWidth
                    height = tileHeight
                    renderFromStrategy = { x, y -> render(id, x, y) }
                }
            }
        }
    }

    @Reusable
    class SingleImageTileParser @Inject internal constructor(
        private val loadImage: (@JvmSuppressWildcards String) -> @JvmSuppressWildcards Image?
    ) {
        internal fun parse(tileSetDirectory: String, element: Element): Tile {
            val tile = PARSE_MULTI_IMAGE_TILE(element)

            val imageLocation = "$tileSetDirectory/${tile.source}"
            val image = loadImage(imageLocation)
            if (image == null) {
                Log.error("Could not load tile image at '$imageLocation'.")
                return tile
            }

            tile.renderFromStrategy = { x, y -> image.draw(x, y) }
            return tile
        }

        internal fun parseHeadless(element: Element): Tile = PARSE_MULTI_IMAGE_TILE(element)
    }

    companion object {
        private val PARSE_BASE_TILE = {
            TileBase()
        }.byCombining(stringAttributeParser("type") withSetter { type = it },
            integerAttributeParser("id") withSetter { id = it },
            Properties.parser() withSetter { properties.overrideWith(it) })
        private val PARSE_GRID_TILES =
            { mutableMapOf<Int, TileBase>() }.byAccumulating("tile").withParser(
                PARSE_BASE_TILE
            ).reduce { put(it.id, it) }
        private val PARSE_MULTI_IMAGE_TILE = {
            MultiImageTile()
        }.byCombining(PARSE_BASE_TILE withSetter {
            id = it.id
            type = it.type
            width = it.width
            height = it.height
            properties.overrideWith(it.properties)
        }, {
            mutableListOf<ImageData>()
        }.byAccumulating("image").withParser({
            ImageData()
        }.byCombining(integerAttributeParser("width") withSetter { width = it },
            integerAttributeParser("height") withSetter { height = it },
            stringAttributeParser("source") withSetter { source = it })
        ) withSetter setImageData@{
            val singleImageData = it.firstOrNull() ?: return@setImageData Unit.also {
                Log.error("Could not determine image data from tile in multi-image tile set.")
            }
            width = singleImageData.width
            height = singleImageData.height
            source = singleImageData.source
        })
    }
}

private open class TileBase : Tile {
    override var id: Int = 0
    override var type: String? = null
    override var width: Int = 0
    override var height: Int = 0
    override val properties: Properties = Properties.empty()
    var renderFromStrategy: (Float, Float) -> Unit = { _, _ -> }

    override fun render(x: Float, y: Float) = renderFromStrategy(x, y)

}

private class MultiImageTile : TileBase() {
    var source: String = ""
}

private class ImageData {
    var width: Int = 0
    var height: Int = 0
    var source: String = ""
}
