package believe.scene

import believe.core.display.Bindable
import believe.core.display.Graphics
import believe.geometry.Point
import believe.geometry.Rectangle
import believe.geometry.mutableRectangle
import believe.react.Observable
import believe.react.Observer

/**
 * An object that transforms the graphics object based on some conditions, like a camera.
 *
 * @param observableFocus the focus of the camera's attention. The camera will continuously re-center itself
 * on this point.
 * @param outerBounds the [Rectangle] defining the maximum area that the camera can travel.
 * @param cameraWidth the width viewed by the camera relative to [outerBounds]' dimensions.
 * @param cameraHeight the height viewed by the camera relative to [outerBounds]' dimensions.
 * @param scaleX the scale at which the camera views the X dimension.
 * @param scaleY the scale at which the camera views the Y dimension.
 */
class Camera(
    private val observableFocus: Observable<Point>,
    private val outerBounds: Rectangle,
    private val cameraWidth: Float,
    private val cameraHeight: Float,
    private val scaleX: Float,
    private val scaleY: Float
) : Bindable {
    private val internalBounds =
        mutableRectangle(x = 0f, y = 0f, width = cameraWidth, height = cameraHeight)
    private val boundsObservers = mutableListOf<Observer<Rectangle>>()

    /**
     * An [Observable] of a [Rectangle] where the [Rectangle] represents the space viewed by the
     * camera.
     */
    val bounds = object : Observable<Rectangle> {
        override fun addObserver(observer: Observer<Rectangle>): Observable<Rectangle> {
            boundsObservers.add(observer)
            return this
        }
    }

    override fun bind() {
        observableFocus.addObserver {
            internalBounds.x = findCameraXPosition(it.x)
            internalBounds.y = findCameraYPosition(it.y)
            boundsObservers.forEach { it.valueChanged(internalBounds) }
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
        g.translate(-internalBounds.x, -internalBounds.y)
    }

    private fun findCameraXPosition(focusX: Float): Float =
        findCameraPosition(outerBounds.x, outerBounds.maxX, internalBounds.width, focusX)

    private fun findCameraYPosition(focusY: Float): Float =
        findCameraPosition(outerBounds.y, outerBounds.maxY, internalBounds.height, focusY)
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
