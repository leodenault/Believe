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
    private val executeSelectionAction: () -> Unit
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
    }

    override fun unfocus() {
        style = configuration.styles.unfocused
        isFocused = false
    }

    override fun render(g: Graphics) {
        g.color = style.color
        g.fill(rect)

        g.color = style.borderColor
        g.lineWidth = style.borderSize.toFloat()
        g.drawRect(
            rect.x + style.borderSize / 2,
            rect.y + style.borderSize / 2,
            rect.width - style.borderSize,
            rect.height - style.borderSize
        )
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
    class Builder : LayoutBuilder<Configuration> {
        /** The logic executed when the [MenuSelectionV2] is selected. */
        var executeSelectionAction: () -> Unit = {}
        /** A [FocusableGroup] that will manage the state of this [MenuSelectionV2]. */
        var focusableGroup: FocusableGroup? = null

        override fun build(
            configuration: Configuration,
            guiLayoutFactory: GuiLayoutFactory,
            positionData: Rectangle
        ): Renderable = MenuSelectionV2(
            configuration, positionData, executeSelectionAction
        ).also { focusableGroup?.add(it) }
    }

    internal class StyleSet(val focused: MenuSelectionStyle, val unfocused: MenuSelectionStyle)

    companion object {
        private val INACTIVE =
            MenuSelectionStyle(colour = 0x0cffb2, borderColour = 0xcf0498, borderSize = 5)
        private val ACTIVE =
            MenuSelectionStyle(colour = 0xff0000, borderColour = 0xffffff, borderSize = 5)
    }
}