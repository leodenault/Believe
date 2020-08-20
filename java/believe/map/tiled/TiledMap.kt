package believe.map.tiled

import believe.io.ResourceManager
import believe.map.tiled.TileSetGroup.Companion.create
import dagger.Reusable
import org.newdawn.slick.util.Log
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*
import java.util.Properties
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * Adaptation of [org.newdawn.slick.tiled.TiledMap] with bug fixes.
 *
 *
 * See http://slick.ninjacave.com/license/ for license and copyright information on the original
 * source.
 */
interface TiledMap {
    /**
     * Get the index of the layer with given name
     *
     * @param name The name of the tile to search for
     * @return The index of the layer or -1 if there is no layer with given name
     */
    fun getLayerIndex(name: String): Int

    /**
     * Get the width of the map
     *
     * @return The width of the map (in tiles)
     */
    val width: Int

    /**
     * Get the height of the map
     *
     * @return The height of the map (in tiles)
     */
    val height: Int

    /**
     * Get the width of a single tile
     *
     * @return The height of a single tile (in pixels)
     */
    val tileWidth: Int

    /**
     * Get the height of a single tile
     *
     * @return The height of a single tile (in pixels)
     */
    val tileHeight: Int

    /** Returns the list of layers associated with this map.  */
    val layers: List<Layer>

    /** Returns the list of object groups associated with this map.  */
    val objectGroups: List<TiledObjectGroup>

    /**
     * Get a property given to the map. Note that this method will not perform well and should not be
     * used as part of the default code path in the game loop.
     *
     * @param propertyName The name of the property of the map to retrieve
     * @return The value assigned to the property on the map (or the default value if none is
     * supplied)
     */
    fun getMapProperty(propertyName: String): Optional<String>

    /**
     * Render the whole tile map at a given location
     *
     * @param x The x location to render at
     * @param y The y location to render at
     */
    fun render(x: Int, y: Int)

    /**
     * Render a single layer from the map
     *
     * @param x The x location to render at
     * @param y The y location to render at
     * @param layer The layer to render
     */
    fun render(x: Int, y: Int, layer: Layer)

    /** A parser for loading [TiledMap] instances from a file on disk.  */
    @Reusable
    class Parser @Inject internal constructor(private val resourceManager: ResourceManager) {
        private val tileSetParserComponentFactory: TileSetParserComponent.Factory =
            DaggerTileSetParserComponent.factory()
        private val tiledParserComponentFactory: TiledParserComponent.Factory =
            DaggerTiledParserComponent.factory()

