package believe.scene

import believe.core.display.Graphics
import believe.core.display.RenderableV2
import believe.map.tiled.TiledMap

/** A renderable component representing a layer of a map.  */
internal class LevelMapLayer constructor(
    private val tiledMap: TiledMap,
    private val layerId: Int,
    var x: Float,
    var y: Float
) : RenderableV2 {

    override fun render(g: Graphics) = tiledMap.render(x.toInt(), y.toInt(), layerId)
}