package believe.map.tiled

import believe.core.PropertyProvider
import dagger.Reusable
import org.newdawn.slick.SpriteSheet
import java.util.Properties
import javax.inject.Inject

/** A tile within the context of a [TiledMap]. */
interface Tile : PropertyProvider {
    /** The X position of the [Tile] in pixels. */
    val pixelX: Int
    /** The Y position of the [Tile] in pixels. */
    val pixelY: Int
    /** The width of the [Tile]. */
    val width: Int
    /** The height of the [Tile]. */
    val height: Int

    /**
     * Renders the [Tile] while a the corresponding [SpriteSheet] is "in use".
     *
     * **Do not call this without first calling [SpriteSheet.startUse]**.
     */
    fun renderInUse(x: Int, y: Int)

    @Reusable
    class Parser @Inject internal constructor(
        @TiledMapTileWidth private val mapTileWidth: Int,
        @TiledMapTileHeight
        private val mapTileHeight: Int
    ) {
        fun parse(x: Int, y: Int, tileId: Int, tileSet: TileSet): Tile? =
            tileId.takeUnless { it == 0 }?.let {
                val localTileId = tileId - tileSet.firstGID
                TileImpl(
                    pixelX = x * mapTileWidth,
                    pixelY = y * mapTileHeight + (mapTileHeight - tileSet.tileHeight),
                    width = tileSet.tileWidth,
                    height = tileSet.tileHeight,
                    properties = tileSet.getPropertiesForTile(tileId) ?: Properties(),
                    spriteSheetX = tileSet.getTileX(localTileId),
                    spriteSheetY = tileSet.getTileY(localTileId),
                    tileSetSpriteSheet = tileSet.tiles
                )
            }
    }
}

private class TileImpl(
    override val pixelX: Int,
    override val pixelY: Int,
    override val width: Int,
    override val height: Int,
    private val properties: Properties,
    private val spriteSheetX: Int,
    private val spriteSheetY: Int,
    private val tileSetSpriteSheet: SpriteSheet
) : Tile {

    override fun getProperty(key: String): String? = properties.getProperty(key)

    override fun renderInUse(x: Int, y: Int) =
        tileSetSpriteSheet.renderInUse(x + pixelX, y + pixelY, spriteSheetX, spriteSheetY)
}
