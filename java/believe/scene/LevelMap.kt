package believe.scene

import believe.core.display.Graphics
import believe.map.data.MapData

class LevelMap private constructor(
    initialX: Float,
    initialY: Float,
    val width: Float,
    val height: Float,
    private val rearLayers: List<LevelMapLayer>,
    private val sceneElements: List<SceneElement>,
    private val frontLayers: List<LevelMapLayer>
) : SceneElement {

    override var x: Float = initialX
        set(value) {
            field = value
            rearLayers.forEach { it.x = field }
            frontLayers.forEach { it.x = field }
        }

    override var y: Float = initialY
        set(value) {
            field = value
            rearLayers.forEach { it.y = field }
            frontLayers.forEach { it.y = field }
        }

    override fun render(g: Graphics) {
        rearLayers.forEach { it.render(g) }
        sceneElements.forEach { it.render(g) }
        frontLayers.forEach { it.render(g) }
    }

    override fun bind() = sceneElements.forEach(SceneElement::bind)
    override fun unbind() = sceneElements.forEach(SceneElement::unbind)
    override fun update(delta: Int) = sceneElements.forEach { it.update(delta) }

    companion object {
        /** The X position of the map. */
        var x: Float = 0f
        /** The Y position of the map. */
        var y: Float = 0f

        /**
         * Creates a [LevelMap].
         *
         * @param mapData the [MapData] used in rendering the map to the screen.
         * @param extraSceneElements the list of [SceneElement] instances which will be rendered
         * above any other [SceneElement] instances generated from [MapData]. These elements will be
         * rendered in order.
         */
        fun create(mapData: MapData, extraSceneElements: List<SceneElement>): LevelMap {
            val tiledMapData = mapData.tiledMapData()
            val sceneElements = tiledMapData.objectLayers().flatMap {
                it.generatedMapEntityData().sceneElements()
            } + extraSceneElements

            val rearLayers = mutableListOf<LevelMapLayer>()
            val frontLayers = mutableListOf<LevelMapLayer>()
            tiledMapData.layers().filter { it.isVisible }.forEach {
                val layer = LevelMapLayer(it.layer(), x, y)
                if (it.isFrontLayer) frontLayers.add(layer) else rearLayers.add(layer)
            }

            return LevelMap(
                x,
                y,
                tiledMapData.width().toFloat(),
                tiledMapData.height().toFloat(),
                rearLayers,
                sceneElements,
                frontLayers
            )
        }
    }
}