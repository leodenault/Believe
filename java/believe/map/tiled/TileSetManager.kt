package believe.map.tiled

import believe.datamodel.DataManager
import believe.io.ResourceManager
import dagger.Reusable
import org.newdawn.slick.util.Log
import org.xml.sax.SAXParseException
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilder

@Reusable
internal class TileSetManager @Inject internal constructor(
    private val resourceManager: ResourceManager,
    private val documentBuilder: DocumentBuilder,
    private val partialTileSetParser: PartialTileSet.Parser
) : DataManager<PartialTileSet> {
    private val tileSets = mutableMapOf<String, PartialTileSet>()

    override fun getDataFor(name: String): PartialTileSet? {
        if (tileSets.containsKey(name)) return tileSets[name]

        val inputStream = resourceManager.getResourceAsStream(name)
        if (inputStream == null) {
            Log.error("Failed to load tile set at '$name'.")
            return null
        }

        return try {
            val tileSetDirectory = name.substring(0, name.lastIndexOf("/"))
            partialTileSetParser.parse(
                tileSetDirectory, documentBuilder.parse(inputStream).documentElement
            ).also { tileSets[name] = it }
        } catch (e: SAXParseException) {
            Log.error("Failed to parse tile set at '$name'.", e)
            null
        }
    }
}