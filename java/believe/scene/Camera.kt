package believe.scene

import believe.core.display.Bindable
import believe.core.display.Graphics
import believe.geometry.Point
import believe.geometry.Rectangle
import believe.geometry.rectangle
import believe.react.Observable

/**
 * An object that transforms the graphics object based on some conditions, like a camera.
 *
 * @param observableFocus the focus of the camera's attention. The camera will continuously re-center itself
 * on this point.
 * @param bounds the [Rectangle] defining the maximum area that the camera can travel.
 * @param cameraWidth the width viewed by the camera relative to [bounds]' dimensions.
 * @param cameraHeight the height viewed by the camera relative to [bounds]' dimensions.
 * @param scaleX the scale at which the camera views the X dimension.
 * @param scaleY the scale at which the camera views the Y dimension.
 */
class Camera(
    private val observableFocus: Observable<Point>,
    private val bounds: Rectangle,
    private val cameraWidth: Float,
    private val cameraHeight: Float,
    private val scaleX: Float,
    private val scaleY: Float
) : Bindable {
    private var x = 0f
    private var y = 0f

    override fun bind() {
        observableFocus.addObserver {
            x = findCameraXPosition(it.x)
            y = findCameraYPosition(it.y)
        }
    }

    override fun unbind() {}

    /**
     * Pushes a set of transformations onto [g].
     *
     * **Don't forget to call [Graphics.popTransform] afterwards.**
     */
    fun pushTransformOn(g: Graphics) {
        g.pushTransform()
        g.scale(scaleX, scaleY)
        g.translate(-x, -y)
    }

    private fun findCameraXPosition(focusX: Float): Float =
        findCameraPosition(bounds.x, bounds.maxX, cameraWidth, focusX)

    private fun findCameraYPosition(focusY: Float): Float =
        findCameraPosition(bounds.y, bounds.maxY, cameraHeight, focusY)
}

private fun findCameraPosition(
    minBound: Float, maxBound: Float, cameraCoverage: Float, focusValue: Float
): Float {
    val minDistanceFromEdge = cameraCoverage / 2
    return when {
        focusValue < minBound + minDistanceFromEdge -> minBound
        focusValue > maxBound - minDistanceFromEdge -> maxBound - cameraCoverage
        else -> focusValue - minDistanceFromEdge
    }
}
