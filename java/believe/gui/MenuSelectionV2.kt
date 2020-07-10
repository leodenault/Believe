package believe.gui

import believe.audio.Sound
import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.geometry.rectangle
import believe.input.InputAdapter
import dagger.Reusable
import javax.inject.Inject

/** A GUI object presented in a menu context that can be focused and selected by the user. */
class MenuSelectionV2 private constructor(
    private val configuration: Configuration,
    private val rect: Rectangle,
    private val borderRect: Rectangle,
    private val executeSelectionAction: () -> Unit,
    private val textDisplay: TextDisplay
) : GuiElement, Focusable {

    private val executeSelection = {
        if (isFocused) {
            configuration.selectionSound.play()
            executeSelectionAction()
        }
    }

    private var style: MenuSelectionStyle = INACTIVE
    private var isFocused = false

    override fun focus() {
        configuration.focusSound.play()
        style = configuration.styles.focused
        isFocused = true
        textDisplay.highlight()
    }

    override fun unfocus() {
        style = configuration.styles.unfocused
        isFocused = false
        textDisplay.unhighlight()
    }

    override fun render(g: Graphics) {
        g.fill(rect, style.color)
        g.draw(borderRect, style.borderColor, style.borderSize.toFloat())
        textDisplay.render(g)
    }

    override fun bind() = configuration.inputAdapter.addActionStartListener(
        GuiAction.EXECUTE_ACTION, executeSelection
    )

    override fun unbind() = configuration.inputAdapter.removeActionStartListener(
        GuiAction.EXECUTE_ACTION, executeSelection
    )

    /** Configures a [MenuSelectionV2] with dependencies injected from a framework. */
    @Reusable
    class Configuration @Inject internal constructor(
        internal val inputAdapter: InputAdapter<GuiAction>,
        @FocusSound
        internal val focusSound: Sound, @SelectedSound internal val selectionSound: Sound
    ) {
        internal val styles: StyleSet = StyleSet(focused = ACTIVE, unfocused = INACTIVE)
    }

    /** A builder for creating instances of [MenuSelectionV2]. */
    class Builder : LayoutBuilder<Configuration, MenuSelectionV2> {
        private var text: String = ""

        /** Visible for testing. */
        internal var createTextDisplay: (GuiLayoutFactory, Rectangle, String) -> TextDisplay =
            { guiLayoutFactory, rectangle, text ->
                guiLayoutFactory.create(
                    TextBox.Builder().apply {
                        +text
                        style = TextBoxStyle(
                            textColour = 0xcf0498, highlightedTextColour = 0xffffff
                        )
                    }, rectangle
                )
            }

        /** The logic executed when the [MenuSelectionV2] is selected. */
        var executeSelectionAction: () -> Unit = {}

        /** Add this string to the builder. */
        operator fun String.unaryPlus() {
            text = this
        }

        override fun build(
            configuration: Configuration,
            guiLayoutFactory: GuiLayoutFactory,
            positionData: Rectangle
        ): MenuSelectionV2 {
            return MenuSelectionV2(configuration, positionData, with(positionData) {
                rectangle(
                    x + BORDER_SIZE / 2,
                    y + BORDER_SIZE / 2,
                    width - BORDER_SIZE,
                    height - BORDER_SIZE
                )
            }, executeSelectionAction, createTextDisplay(guiLayoutFactory, with(positionData) {
                rectangle(
                    x + BORDER_SIZE,
                    y + BORDER_SIZE,
                    width - BORDER_SIZE * 2,
                    height - BORDER_SIZE * 2
                )
            }, text)
            )
        }
    }

    internal class StyleSet(val focused: MenuSelectionStyle, val unfocused: MenuSelectionStyle)

    companion object {
        private const val BORDER_SIZE = 6
        private val INACTIVE =
            MenuSelectionStyle(colour = 0x0cffb2, borderColour = 0xcf0498, borderSize = BORDER_SIZE)
        private val ACTIVE =
            MenuSelectionStyle(colour = 0xff0000, borderColour = 0xffffff, borderSize = BORDER_SIZE)
    }
}