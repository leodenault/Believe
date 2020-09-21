package believe.map.tiled

import dagger.Reusable
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet
import org.w3c.dom.Element
import javax.inject.Inject

interface PartialTileSet : TileSet {
    override var firstGid: Int

    @Reusable
    class Parser @Inject internal constructor(
        private val gridTileFactory: Tile.GridTileFactory,
        private val singleImageTileParser: Tile.SingleImageTileParser,
        private val loadImage: (@JvmSuppressWildcards String) -> @JvmSuppressWildcards Image?,
        @IsHeadless private val isHeadless: Boolean
    ) {
        fun parse(tileSetDirectory: String, element: Element): PartialTileSet {
            val name = PARSE_NAME(element) ?: ""
            val columns = PARSE_COLUMNS(element)
            val tileCount = PARSE_TILE_COUNT(element) ?: 0

            val tilesById: Map<Int, Tile>
            var render: (Float, Float, TileRenderingBatch) -> Unit = { _, _, _ -> }
            if (columns == null || columns == 0) {
                tilesById = createMultiTileParser(isHeadless, tileSetDirectory)(element)
                render = Companion::renderTiles
            } else {
                tilesById = with(PARSE_SPRITE_SHEET_DEFINITION(element)) {
                    val spriteSheet: SpriteSheet?
                    if (isHeadless) {
                        spriteSheet = null
                    } else {
                        spriteSheet = createSpriteSheet(loadImage, tileSetDirectory)
                        if (spriteSheet != null) {
                            render = { x, y, tileRenderingBatch ->
                                spriteSheet.startUse()
                                renderTiles(x, y, tileRenderingBatch)
                                spriteSheet.endUse()
                            }
                        }
                    }
                    parseGridTiles(tileWidth, tileHeight, tileCount, spriteSheet, element)
                }
            }

            val tileSetProperties = TileSetProperties(name, tilesById.keys.max() ?: 0, tilesById)
            return if (isHeadless) HeadlessTileSet(tileSetProperties) else TileSetBase(
                tileSetProperties, render
            )
        }

        private fun createMultiTileParser(
            isHeadless: Boolean, tileSetDirectory: String
        ): ElementParser<Map<Int, Tile>> = {
            mutableMapOf<Int, Tile>()
        }.byAccumulating("tile").withParser { element: Element ->
            if (isHeadless) singleImageTileParser.parse(
                tileSetDirectory, element
            ) else singleImageTileParser.parseHeadless(element)
        }.reduce {
            it.let { put(it.id, it) }
        }

        private fun parseGridTiles(
            tileWidth: Int,
            tileHeight: Int,
            tileCount: Int,
            spriteSheet: SpriteSheet?,
            element: Element
        ): Map<Int, Tile> {
            val tileMap = if (spriteSheet == null) gridTileFactory.createHeadless(
                tileWidth, tileHeight, tileCount, element
            ) else gridTileFactory.create(
                tileWidth, tileHeight, spriteSheet, element
            )
            return tileMap.associateBy { it.id }
        }

        companion object {
            private val PARSE_NAME = stringAttributeParser("name")
            private val PARSE_COLUMNS = integerAttributeParser("columns")
            private val PARSE_TILE_COUNT = integerAttributeParser("tilecount")
            private val PARSE_SPRITE_SHEET_DEFINITION = { SpriteSheetDefinition() }.byCombining(
                integerAttributeParser("tilewidth") withSetter { tileWidth = it },
                integerAttributeParser("tileheight") withSetter { tileHeight = it },
                integerAttributeParser("spacing") withSetter { tileSpacing = it },
                integerAttributeParser("margin") withSetter { tileMargin = it },
                { mutableListOf<String>() }.byAccumulating("image").withParser(
                    stringAttributeParser("source")
                ).reduce { if (it != null) add(it) } withSetter { source = it.first() })

            fun renderTiles(x: Float, y: Float, tileRenderingBatch: TileRenderingBatch) {
                tileRenderingBatch.forEach { (tile, coordinateList) ->
                    coordinateList.forEach { coordinate ->
                        tile.render(x + coordinate.x, y + coordinate.y)
                    }
                }
            }
        }
    }
}

private class TileSetProperties(
    val name: String, val lastLocalId: Int, val tiles: Map<Int, Tile>
)

private class TileSetBase(
    private val tileSetProperties: TileSetProperties,
    private val render: (Float, Float, tileRenderingBatch: TileRenderingBatch) -> Unit
) : PartialTileSet {
    override var firstGid: Int = 0
    override val name: String = tileSetProperties.name

    override fun get(gid: Int): Tile? = tileSetProperties.tiles[gid - firstGid]

    override fun contains(gid: Int) =
        tileSetContainsTileId(gid, firstGid, tileSetProperties.lastLocalId)

    override fun renderBatch(x: Float, y: Float, tileRenderingBatch: TileRenderingBatch) =
        render(x, y, tileRenderingBatch)
}

private class HeadlessTileSet(
    tileSetProperties: TileSetProperties
) : PartialTileSet by TileSetBase(tileSetProperties, { _, _, _ -> })

private class SpriteSheetDefinition {
    var tileWidth: Int = 0
    var tileHeight: Int = 0
    var tileSpacing: Int = 0
    var tileMargin: Int = 0
    var source: String = ""

    inline fun createSpriteSheet(loadImage: (String) -> Image?, tileSetDirectory: String) =
        loadImage("$tileSetDirectory/$source").let {
            if (it == null) null else SpriteSheet(
                it, tileWidth, tileHeight, tileSpacing, tileMargin
            )
        }
}

private fun tileSetContainsTileId(tileGid: Int, firstGid: Int, lastLocalId: Int) =
    tileGid in firstGid..(lastLocalId + firstGid)
