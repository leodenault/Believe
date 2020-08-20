package believe.map.tiled

import dagger.Reusable
import java.util.function.Supplier
import javax.inject.Inject

class PartialTiledObject private constructor() {
    val properties = Properties.empty()

    var type: String? = null
        private set
    var x: Float? = null
        private set
    var y: Float? = null
        private set
    var width: Float? = null
        private set
    var height: Float? = null
        private set

    fun overrideWith(other: PartialTiledObject) {
        type = other.type ?: type
        x = other.x ?: x
        y = other.y ?: y
        width = other.width ?: width
        height = other.height ?: height
        properties.overrideWith(other.properties)
    }

    fun toTiledObject() = object : TiledObject {
        override val type = this@PartialTiledObject.type
        override val x = this@PartialTiledObject.x ?: 0f
        override val y = this@PartialTiledObject.y ?: 0f
        override val width = this@PartialTiledObject.width ?: 0f
        override val height = this@PartialTiledObject.height ?: 0f

        override fun getProperty(key: String): String? =
            this@PartialTiledObject.properties.getProperty(key)
    }

    @Reusable
    class Parser @Inject internal constructor(
        private val tileSetGroup: TileSetGroup
    ) : ElementParser<PartialTiledObject> by {
        PartialTiledObject()
    }.byCombining(stringAttributeParser("type") withSetter { type = it },
        floatAttributeParser("width") withSetter { width = it },
        floatAttributeParser("height") withSetter { height = it },
        floatAttributeParser("x") withSetter { x = it },
        // Due to the Tiled issue found at https://github.com/bjorn/tiled/issues/91 we have to
        // adjust the y position of objects by subtracting the height. The reason for this is that,
        // unlike tiles, the y position for objects is defined at the bottom of the object rather
        // than the top.
        floatAttributeParser("y") withSetter { y = it - (height ?: 0f) },
        attributeParser("gid") { stringGid ->
            stringGid.toIntOrNull()?.let { gid ->
                val tileSet = tileSetGroup.tileSetForGid(gid)
                tileSet?.getPropertiesForTile(gid)
            }?.let { Properties.fromJava(it) }
        } withSetter { properties.overrideWith(it) },
        Properties.parser() withSetter { properties.overrideWith(it) })

    companion object {
        internal fun empty() = PartialTiledObject()
    }
}
