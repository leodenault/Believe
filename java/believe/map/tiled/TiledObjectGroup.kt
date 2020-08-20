package believe.map.tiled

import believe.core.PropertyProvider
import dagger.Reusable
import org.w3c.dom.Element
import javax.inject.Inject

/** A group of objects in a Tiled map. */
interface TiledObjectGroup : PropertyProvider {
    val name: String
    val objects: List<TiledObject>
    val width: Float
    val height: Float

    @Reusable
    class Parser @Inject internal constructor(
        tiledObjectParser: @JvmSuppressWildcards ElementParser<TiledObject>
    ) : ElementParser<TiledObjectGroup> {
        private val parseTiledObjectGroup = { TiledObjectGroupImpl() }.byCombining(
            stringAttributeParser("name") withSetter {
                name = it
            },
            floatAttributeParser("width") withSetter { width = it },
            floatAttributeParser("height") withSetter { height = it },
            { mutableListOf<TiledObject>() }.byAccumulating("object").withParser(
                tiledObjectParser
            ) withSetter { objects = it },
            Properties.parser() withSetter { properties.overrideWith(it) })

        override fun invoke(element: Element): TiledObjectGroup = parseTiledObjectGroup(element)
    }
}

private class TiledObjectGroupImpl : TiledObjectGroup {
    internal val properties = Properties.empty()

    override var name = ""
    override var objects = listOf<TiledObject>()
    override var width = 0f
    override var height = 0f

    override fun getProperty(key: String) = properties.getProperty(key)
}
