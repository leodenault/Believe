package believe.gui

import believe.audio.Sound
import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.gui.GuiBuilders.numberPicker
import believe.gui.testing.DaggerGuiTestComponent
import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test
import org.newdawn.slick.geom.Shape

internal class NumberPickerV2Test {
    private val graphics: Graphics = mock()
    private val inputAdapter = FakeInputAdapter.create<GuiAction>()
    private val textDisplay: TextDisplay = mock()
    private val arrowPressedSound: Sound = mock()
    private val arrowDepressedSound: Sound = mock()
    private val numberConfirmSound: Sound = mock()
    private val layoutFactory: GuiLayoutFactory = DaggerGuiTestComponent.builder()
        .addGuiConfiguration(MenuSelectionV2.Configuration(inputAdapter, mock(), mock()))
        .addGuiConfiguration(
            NumberPickerV2.Configuration(
                inputAdapter,
                arrowPressedSound,
                arrowDepressedSound,
                numberConfirmSound
            )
        ).build().guiLayoutFactory
    private var layoutBuilder =
        numberPicker { createInnerTextDisplay = { _, _, _, _ -> textDisplay } }
    private val numberPicker: NumberPickerV2 by lazy {
        layoutFactory.create(
            layoutBuilder, Rectangle(x = 100f, y = 200f, width = 500f, height = 200f)
        )
    }

    @Test
    fun render_correctlyDrawsText() {
        numberPicker.render(graphics)

        verify(textDisplay).render(graphics)
    }

    @Test
    fun render_isNotSelected_doesNotRenderArrows() {
        numberPicker.render(graphics)

        verify(graphics, never()).fill(any<Shape>(), any())
    }

    @Test
    fun render_isSelected_drawsArrows() {
        numberPicker.bind()
        numberPicker.focus()
        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)

        numberPicker.render(graphics)

