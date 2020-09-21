package believe.map.tiled

import dagger.Reusable
import javax.inject.Inject

class PartialTiledObject private constructor() {
    val properties = Properties.empty()

    var name: String? = null
        private set
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
        name = other.name ?: name
        type = other.type ?: type
        x = other.x ?: x
        y = other.y ?: y
        width = other.width ?: width
        height = other.height ?: height
        properties.overrideWith(other.properties)
    }

    fun toTiledObject() = object : TiledObject {
        override val name = this@PartialTiledObject.name
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
    }.byCombining(stringAttributeParser("name") withSetter { name = it },
        floatAttributeParser("x") withSetter { x = it },
        floatAttributeParser("y") withSetter { y = it },
        attributeParser("gid") { stringGid ->
            stringGid.toIntOrNull()?.let { gid ->
                tileSetGroup.tileSetForGid(gid)?.get(gid)
            }
        } withSetter {
            type = it.type
            width = it.width.toFloat()
            height = it.height.toFloat()
            properties.overrideWith(it.properties)
        },
        stringAttributeParser("type") withSetter { type = it },
        floatAttributeParser("width") withSetter { width = it },
        floatAttributeParser("height") withSetter { height = it },
        Properties.parser() withSetter { properties.overrideWith(it) })

    companion object {
        internal fun empty() = PartialTiledObject()
    }
}
