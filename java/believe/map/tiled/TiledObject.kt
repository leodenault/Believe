package believe.map.tiled

import believe.core.PropertyProvider
import believe.datamodel.DataManager
import dagger.Reusable
import org.w3c.dom.Element
import javax.inject.Inject

/** An object from a Tiled map. */
interface TiledObject : PropertyProvider {
    /** The type of entity parsed from the object. */
    val type: String?
    /** The x pixel position of the object within the map. */
    val x: Float
    /** The y pixel position of the object within the map. */
    val y: Float
    /** The width of the object. */
    val width: Float
    /** The height of the object. */
    val height: Float

    @Reusable
    class Parser @Inject internal constructor(
        private val parsePartialTiledObject: @JvmSuppressWildcards ElementParser<PartialTiledObject>,
        private val templateManager: @JvmSuppressWildcards DataManager<PartialTiledObject>
    ) : ElementParser<TiledObject> {

        private val parseTemplateLocation = stringAttributeParser("template")

        override fun invoke(element: Element): TiledObject {
            val partialTiledObject = parseTemplateLocation(element)?.let {
                templateManager.getDataFor(it)
            } ?: PartialTiledObject.empty()

            partialTiledObject.overrideWith(parsePartialTiledObject(element))
            return partialTiledObject.toTiledObject()
        }
    }
}
