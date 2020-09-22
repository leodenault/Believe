package believe.map.tiled.testing

import believe.map.tiled.Layer
import believe.map.tiled.Properties
import believe.map.tiled.Tile
import believe.map.tiled.TiledObject
import believe.map.tiled.TiledObjectGroup

/** Creates fake instances of {@link believe.map.tiled.TiledObjectImpl} */
object TiledFakes {
    /** Instantiates a fake [TiledObject]. */
    @JvmStatic
    @JvmOverloads
    fun fakeTiledObject(
        type: String? = null,
        name: String? = null,
        x: Float = 0f,
        y: Float = 0f,
        width: Float = 0f,
        height: Float = 0f,
        properties: Map<String, String> = mapOf()
    ) = object : TiledObject {

        override val name = name
        override val type = type
        override val x = x
        override val y = y
        override val width = width
        override val height = height

        override fun getProperty(key: String) = properties[key]
    }

    /** Instantiates a fake [TiledObjectGroup]. */
    @JvmStatic
    @JvmOverloads
    fun fakeTiledObjectGroup(
        name: String = "",
        objects: List<TiledObject> = listOf(),
        width: Float = 0f,
        height: Float = 0f
    ) = object : TiledObjectGroup {
        override val name = name
        override val objects = objects
        override val width = width
        override val height = height

        override fun getProperty(key: String): Nothing? = null

    }

    /** Instantiates a fake [Layer]. */
    @JvmStatic
    @JvmOverloads
    fun fakeLayer(
        properties: Map<String, String> = mapOf()
    ) = object : Layer {
        override val name = ""

        override fun render(x: Float, y: Float) = Unit

        override fun getProperty(key: String) = properties[key]
    }

    /** Instantiates a fake [Tile]. */
    @JvmStatic
    @JvmOverloads
    fun fakeTile(
        type: String? = null,
        width: Int = 0,
        height: Int = 0,
        properties: Map<String, String> = mapOf()
    ) = object : Tile {
        override val id: Int = 0
        override val type: String? = type
        override val width = width
        override val height = height
        override val properties: Properties = Properties.fromMap(properties)

        override fun render(x: Float, y: Float) = Unit

    }
}
