package believe.map.tiled

import believe.datamodel.DataManager
import believe.geometry.IntPoint
import dagger.Reusable
import org.w3c.dom.Element
import javax.inject.Inject

typealias TileRenderingBatch = Map<Tile, List<IntPoint>>

/**
 * Adaptation of [org.newdawn.slick.tiled.TileSet] for fixing bugs.
 *
 * See http://slick.ninjacave.com/license/ for license and copyright information on the original
 * source.
 */
interface TileSet {
    /** The name of the tile set  */
    val name: String

    /** The first global tile id in the set  */
    val firstGid: Int

    /** Returns the [Tile] associated with [gid], otherwise null. */
    operator fun get(gid: Int): Tile?

    /**
     * Check if this tileset contains a particular tile
     *
     * @param gid The global id to search for
     * @return True if the ID is contained in this tileset
     */
    operator fun contains(gid: Int): Boolean

    /**
     * Renders tiles in a batch.
     *
     * @param x the X coordinate by which [tileRenderingBatch] should offset its tiles.
     * @param y the Y coordinate by which [tileRenderingBatch] should offset its tiles.
     * @param tileRenderingBatch a map of coordinate lists keyed by the GID of the [Tile] that
     * should be rendered at those coordinates.
     */
    fun renderBatch(x: Float, y: Float, tileRenderingBatch: TileRenderingBatch)

    @Reusable
    class Parser @Inject constructor(
        private val partialTileSetParser: PartialTileSet.Parser,
        private val tileSetManager: DataManager<PartialTileSet>,
        @TiledMapLocation tiledMapLocation: String
    ) : ElementParser<TileSet?> {

        private val tiledMapDirectory =
            tiledMapLocation.substring(0, tiledMapLocation.lastIndexOf("/"))

        override fun invoke(element: Element): TileSet? {
            val source = PARSE_SOURCE(element)

            return (if (source == null) partialTileSetParser.parse(
                tiledMapDirectory, element
            ) else tileSetManager.getDataFor(
                "$tiledMapDirectory/$source"
            ))?.also { it.firstGid = PARSE_FIRST_GID(element) ?: 0 }
        }
    }

    companion object {
        private val PARSE_FIRST_GID = integerAttributeParser("firstgid")
        private val PARSE_SOURCE = stringAttributeParser("source")
    }
}
