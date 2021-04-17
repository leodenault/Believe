package believe.scene

import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.map.data.MapData
import believe.physics.manager.PhysicsManager
import believe.react.Observer
import dagger.Reusable
import javax.inject.Inject

class LevelMap private constructor(
    val width: Float,
    val height: Float,
    private val mapBackgrounds: List<MapBackground>,
    private val rearLayers: List<LevelMapLayer>,
    private val sceneElements: List<SceneElement>,
    private val frontLayers: List<LevelMapLayer>
) : SceneElement, Observer<Rectangle> {

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
        mapBackgrounds.forEach { it.render(g) }
        rearLayers.forEach { it.render(g) }
        sceneElements.forEach { it.render(g) }
        frontLayers.forEach { it.render(g) }
    }

    override fun bind() = sceneElements.forEach(SceneElement::bind)
    override fun unbind() = sceneElements.forEach(SceneElement::unbind)
    override fun update(delta: Long) = sceneElements.forEach { it.update(delta) }

    override fun valueChanged(newValue: Rectangle) {
        mapBackgrounds.forEach { it.valueChanged(newValue) }
    }

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
        fun create(mapData: MapData, extraSceneElements: Set<SceneElement>): LevelMap {
            val tiledMapData = mapData.tiledMapData()
            val generatedEntities = GeneratedMapEntityData.newBuilder().apply {
                tiledMapData.objectLayers().flatMap {
                    it.objectFactories()
                }.forEach { it.createAndAddTo(this) }
            }.build()
            val sceneElements = (generatedEntities.sceneElements() + extraSceneElements).toList()

            val layerData =
                tiledMapData.layers().filter { it.isVisible }.partition { it.isFrontLayer }
            generatedEntities.physicsManageables()
                .forEach { it.addToPhysicsManager(physicsManager) }

            val mapHeight = tiledMapData.height()
            return LevelMap(tiledMapData.width().toFloat(),
                mapHeight.toFloat(),
                mapData.backgroundScenes().map { MapBackground(it, mapHeight) },
                layerData.second.map { LevelMapLayer(it.layer()) },
                sceneElements,
                layerData.first.map { LevelMapLayer(it.layer()) })
        }
    }
}