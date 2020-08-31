package believe.scene

import believe.core.display.Graphics
import believe.map.data.MapData
import believe.physics.manager.PhysicsManager
import dagger.Reusable
import javax.inject.Inject

class LevelMap private constructor(
    val width: Float,
    val height: Float,
    private val rearLayers: List<LevelMapLayer>,
    private val sceneElements: List<SceneElement>,
    private val frontLayers: List<LevelMapLayer>
) : SceneElement {

    override var x: Float = 0f
        set(value) {
            field = value
            rearLayers.forEach { it.x = field }
            frontLayers.forEach { it.x = field }
        }

    override var y: Float = 0f
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

    @Reusable
    class Factory @Inject internal constructor(private val physicsManager: PhysicsManager) {
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
            val generatedEntities = tiledMapData.objectLayers().map { it.generatedMapEntityData() }
            val sceneElements =
                generatedEntities.flatMap { it.sceneElements() } + extraSceneElements

            val rearLayers = mutableListOf<LevelMapLayer>()
            val frontLayers = mutableListOf<LevelMapLayer>()
            tiledMapData.layers().filter { it.isVisible }.forEach {
                val layer = LevelMapLayer(it.layer())
                if (it.isFrontLayer) frontLayers.add(layer) else rearLayers.add(layer)
            }
            val physicsManageables = tiledMapData.layers().flatMap {
                it.generatedMapEntityData().physicsManageables()
            } + generatedEntities.flatMap { it.physicsManageables() }
            physicsManageables.forEach { it.addToPhysicsManager(physicsManager) }

            return LevelMap(
                tiledMapData.width().toFloat(),
                tiledMapData.height().toFloat(),
                rearLayers,
                sceneElements,
                frontLayers
            )
        }
    }
}