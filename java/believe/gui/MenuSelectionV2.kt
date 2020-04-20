package believe.gui

import believe.audio.Sound
import believe.core.display.Renderable
import believe.geometry.Rectangle
import believe.input.InputAdapter
import dagger.Reusable
import org.newdawn.slick.Graphics
import javax.inject.Inject

/** A GUI object presented in a menu context that can be focused and selected by the user. */
class MenuSelectionV2 private constructor(
    private val configuration: Configuration,
    private val rect: Rectangle,
    private val borderRect: Rectangle,
    private val executeSelectionAction: () -> Unit,
    private val textDisplay: TextDisplay
) : Renderable, Focusable {

    private var style: MenuSelectionStyle = INACTIVE
    private var isFocused = false;

    init {
        configuration.inputAdapter.addListener(object : InputAdapter.Listener<GuiAction> {
            override fun actionStarted(action: GuiAction) {
                if (action == GuiAction.EXECUTE_ACTION && isFocused) {
                    configuration.selectionSound.play()
                    executeSelectionAction()
                }
            }

            override fun actionEnded(action: GuiAction) {}
        })
    }

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
        g.color = style.color
        g.fill(rect)
        g.color = style.borderColor
        g.lineWidth = style.borderSize.toFloat()
        g.drawRect(borderRect.x, borderRect.y, borderRect.width, borderRect.height)
        textDisplay.render(g)
    }

    interface TextDisplay : Renderable {
        fun highlight()
        fun unhighlight()
    }

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
                object : TextDisplay {
                    private val textBox = guiLayoutFactory.create(
                        TextBox.Builder().apply {
                            +text
                            style = TextBoxStyle(
                                textColour = 0xcf0498, highlightedTextColour = 0xffffff
                            )
                        }, rectangle
                    )

                    override fun highlight() = textBox.highlight()

                    override fun unhighlight() = textBox.unhighlight()

                    override fun render(g: Graphics) = textBox.render(g)
                }
            }

        /** The logic executed when the [MenuSelectionV2] is selected. */
        var executeSelectionAction: () -> Unit = {}
        /** A [FocusableGroup] that will manage the state of this [MenuSelectionV2]. */
        var focusableGroup: FocusableGroup? = null

        /** Add this string to the builder. */
        operator fun String.unaryPlus() {
            text = this
        }

        override fun build(
            configuration: Configuration,
            guiLayoutFactory: GuiLayoutFactory,
            positionData: Rectangle
        ): MenuSelectionV2 {
            val borderPositionData: Rectangle = with(positionData) {
                Rectangle(
                    x.toInt() + BORDER_SIZE / 2,
                    y.toInt() + BORDER_SIZE / 2,
                    width.toInt() - BORDER_SIZE,
                    height.toInt() - BORDER_SIZE
                )
            }
            return MenuSelectionV2(
                configuration,
                positionData,
                borderPositionData,
                executeSelectionAction,
                createTextDisplay(guiLayoutFactory, borderPositionData, text)
            ).also { focusableGroup?.add(it) }
        }
    }

    internal class StyleSet(val focused: MenuSelectionStyle, val unfocused: MenuSelectionStyle)

    companion object {
        private const val BORDER_SIZE = 5
        private val INACTIVE =
            MenuSelectionStyle(colour = 0x0cffb2, borderColour = 0xcf0498, borderSize = BORDER_SIZE)
        private val ACTIVE =
            MenuSelectionStyle(colour = 0xff0000, borderColour = 0xffffff, borderSize = BORDER_SIZE)
    }
}