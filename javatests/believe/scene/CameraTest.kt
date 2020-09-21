package believe.scene

import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.geometry.floatPoint
import believe.geometry.rectangle
import believe.react.ObservableValue
import believe.react.Observer
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CameraTest {
    private val graphics: Graphics = mock()
    private val focus = ObservableValue.of(floatPoint(INITIAL_FOCUS_X, INITIAL_FOCUS_Y))
    private val camera = Camera(
        focus, BOUNDS, CAMERA_WIDTH, CAMERA_HEIGHT, SCALE_X, SCALE_Y
    )

    @BeforeEach
    fun setUp() {
        camera.bind()
    }

    @Test
    fun pushTransformOn_focusNotUpdated_cameraIsProperlyCenteredOnFocus() {
        camera.pushTransformOn(graphics)

        inOrder(graphics) {
            verify(graphics).pushTransform()
            verify(graphics).scale(SCALE_X, SCALE_Y)
            verify(graphics).translate(
                -INITIAL_FOCUS_X + (CAMERA_WIDTH / 2), -INITIAL_FOCUS_Y + (CAMERA_HEIGHT / 2)
            )
        }
    }

    @Test
    fun pushTransformOn_focusNearLeft_cameraClipsToLeftEdge() {
        focus.setValue(floatPoint(BOUNDS.x, INITIAL_FOCUS_Y))

        camera.pushTransformOn(graphics)

        inOrder(graphics) {
            verify(graphics).pushTransform()
            verify(graphics).scale(SCALE_X, SCALE_Y)
            verify(graphics).translate(-BOUNDS.x, -INITIAL_FOCUS_Y + (CAMERA_HEIGHT / 2))
        }
    }

    @Test
    fun pushTransformOn_focusNearRight_cameraClipsToRightEdge() {
        focus.setValue(floatPoint(BOUNDS.maxX, INITIAL_FOCUS_Y))

        camera.pushTransformOn(graphics)

        inOrder(graphics) {
            verify(graphics).pushTransform()
            verify(graphics).scale(SCALE_X, SCALE_Y)
            verify(graphics).translate(
                -BOUNDS.maxX + CAMERA_WIDTH, -INITIAL_FOCUS_Y + (CAMERA_HEIGHT / 2)
            )
        }
    }

    @Test
    fun pushTransformOn_focusNearTop_cameraClipsToTopEdge() {
        focus.setValue(floatPoint(INITIAL_FOCUS_X, BOUNDS.y))

        camera.pushTransformOn(graphics)

        inOrder(graphics) {
            verify(graphics).pushTransform()
            verify(graphics).scale(SCALE_X, SCALE_Y)
            verify(graphics).translate(-INITIAL_FOCUS_X + (CAMERA_WIDTH / 2), -BOUNDS.y)
        }
    }


    @Test
    fun pushTransformOn_focusNearBottom_cameraClipsToBottomEdge() {
        focus.setValue(floatPoint(INITIAL_FOCUS_X, BOUNDS.maxY))

        camera.pushTransformOn(graphics)

        inOrder(graphics) {
            verify(graphics).pushTransform()
            verify(graphics).scale(SCALE_X, SCALE_Y)
            verify(graphics).translate(
                -INITIAL_FOCUS_X + (CAMERA_WIDTH / 2), -BOUNDS.maxY + CAMERA_HEIGHT
            )
        }
    }

    @Test
    fun cameraPositionChanges_notifiesObservers() {
        val boundsObserver = mock<Observer<Rectangle>>()

        camera.bounds.addObserver(boundsObserver)
        focus.setValue(floatPoint(BOUNDS.centerX, BOUNDS.centerY))

        verify(boundsObserver).valueChanged(check { bounds ->
            assertThat(bounds).isEqualTo(
                rectangle(
                    BOUNDS.centerX - CAMERA_WIDTH / 2,
                    BOUNDS.centerY - CAMERA_HEIGHT / 2,
                    CAMERA_WIDTH,
                    CAMERA_HEIGHT
                )
            )
        })
    }

    companion object {
        private val BOUNDS = rectangle(0f, 0f, 500f, 1000f)
        private const val CAMERA_WIDTH = 100f
        private const val CAMERA_HEIGHT = 50f
        private const val INITIAL_FOCUS_X = 60f
        private const val INITIAL_FOCUS_Y = 30f
        private const val SCALE_X = 2f
        private const val SCALE_Y = 3f
    }
}
