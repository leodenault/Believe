package believe.map.tiled

import believe.io.ResourceManager
import dagger.Reusable
import org.newdawn.slick.Color
import org.newdawn.slick.Image
import org.newdawn.slick.SlickException
import org.newdawn.slick.SpriteSheet
import org.newdawn.slick.util.Log
import org.w3c.dom.Element
import java.util.*
import java.util.Properties
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Adaptation of [org.newdawn.slick.tiled.TileSet] for fixing bugs.
 *
 * See http://slick.ninjacave.com/license/ for license and copyright information on the original
 * source.
 */
interface TileSet {
    /** The index of the tile set  */
    val index: Int
    /** The name of the tile set  */
    val name: String
    /** The first global tile id in the set  */
    val firstGID: Int
    /** The width of the tiles  */
    val tileWidth: Int
    /** The height of the tiles  */
    val tileHeight: Int
    /** The image containing the tiles  */
    val tiles: SpriteSheet

    /**
     * Get the properties for a specific tile in this tileset
     *
     * @param globalID The global ID of the tile whose properties should be retrieved
     * @return The properties for the specified tile, or null if no properties are defined
     */
    fun getPropertiesForTile(globalID: Int): Properties?

    /**
     * Get the x position of a tile on this sheet
     *
     * @param id The tileset specific ID (i.e. not the global one)
     * @return The index of the tile on the x-axis
     */
    fun getTileX(id: Int): Int

    /**
     * Get the y position of a tile on this sheet
     *
     * @param id The tileset specific ID (i.e. not the global one)
     * @return The index of the tile on the y-axis
     */
    fun getTileY(id: Int): Int

    /**
     * Check if this tileset contains a particular tile
     *
     * @param gid The global id to search for
     * @return True if the ID is contained in this tileset
     */
    operator fun contains(gid: Int): Boolean

    @Reusable
    class Parser @Inject constructor(
        private val resourceManager: ResourceManager,
        @TileSetsLocation
        private val tileSetsLocation: String, @TiledMapLocation private val tiledMapLocation: String
    ) {
        /**
         * Create a tile set based on an XML definition
         *
         * @param element the XML describing the tileset context to paths)
         * @param index the index of the tile set relative to other tile sets.
         * @param lastGID the last GID that can be assigned to a tile in this set.
         * @param loadImage true if we should load the image (false is useful in headless mode)
         */
        fun parse(
            element: Element, index: Int, lastGID: Int, loadImage: Boolean
        ): TileSet {
            var elementToParse = element
            val name = elementToParse.getAttribute("name")
            val firstGID = elementToParse.getAttribute("firstgid").toInt()
            val source = elementToParse.getAttribute("source")
            if (source != null && source != "") {
                val sourceLocation =
                    tiledMapLocation.substring(0, tiledMapLocation.lastIndexOf("/")) + "/" + source
                elementToParse = try {
                    val inputStream = resourceManager.getResourceAsStream(sourceLocation)
                    val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    val doc = builder.parse(inputStream)
                    doc.documentElement
                } catch (e: Exception) {
                    Log.error(e)
                    throw RuntimeException(
                        "Unable to load or parse sourced tileset: $sourceLocation"
                    )
                }
            }
            val tileWidthString = elementToParse.getAttribute("tilewidth")
            val tileHeightString = elementToParse.getAttribute("tileheight")
            if (tileWidthString.isEmpty() || tileHeightString.isEmpty()) {
                throw RuntimeException(
                    """
                        TiledMap requires that the map be created with tilesets that use a single
                        image. Check the WiKi for more complete information.
                    """.trimMargin()
                )
            }
            val tileWidth = tileWidthString.toInt()
            val tileHeight = tileHeightString.toInt()
            val tileSpacing =
                elementToParse.getAttribute("spacing")?.takeUnless { it.isEmpty() }?.toInt() ?: 0
            val tileMargin =
                elementToParse.getAttribute("margin")?.takeUnless { it.isEmpty() }?.toInt() ?: 0
            val list = elementToParse.getElementsByTagName("image")
            val imageNode = list.item(0) as Element
            val ref = imageNode.getAttribute("source")
            val trans: Color? = imageNode.getAttribute("trans")?.takeUnless { it.isEmpty() }
                ?.let { Color(it.toInt(16)) }
            val tiles: SpriteSheet? = if (loadImage) {
                try {
                    val image = Image("$tileSetsLocation/$ref", false, Image.FILTER_NEAREST, trans)
                    SpriteSheet(image, tileWidth, tileHeight, tileSpacing, tileMargin)
                } catch (e: SlickException) {
                    Log.error("Failed to load tile set image.", e)
                    null
                }
            } else null
            val props: MutableMap<Int, Properties> = HashMap()
            val pElements = elementToParse.getElementsByTagName("tile")
            for (i in 0 until pElements.length) {
                val tileElement = pElements.item(i) as Element
                var id = tileElement.getAttribute("id").toInt()
                id += firstGID
                val tileProps = Properties()
                val propsElement = tileElement.getElementsByTagName("properties").item(0) as Element
                val properties = propsElement.getElementsByTagName("property")
                for (p in 0 until properties.length) {
                    val propElement = properties.item(p) as Element
                    val propName = propElement.getAttribute("name")
                    var value = propElement.getAttribute("value")
                    if (value.isEmpty()) {
                        value = propElement.textContent
                    }
                    tileProps.setProperty(propName, value)
                }
                props[id] = tileProps
            }

            return if (tiles == null) HeadlessTileSet(
                index, name, firstGID, lastGID, tileWidth, tileHeight, props
            ) else TileSetImpl(
                index, name, firstGID, lastGID, tileWidth, tileHeight, tiles, props
            )
        }
    }
}

private class TileSetImpl(
    override val index: Int,
    override val name: String,
    override val firstGID: Int,
    private val lastGID: Int,
    override val tileWidth: Int,
    override val tileHeight: Int,
    override val tiles: SpriteSheet,
    private val tileProperties: Map<Int, Properties>
) : TileSet {
    override fun getPropertiesForTile(globalID: Int) = tileProperties[globalID]
    override fun getTileX(id: Int) = id % tiles.horizontalCount
    override fun getTileY(id: Int) = id / tiles.verticalCount
    override fun contains(gid: Int) = gid in firstGID..lastGID
}

private class HeadlessTileSet(
    override val index: Int,
    override val name: String,
    override val firstGID: Int,
    private val lastGID: Int,
    override val tileWidth: Int,
    override val tileHeight: Int,
    private val tileProperties: Map<Int, Properties>
) : TileSet {
    override val tiles: SpriteSheet = SpriteSheet(Image(0, 0), 0, 0)
    override fun getPropertiesForTile(globalID: Int) = tileProperties[globalID]
    override fun getTileX(id: Int) = 0
    override fun getTileY(id: Int) = 0
    override fun contains(gid: Int) = gid in firstGID..lastGID
}
