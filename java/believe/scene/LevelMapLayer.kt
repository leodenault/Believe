package believe.scene

import believe.core.display.Graphics
import believe.core.display.RenderableV2
import believe.map.tiled.Layer
import believe.map.tiled.TiledMap

/** A renderable component representing a layer of a map.  */
internal class LevelMapLayer constructor(private val layer: Layer) : RenderableV2 {
    var x: Float = 0f
    var y: Float = 0f

    override fun render(g: Graphics) = layer.render(x, y)
}