        /** Parse a TilED map  */
        @JvmOverloads
        fun parse(
            tileMapLocation: String?, tileSetsLocation: String?, isHeadless: Boolean = false
        ): TiledMap? {
            val `in` = resourceManager.getResourceAsStream(tileMapLocation!!) ?: return null
            val doc: Document
            try {
                val factory = DocumentBuilderFactory.newInstance()
                factory.isValidating = false
                val builder = factory.newDocumentBuilder()
                builder.setEntityResolver { _: String, _: String ->
                    InputSource(ByteArrayInputStream(ByteArray(0)))
                }
                doc = builder.parse(`in`)
            } catch (e: ParserConfigurationException) {
                Log.error("Failed to parse tilemap", e)
                return null
            } catch (e: SAXException) {
                Log.error("Failed to parse tilemap", e)
                return null
            } catch (e: IOException) {
                Log.error("Failed to parse tilemap", e)
                return null
            }
            val docElement = doc.documentElement
            if (docElement.getAttribute("orientation") != "orthogonal") {
                Log.error("Only orthogonal maps are supported. Continuing by drawing orthogonally.")
            }
            val width = parseIntOrZero(docElement.getAttribute("width"))
            val height = parseIntOrZero(docElement.getAttribute("height"))
            val tileWidth = parseIntOrZero(docElement.getAttribute("tilewidth"))
            val tileHeight = parseIntOrZero(docElement.getAttribute("tileheight"))
            // now read the map properties
            val props = Properties()
            val propsElement = docElement.getElementsByTagName("properties")?.item(0)
            if (propsElement != null) {
                val properties = (propsElement as Element).getElementsByTagName("property")
                if (properties != null) {
                    for (p in 0 until properties.length) {
                        val propElement = properties.item(p) as Element
                        val name = propElement.getAttribute("name")
                        val value = propElement.getAttribute("value")
                        props.setProperty(name, value)
                    }
                }
            }
            val tileSetParserComponent =
                tileSetParserComponentFactory.create(tileMapLocation, tileSetsLocation)
            val tileSetParser = tileSetParserComponent.tileSetParser
            val tileSets = ArrayList<TileSet>()
            var tileSet: TileSet
            var lastGid = Int.MAX_VALUE
            val setNodes = docElement.getElementsByTagName("tileset")
            for (i in setNodes.length - 1 downTo -1 + 1) {
                val current = setNodes.item(i) as Element
                tileSet = tileSetParser.parse(current, i, lastGid, !isHeadless)
                tileSets.add(tileSet)
                lastGid = tileSet.firstGID - 1
            }
            val tileSetGroup = create(tileSets)
            val tiledParserComponent = tiledParserComponentFactory.create(
                tileMapLocation, tileSetGroup, tileWidth, tileHeight
            )
            val layers: MutableList<Layer> = ArrayList()
            val layerNodes = docElement.getElementsByTagName("layer")
            for (i in 0 until layerNodes.length) {
                val current = layerNodes.item(i) as Element
                val layer = tiledParserComponent.layerParser.invoke(current)
                layers.add(layer)
            }
            // acquire object-groups
            val objectGroups: MutableList<TiledObjectGroup> = ArrayList()
            val objectGroupNodes = docElement.getElementsByTagName("objectgroup")
            for (i in 0 until objectGroupNodes.length) {
                objectGroups.add(
                    tiledParserComponent.tiledObjectGroupParser.invoke(objectGroupNodes.item(i) as Element)
                )
            }
            return TiledMapImpl(
                width, height, tileWidth, tileHeight, objectGroups, layers, props, tileSetGroup
            )
        }

        companion object {
            /**
             * Save parser for strings to ints
             *
             * @param value The string to parse
             * @return The integer to parse or zero if the string isn't an int
             */
            private fun parseIntOrZero(value: String): Int {
                return try {
                    value.toInt()
                } catch (e: NumberFormatException) {
                    0
                }
            }
        }

    }
}

private class TiledMapImpl constructor(
    /**
     * Get the width of the map
     *
     * @return The width of the map (in tiles)
     */
    override val width: Int,
    /**
     * Get the height of the map
     *
     * @return The height of the map (in tiles)
     */
    override val height: Int,
    /**
     * Get the width of a single tile
     *
     * @return The height of a single tile (in pixels)
     */
    override val tileWidth: Int,
    /**
     * Get the height of a single tile
     *
     * @return The height of a single tile (in pixels)
     */
    override val tileHeight: Int,
    /** Returns the list of object groups associated with this map.  */
    override val objectGroups: List<TiledObjectGroup>,
    /** Returns the list of layers associated with this map.  */
    override val layers: List<Layer>,
    private val props: Properties,
    private val tileSetGroup: TileSetGroup
) : TiledMap {
    /**
     * Get the index of the layer with given name
     *
     * @param name The name of the tile to search for
     * @return The index of the layer or -1 if there is no layer with given name
     */
    override fun getLayerIndex(name: String): Int {
        for (i in layers.indices) {
            val layer = layers[i]
            if (layer.name == name) {
                return i
            }
        }
        return -1
    }

    /**
     * Get a property given to the map. Note that this method will not perform well and should not
     * be used as part of the default code path in the game loop.
     *
     * @param propertyName The name of the property of the map to retrieve
     * @return The value assigned to the property on the map (or the default value if none is
     * supplied)
     */
    override fun getMapProperty(propertyName: String): Optional<String> {
        return Optional.ofNullable(props.getProperty(propertyName))
    }

    /**
     * Render the whole tile map at a given location
     *
     * @param x The x location to render at
     * @param y The y location to render at
     */
    override fun render(x: Int, y: Int) = layers.forEach { render(x, y, it) }

    /**
     * Render a single layer from the map
     *
     * @param x The x location to render at
     * @param y The y location to render at
     * @param layer The layer to render
     */
    override fun render(x: Int, y: Int, layer: Layer) = layer.render(x, y)
}
