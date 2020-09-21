package believe.map.tiled

import believe.datamodel.DataManager
import believe.io.ResourceManager
import believe.util.KotlinHelpers.whenNull
import dagger.Reusable
import org.newdawn.slick.util.Log
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilder

@Reusable
class ObjectTemplateManager @Inject internal constructor(
    private val resourceManager: ResourceManager,
    private val documentBuilder: DocumentBuilder,
    parsePartialTiledObject: @JvmSuppressWildcards ElementParser<PartialTiledObject>,
    @TiledMapLocation tiledMapLocation: String
) : DataManager<PartialTiledObject> {

    private val tiledMapDirectory = tiledMapLocation.substring(0, tiledMapLocation.lastIndexOf("/"))
    private val parseTemplate = object : ElementParser<PartialTiledObject?> {
        private val parseObjects = {
            mutableListOf<PartialTiledObject>()
        }.byAccumulating("object").withParser(parsePartialTiledObject)

        override fun invoke(objectParentElement: Element): PartialTiledObject? =
            parseObjects(objectParentElement).firstOrNull()
    }
    private val cachedTemplates = mutableMapOf<String, PartialTiledObject>()

    override fun getDataFor(name: String): PartialTiledObject? {
        val template = cachedTemplates[name]
        if (template != null) return template

        val xmlTemplateLocation = "$tiledMapDirectory/$name"
        return resourceManager.getResourceAsStream(xmlTemplateLocation)?.let { xmlTemplate ->
            documentBuilder.run {
                try {
                    parse(xmlTemplate)
                } catch (e: IOException) {
                    null.also { Log.error(e) }
                } catch (e: SAXException) {
                    null.also { Log.error(e) }
                }
            }
        }?.documentElement?.let { parseTemplate(it) }
            .whenNull { Log.error("Could not parse contents of $xmlTemplateLocation.") }
            ?.also { cachedTemplates[name] = it }
    }
}
