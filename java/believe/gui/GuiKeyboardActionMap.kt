package believe.gui

import dagger.Reusable
import org.newdawn.slick.Input
import javax.inject.Inject

/** Maps keyboard input to [GuiAction] instances. */
@Reusable
internal class GuiKeyboardActionMap @Inject internal constructor() : (Int) -> GuiAction {
    override fun invoke(key: Int): GuiAction = when (key) {
        Input.KEY_ENTER, Input.KEY_NUMPADENTER -> GuiAction.EXECUTE_ACTION
        Input.KEY_UP -> GuiAction.SELECT_UP
        Input.KEY_DOWN -> GuiAction.SELECT_DOWN
        Input.KEY_RIGHT -> GuiAction.SELECT_RIGHT
        Input.KEY_LEFT -> GuiAction.SELECT_LEFT
        else -> GuiAction.UNKNOWN
    }
}