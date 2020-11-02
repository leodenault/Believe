package believe.gui

import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.geometry.mutableRectangle
import believe.react.Observable
import believe.react.ObservableValue
import org.newdawn.slick.Color

/** A bar that fills up or empties as progress if being made by some process. */
class ProgressBarV2 private constructor(
    private val rectangle: Rectangle,
    private val initialProgress: Float = 0f,
    private val getProgressObservable: () -> Observable<Float>
) : GuiElement {
    private val maxProgressWidth = rectangle.width - 2 * LINE_WIDTH
    private val progressRectangle = with(rectangle) {
        mutableRectangle(
            x + LINE_WIDTH,
            y + LINE_WIDTH,
            computeProgressWidth(initialProgress),
            height - 2 * LINE_WIDTH
        )
    }

    override fun render(g: Graphics) {
        with(g) {
            fill(rectangle, BACKGROUND_COLOUR)
            draw(rectangle, FOREGROUND_COLOUR, LINE_WIDTH)
            fill(progressRectangle, FOREGROUND_COLOUR)
        }
    }

    override fun bind() {
        getProgressObservable().addObserver(this::updateProgress)
    }

    override fun unbind() {
    }

    fun updateProgress(progress: Float) {
        progressRectangle.width = computeProgressWidth(progress)
    }

    private fun computeProgressWidth(progress: Float): Float = when {

        progress <= 0f -> 0f
        progress >= 1f -> maxProgressWidth
        else -> progress * maxProgressWidth
    }

    companion object {
        private val FOREGROUND_COLOUR = Color(0x0066ff)
        private val BACKGROUND_COLOUR = Color(0x000000)
        private const val LINE_WIDTH = 2f
    }

    class Builder : LayoutBuilder<Unit, ProgressBarV2> {
        var getProgressObservable: () -> Observable<Float> = {
            ObservableValue.of(initialProgress)
        }

        var initialProgress = 0f

        operator fun String.unaryPlus() {}

        override fun build(
            configuration: Unit, guiLayoutFactory: GuiLayoutFactory, positionData: Rectangle
        ): ProgressBarV2 = ProgressBarV2(positionData, initialProgress, getProgressObservable)
    }
}
