package believe.gui

import believe.core.display.Graphics
import believe.geometry.rectangle
import believe.gui.GuiBuilders.progressBar
import believe.gui.testing.DaggerGuiTestComponent
import believe.react.Observable
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class ProgressBarV2Test {
    private val layoutFactory: GuiLayoutFactory =
        DaggerGuiTestComponent.builder().build().guiLayoutFactory
    private var layoutBuilder = progressBar {}
    private val progressBar: ProgressBarV2 by lazy {
        layoutFactory.create(layoutBuilder, POSITION_DATA).apply { bind() }
    }
    private val graphics = mock<Graphics>()

    @Test
    fun render_initialProgressUnset_rendersBarWithInitialProgressOfZero() {
        progressBar.render(graphics)

        verify(graphics).draw(eq(POSITION_DATA), any(), eq(LINE_WIDTH)) // Foreground colour.
        verify(graphics).fill(eq(POSITION_DATA), any()) // Background colour.
        with(POSITION_DATA) {
            verify(graphics).fill( // Progress component.
                eq(
                    rectangle(
                        x + LINE_WIDTH, y + LINE_WIDTH, 0f, height - 2 * LINE_WIDTH
                    )
                ), any()
            )
        }
    }

    @Test
    fun render_initialProgressIsSet_rendersBarWithInitialProgress() {
        layoutBuilder = progressBar { initialProgress = 0.5f }

        progressBar.render(graphics)

        verify(graphics).draw(eq(POSITION_DATA), any(), eq(LINE_WIDTH))
        with(POSITION_DATA) {
            verify(graphics).fill(
                eq(
                    rectangle(
                        x + LINE_WIDTH,
                        y + LINE_WIDTH,
                        (width - 2 * LINE_WIDTH) * 0.5f,
                        height - 2 * LINE_WIDTH
                    )
                ), any()
            )
        }
    }

    @Test
    fun build_observableSetInBuilder_addsProgressBarToObservable() {
        val observable = mock<Observable<Float>>()
        layoutBuilder = progressBar { getProgressObservable = { observable } }

        progressBar

        verify(observable).addObserver(any())
    }

    @Test
    fun render_observedValueChanged_rendersBarWithNewProgress() {
        progressBar.updateProgress(0.5f)

        progressBar.render(graphics)

        with(POSITION_DATA) {
            verify(graphics).fill(
                eq(
                    rectangle(
                        x + LINE_WIDTH,
                        y + LINE_WIDTH,
                        (width - 2 * LINE_WIDTH) * 0.5f,
                        height - 2 * LINE_WIDTH
                    )
                ), any()
            )
        }
    }

    @Test
    fun render_observedValueChangedBelowZero_clampsToZero() {
        progressBar.updateProgress(-0.01f)

        progressBar.render(graphics)

        with(POSITION_DATA) {
            verify(graphics).fill(
                eq(
                    rectangle(
                        x + LINE_WIDTH, y + LINE_WIDTH, 0f, height - 2 * LINE_WIDTH
                    )
                ), any()
            )
        }
    }

    @Test
    fun render_observedValueChangedAboveOne_clampsToOne() {
        progressBar.updateProgress(2f)

        progressBar.render(graphics)

        with(POSITION_DATA) {
            verify(graphics).fill(
                eq(
                    rectangle(
                        x + LINE_WIDTH,
                        y + LINE_WIDTH,
                        width - 2 * LINE_WIDTH,
                        height - 2 * LINE_WIDTH
                    )
                ), any()
            )
        }
    }

    companion object {
        private const val LINE_WIDTH = 2f
        private val POSITION_DATA = rectangle(50f, 100f, 104f, 54f)
    }
}
