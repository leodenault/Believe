package believe.scene

import believe.core.display.Graphics
import believe.core.display.RenderableV2
import believe.geometry.Rectangle
import believe.map.data.BackgroundSceneData
import believe.react.Observer

/**
 * A wrapper for an [org.newdawn.slick.Image] that displays repeatedly horizontally in the
 * background of the scene.
 */
class MapBackground internal constructor(
    private val backgroundSceneData: BackgroundSceneData, private val mapHeight: Int
) : RenderableV2, Observer<Rectangle> {

    // The segment of the map through which the image can slide through, expressed as a percentage.
    private val verticalWindowSpace: Float =
        backgroundSceneData.bottomYPosition() - backgroundSceneData.topYPosition()

    private var xMax = 0f
    private var xMin = 0f
    private var y = 0f

    override fun render(g: Graphics) {
        var currentImageX = xMin
        while (currentImageX < xMax) {
            g.drawImage(backgroundSceneData.image(), currentImageX, y)
            currentImageX += backgroundSceneData.image().width.toFloat()
        }
    }

    override fun valueChanged(parentRect: Rectangle) {
        val parentYPositionPercent = parentRect.y / (mapHeight - parentRect.height)
        val yPercent =
            verticalWindowSpace * parentYPositionPercent + backgroundSceneData.topYPosition()
        y = yPercent * (mapHeight - backgroundSceneData.image().height)
        xMin =
            parentRect.x - backgroundSceneData.horizontalSpeedMultiplier() * (parentRect.x % (backgroundSceneData.image().width / backgroundSceneData.horizontalSpeedMultiplier()))
        xMax = parentRect.maxX
    }
}