        verify(graphics).fill(check<Shape> {
            with(it) {
                assertThat(centerX).isWithin(TOLERANCE).of(166.25f)
                assertThat(centerY).isWithin(TOLERANCE).of(300f)
                assertThat(width).isWithin(TOLERANCE).of(61.25f)
                assertThat(height).isWithin(TOLERANCE).of(57f)
            }
        }, any())
        verify(graphics).fill(check<Shape> {
            with(it) {
                assertThat(centerX).isWithin(TOLERANCE).of(533.75f)
                assertThat(centerY).isWithin(TOLERANCE).of(300f)
                assertThat(width).isWithin(TOLERANCE).of(61.25f)
                assertThat(height).isWithin(TOLERANCE).of(57f)
            }
        }, any())
    }

    @Test
    fun render_isSelectedAndAtMax_onlyRendersLeftArrow() {
        layoutBuilder = numberPicker {
            this.createInnerTextDisplay = { _, _, _, _ -> textDisplay }
            initialValue = 12
            maxValue = 12
        }
        numberPicker.bind()
        numberPicker.focus()
        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)

        numberPicker.render(graphics)

        verify(graphics, times(1)).fill(any<Shape>(), any())
        verify(graphics).fill(check<Shape> {
            with(it) {
                assertThat(centerX).isWithin(TOLERANCE).of(166.25f)
                assertThat(centerY).isWithin(TOLERANCE).of(300f)
                assertThat(width).isWithin(TOLERANCE).of(61.25f)
                assertThat(height).isWithin(TOLERANCE).of(57f)
            }
        }, any())
    }

    @Test
    fun render_isSelectedAndLeftMax_rendersArrows() {
        layoutBuilder = numberPicker {
            this.createInnerTextDisplay = { _, _, _, _ -> textDisplay }
            initialValue = 12
            maxValue = 12
        }
        numberPicker.bind()
        numberPicker.focus()
        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)
        inputAdapter.actionStarted(GuiAction.SELECT_LEFT)
        inputAdapter.actionEnded(GuiAction.SELECT_LEFT)

        numberPicker.render(graphics)

        verify(graphics, times(2)).fill(any<Shape>(), any())
    }

    @Test
    fun render_isSelectedAndAtMin_onlyRendersRightArrow() {
        layoutBuilder = numberPicker {
            this.createInnerTextDisplay = { _, _, _, _ -> textDisplay }
            initialValue = 12
            minValue = 12
        }
        numberPicker.bind()
        numberPicker.focus()
        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)

        numberPicker.render(graphics)

        verify(graphics, times(1)).fill(any<Shape>(), any())
        verify(graphics).fill(check<Shape> {
            with(it) {
                assertThat(centerX).isWithin(TOLERANCE).of(533.75f)
                assertThat(centerY).isWithin(TOLERANCE).of(300f)
                assertThat(width).isWithin(TOLERANCE).of(61.25f)
                assertThat(height).isWithin(TOLERANCE).of(57f)
            }
        }, any())
    }

    @Test
    fun render_isSelectedAndLeftMin_rendersArrows() {
        layoutBuilder = numberPicker {
            this.createInnerTextDisplay = { _, _, _, _ -> textDisplay }
            initialValue = 12
            minValue = 12
        }
        numberPicker.bind()
        numberPicker.focus()
        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)
        inputAdapter.actionStarted(GuiAction.SELECT_RIGHT)
        inputAdapter.actionEnded(GuiAction.SELECT_RIGHT)

        numberPicker.render(graphics)

        verify(graphics, times(2)).fill(any<Shape>(), any())
    }

    @Test
    fun onSelection_addsListenerForIncrementAndDecrement() {
        numberPicker.bind()
        numberPicker.focus()

        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)

        assertThat(inputAdapter.startListeners[GuiAction.SELECT_LEFT]).hasSize(1)
        assertThat(inputAdapter.startListeners[GuiAction.SELECT_RIGHT]).hasSize(1)
    }

    @Test
    fun onDeselection_removesListenerForIncrementAndDecrementAndPlaysConfirmationSound() {
        numberPicker.bind()
        numberPicker.focus()

        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)
        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)

        assertThat(inputAdapter.startListeners[GuiAction.SELECT_LEFT]).isEmpty()
        assertThat(inputAdapter.startListeners[GuiAction.SELECT_RIGHT]).isEmpty()
        verify(numberConfirmSound).play()
    }

    @Test
    fun focus_highlightsInnerTextDisplay() {
        numberPicker.focus()

        verify(textDisplay).highlight()
    }

    @Test
    fun unfocus_unhighlightsInnerTextDisplay() {
        numberPicker.unfocus()

        verify(textDisplay).unhighlight()
    }

    @Test
    fun incremement_updatesInnerText() {
        val createInnerTextDisplay: (String, Int, GuiLayoutFactory, Rectangle) -> TextDisplay =
            mock {
                on { invoke(any(), any(), any(), any()) } doReturn textDisplay
            }
        layoutBuilder = numberPicker {
            this.createInnerTextDisplay = createInnerTextDisplay
            initialValue = 0
        }
        numberPicker.bind()
        numberPicker.focus()

        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)
        inputAdapter.actionStarted(GuiAction.SELECT_RIGHT)

        verify(createInnerTextDisplay).invoke(any(), eq(1), any(), any())
        verify(textDisplay, times(2)).highlight()
        verify(arrowPressedSound).play()

        inputAdapter.actionEnded(GuiAction.SELECT_RIGHT)

        verify(arrowDepressedSound).play()
    }

    @Test
    fun incremement_atMax_doesNotUpdateText() {
        val createInnerTextDisplay: (String, Int, GuiLayoutFactory, Rectangle) -> TextDisplay =
            mock {
                on { invoke(any(), any(), any(), any()) } doReturn textDisplay
            }
        layoutBuilder = numberPicker {
            this.createInnerTextDisplay = createInnerTextDisplay
            initialValue = 12
            maxValue = 12
        }
        numberPicker.bind()
        numberPicker.focus()

        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)
        inputAdapter.actionStarted(GuiAction.SELECT_RIGHT)

        verify(createInnerTextDisplay, never()).invoke(any(), eq(13), any(), any())

        inputAdapter.actionEnded(GuiAction.SELECT_RIGHT)

        verifyZeroInteractions(arrowPressedSound)
        verifyZeroInteractions(arrowDepressedSound)
    }

    @Test
    fun decrement_updatesInnerText() {
        val createInnerTextDisplay: (String, Int, GuiLayoutFactory, Rectangle) -> TextDisplay =
            mock {
                on { invoke(any(), any(), any(), any()) } doReturn textDisplay
            }
        layoutBuilder = numberPicker {
            this.createInnerTextDisplay = createInnerTextDisplay
            initialValue = 0
        }
        numberPicker.bind()
        numberPicker.focus()

        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)
        inputAdapter.actionStarted(GuiAction.SELECT_LEFT)

        verify(createInnerTextDisplay).invoke(any(), eq(-1), any(), any())
        verify(textDisplay, times(2)).highlight()
        verify(arrowPressedSound).play()

        inputAdapter.actionEnded(GuiAction.SELECT_LEFT)

        verify(arrowDepressedSound).play()
    }

    @Test
    fun decrement_atMin_doesNotUpdateInnerText() {
        val createInnerTextDisplay: (String, Int, GuiLayoutFactory, Rectangle) -> TextDisplay =
            mock {
                on { invoke(any(), any(), any(), any()) } doReturn textDisplay
            }
        layoutBuilder = numberPicker {
            this.createInnerTextDisplay = createInnerTextDisplay
            initialValue = 12
            minValue = 12
        }
        numberPicker.bind()
        numberPicker.focus()

        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)
        inputAdapter.actionStarted(GuiAction.SELECT_LEFT)

        verify(createInnerTextDisplay, never()).invoke(any(), eq(11), any(), any())

        inputAdapter.actionEnded(GuiAction.SELECT_LEFT)

        verifyZeroInteractions(arrowPressedSound)
        verifyZeroInteractions(arrowDepressedSound)
    }

    companion object {
        private const val TOLERANCE = 1f
    }
}
