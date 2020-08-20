package believe.map.tiled

import believe.core.PropertyProvider
import dagger.Reusable
import org.newdawn.slick.util.Log
import org.w3c.dom.Element
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.Properties
import java.util.zip.GZIPInputStream
import javax.inject.Inject

/** Adaptation of [org.newdawn.slick.tiled.Layer] for bug fixing.  */
interface Layer : PropertyProvider {
    /** The name of this layer - read from the XML. */
    val name: String
    /** The list of tiles within this [Layer]. */
    val tiles: List<Tile>

    /**
     * Render a section of this layer
     *
     * @param x The x location to render at
     * @param y The y location to render at
     */
    fun render(x: Int, y: Int)

    /** A parser that generates [Layer] instances based on provided [Element] instances. */
    @Reusable
    class Parser @Inject internal constructor(
        private val tileParser: Tile.Parser, private val tileSetGroup: TileSetGroup
    ) : ElementParser<Layer?> {

        override operator fun invoke(element: Element): Layer? {
            val name = element.getAttribute("name")
            val width = element.getAttribute("width").toInt()
            val height = element.getAttribute("height").toInt()
            val tiles = mutableListOf<Tile>()
            // now read the layer properties
            val props = Properties()
            val propsElement =
                element.getElementsByTagName("properties").takeIf { it.length > 0 }?.item(0)
            if (propsElement != null) {
                val properties = (propsElement as Element).getElementsByTagName("property")
                if (properties != null) {
                    for (p in 0 until properties.length) {
                        val propElement = properties.item(p) as Element
                        val propName = propElement.getAttribute("name")
                        val value = propElement.getAttribute("value")
                        props.setProperty(propName, value)
                    }
                }
            }
            val dataNode = element.getElementsByTagName("data").item(0) as Element
            val encoding = dataNode.getAttribute("encoding")
            val compression = dataNode.getAttribute("compression")
            if (encoding != "base64" || compression != "gzip") {
                Log.error(
                    "Unsupport tiled map type: $encoding,$compression (only gzip base64 supported)"
                )
                return null
            }
            val tilesByTileSet = mutableMapOf<TileSet, MutableList<Tile>>()
            try {
                val cdata = dataNode.firstChild
                val enc = cdata.nodeValue.trim { it <= ' ' }.toCharArray()
                val dec = decodeBase64(enc)
                val inputStream = GZIPInputStream(ByteArrayInputStream(dec))
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val bits7To0 = inputStream.read()
                        val bits15To8 = inputStream.read() shl 8
                        val bits23To16 = inputStream.read() shl 16
                        val bits31To24 = inputStream.read() shl 24
                        val tileId = bits31To24 or bits23To16 or bits15To8 or bits7To0
                        tileId.takeUnless { it == 0 }?.let {
                            tileSetGroup.tileSetForGid(tileId)
                        }?.let { tileSet ->
                            tileParser.parse(x, y, tileId, tileSet)?.also {
                                tilesByTileSet.getOrPut(tileSet) { mutableListOf() }.add(it)
                                tiles.add(it)
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                Log.error("Unable to decode base 64 block", e)
                return null
            }
            return LayerImpl(name, props, tiles, tilesByTileSet)
        }

        companion object {
            /** The code used to decode Base64 encoding  */
            private val baseCodes = ByteArray(256)

            /**
             * Decode a Base64 string as encoded by TilED
             *
             * @param data The string of character to decode
             * @return The byte array represented by character encoding
             */
            private fun decodeBase64(data: CharArray): ByteArray {
                var temp = data.size
                for (datum in data) {
                    if (datum.toInt() > 255 || baseCodes[datum.toInt()] < 0) {
                        --temp
                    }
                }
                var len = temp / 4 * 3
                if (temp % 4 == 3) len += 2
                if (temp % 4 == 2) len += 1
                val out = ByteArray(len)
                var shift = 0
                var accum = 0
                var index = 0
                for (datum in data) {
                    val value = if (datum.toInt() > 255) -1 else baseCodes[datum.toInt()].toInt()
                    if (value >= 0) {
                        accum = accum shl 6
                        shift += 6
                        accum = accum or value
                        if (shift >= 8) {
                            shift -= 8
                            out[index++] = (accum shr shift and 0xff).toByte()
                        }
                    }
                }
                if (index != out.size) {
                    throw RuntimeException(
                        "Data length appears to be wrong (wrote " + index + " should be " + out.size + ")"
                    )
                }
                return out
            }

            // Static initialiser for the codes created against Base64.
            init {
                for (i in 0..255) baseCodes[i] = -1
                for (i in 'A'.toByte()..'Z'.toByte()) {
                    baseCodes[i] = (i - 'A'.toByte()).toByte()
                }
                for (i in 'a'.toByte()..'z'.toByte()) {
                    baseCodes[i] = (26 + i - 'a'.toByte()).toByte()
                }
                for (i in '0'.toByte()..'9'.toByte()) {
                    baseCodes[i] = (52 + i - '0'.toByte()).toByte()
                }
                baseCodes['+'.toInt()] = 62
                baseCodes['/'.toInt()] = 63
            }
        }
    }
}

private class LayerImpl constructor(
    override val name: String,
    private val props: Properties,
    override val tiles: List<Tile>,
    private val tilesByTileSet: Map<TileSet, List<Tile>>
) : Layer, PropertyProvider {

    override fun render(x: Int, y: Int) = tilesByTileSet.forEach { (tileSet, tiles) ->
        tileSet.tiles.startUse()
        tiles.forEach { tile -> tile.renderInUse(x, y) }
        tileSet.tiles.endUse()
    }

    override fun getProperty(key: String): String? = props.getProperty(key)
}