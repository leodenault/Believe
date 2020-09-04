package believe.scene

import believe.core.display.Graphics
import believe.geometry.rectangle
import believe.gui.testing.FakeImage
import believe.map.data.BackgroundSceneData
import believe.map.data.proto.MapMetadataProto
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.jupiter.api.Test

internal class MapBackgroundTest {
    private val graphics = mock<Graphics>()
    private val mapBackground = MapBackground(
        BackgroundSceneData.create(
            IMAGE, MapMetadataProto.MapBackground.newBuilder().setTopYPosition(
                TOP_Y_POSITION
            ).setBottomYPosition(
                BOTTOM_Y_POSITION
            ).setHorizontalSpeedMultiplier(HORIZONTAL_SPEED_MULTIPLIER).build()
        ), mapHeight = 500
    )

    @Test
    fun render_backgroundIsAtTop_rendersAtTopYPosition() {
        mapBackground.valueChanged(
            rectangle(
                x = 0f, y = 0f, width = IMAGE_WIDTH.toFloat(), height = IMAGE_HEIGHT.toFloat()
            )
        )
        mapBackground.render(graphics)

        verify(graphics).drawImage(
            eq(IMAGE), x = any(), y = eq(positionFromPercentage(TOP_Y_POSITION))
        )
    }

    @Test
    fun render_backgroundIsAtBottom_rendersAtBottomYPosition() {
        val parentHeight = 50

        mapBackground.valueChanged(
            rectangle(
                x = 0f,
                y = MAP_HEIGHT - parentHeight.toFloat(),
                width = IMAGE_WIDTH.toFloat(),
                height = parentHeight.toFloat()
            )
        )
        mapBackground.render(graphics)

        verify(graphics).drawImage(
            eq(IMAGE), x = any(), y = eq(positionFromPercentage(BOTTOM_Y_POSITION))
        )
    }

    @Test
    fun render_imageIsRepeatedAcrossParentWidth() {
        val parentWidth = IMAGE_WIDTH * 3

        mapBackground.valueChanged(
            rectangle(
                x = 0f, y = 0f, width = parentWidth.toFloat(), height = IMAGE_HEIGHT.toFloat()
            )
        )
        mapBackground.render(graphics)

        verify(graphics).drawImage(eq(IMAGE), x = eq(0f), y = any())
        verify(graphics).drawImage(eq(IMAGE), x = eq(IMAGE_WIDTH.toFloat()), y = any())
        verify(graphics).drawImage(eq(IMAGE), x = eq(2 * IMAGE_WIDTH.toFloat()), y = any())
        verifyNoMoreInteractions(graphics)
    }

    @Test
    fun render_parentPositionNotAtMultipleOfImageWidth_scrollsImageByHorizontalMultiplier() {
        val parentWidth = IMAGE_WIDTH * 3
        val parentHorizontalScroll = IMAGE_WIDTH
        val imageHorizontalScroll = HORIZONTAL_SPEED_MULTIPLIER * parentHorizontalScroll

        mapBackground.valueChanged(
            rectangle(
                x = parentHorizontalScroll.toFloat(),
                y = 0f,
                width = parentWidth.toFloat(),
                height = IMAGE_HEIGHT.toFloat()
            )
        )
        mapBackground.render(graphics)

        verify(graphics).drawImage(eq(IMAGE), x = eq(imageHorizontalScroll), y = any())
        verify(graphics).drawImage(
            eq(IMAGE), x = eq(IMAGE_WIDTH + imageHorizontalScroll), y = any()
        )
        verify(graphics).drawImage(
            eq(IMAGE), x = eq(2 * IMAGE_WIDTH + imageHorizontalScroll), y = any()
        )
        verify(graphics).drawImage(
            eq(IMAGE), x = eq(3 * IMAGE_WIDTH + imageHorizontalScroll), y = any()
        )
        verifyNoMoreInteractions(graphics)
    }

    companion object {
        private const val MAP_HEIGHT = 500
        private const val IMAGE_WIDTH = 10
        private const val IMAGE_HEIGHT = 20
        private const val TOP_Y_POSITION = 0.1f
        private const val BOTTOM_Y_POSITION = 0.8f
        private const val HORIZONTAL_SPEED_MULTIPLIER = 0.5f
        private val IMAGE = FakeImage(IMAGE_WIDTH, IMAGE_HEIGHT)

        private fun positionFromPercentage(percentage: Float): Float {
            return percentage * (MAP_HEIGHT - IMAGE_HEIGHT)
        }
    }
}