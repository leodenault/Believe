package believe.scene

import believe.core.display.Graphics
import believe.geometry.rectangle
import believe.gui.testing.FakeImage
import believe.map.data.BackgroundSceneData
import believe.map.data.GeneratedMapEntityData
import believe.map.data.LayerData
import believe.map.data.MapData
import believe.map.data.ObjectLayerData
import believe.map.data.TiledMapData
import believe.map.data.proto.MapMetadataProto
import believe.map.tiled.Layer
import believe.physics.manager.PhysicsManageable
import believe.physics.manager.PhysicsManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test

internal class LevelMapTest {
    private val backgroundImage = FakeImage(width = 10, height = 10)
    private val physicsManager = mock<PhysicsManager>()
    private val layerPhysicsManageable = mock<PhysicsManageable>()
    private val objectPhysicsManageable = mock<PhysicsManageable>()
    private val objectSceneElement = mock<SceneElement>()
    private val visibleFrontLayer = mock<Layer>()
    private val visibleRearLayer = mock<Layer>()
    private val invisibleBackLayer = mock<Layer>()
    private val invisibleBackLayerWithData = mock<Layer>()
    private val levelMap = LevelMap.Factory(physicsManager).create(
        MapData.newBuilder(
            TiledMapData.newBuilder(0, 0, 100, 100).addLayer(
                LayerData.newBuilder(
                    visibleFrontLayer
                ).setIsFrontLayer(true).setIsVisible(true).build()
            ).addLayer(
                LayerData.newBuilder(
                    visibleRearLayer
                ).setIsFrontLayer(false).setIsVisible(true).build()
            ).addLayer(
                LayerData.newBuilder(
                    invisibleBackLayer
                ).setIsFrontLayer(false).setIsVisible(false).build()
            ).addLayer(
                LayerData.newBuilder(
                    invisibleBackLayerWithData
                ).setIsFrontLayer(false).setIsVisible(false).setGeneratedMapEntityData(
                    GeneratedMapEntityData.newBuilder().addPhysicsManageable(
                        layerPhysicsManageable
                    ).build()
                ).build()
            ).addObjectLayer(
                ObjectLayerData.create(
                    GeneratedMapEntityData.newBuilder().addPhysicsManageable(
                        objectPhysicsManageable
                    ).addSceneElement(objectSceneElement).build()
                )
            ).build()
        ).addBackgroundScene(
            BackgroundSceneData.create(
                backgroundImage,
                MapMetadataProto.MapBackground.newBuilder().setTopYPosition(0f).setBottomYPosition(
                    1f
                ).setHorizontalSpeedMultiplier(1f).build()
            )
        ).build(), emptyList()
    )

    @Test
    fun bind_bindsSceneElements() {
        levelMap.bind()

        verify(objectSceneElement).bind()
    }

    @Test
    fun unbind_unbindsSceneElements() {
        levelMap.unbind()

        verify(objectSceneElement).unbind()
    }

    @Test
    fun render_rendersSubElements() {
        val graphics = mock<Graphics>()
        levelMap.valueChanged(rectangle(1f, 2f, 3f, 4f))

        levelMap.render(graphics)

        inOrder(graphics, visibleRearLayer, objectSceneElement, visibleFrontLayer) {
            verify(graphics).drawImage(eq(backgroundImage), any(), any())
            verify(visibleRearLayer).render(0f, 0f)
            verify(objectSceneElement).render(graphics)
            verify(visibleFrontLayer).render(0f, 0f)
        }
        verifyZeroInteractions(invisibleBackLayer)
        verifyZeroInteractions(invisibleBackLayerWithData)
    }

    @Test
    fun update_updatesSceneElements() {
        levelMap.update(456)

        verify(objectSceneElement).update(456)
    }

    @Test
    fun valueChanged_updatesMapBackgrounds() {
        val graphics = mock<Graphics>()

        levelMap.valueChanged(rectangle(x = 1f, y = 2f, width = 1000f, height = 500f))
        levelMap.render(graphics)

        verify(graphics).drawImage(backgroundImage, 0f, -0.45f)
    }

    @Test
    fun setX_updatesLayers() {
        val graphics = mock<Graphics>()

        levelMap.x = 123f
        levelMap.render(graphics)

        verify(visibleRearLayer).render(123f, 0f)
    }

    @Test
    fun setY_updatesLayers() {
        val graphics = mock<Graphics>()

        levelMap.y = 123f
        levelMap.render(graphics)

        verify(visibleRearLayer).render(0f, 123f)
    }
}
