package believe.gui

import believe.input.InputAdapter
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided

/** A grouping of [Focusable] instances that ensures only a single one is ever focused at a time. */
@AutoFactory
class FocusableGroupImpl internal constructor(
    @Provided inputAdapter: InputAdapter<GuiAction>
) : FocusableGroup {

    private val focusables: MutableList<Focusable> = mutableListOf()

    private var currentFocusableIndex = -1

    init {
        inputAdapter.addListener(object : InputAdapter.Listener<GuiAction> {
            override fun actionStarted(action: GuiAction) {
                if (currentFocusableIndex < 0 || focusables.size < 2) return

                if (action == GuiAction.SELECT_UP) {
                    changeSelection(
                        if (currentFocusableIndex == 0) {
                            focusables.size - 1
                        } else {
                            currentFocusableIndex - 1
                        }
                    )
                } else if (action == GuiAction.SELECT_DOWN) {
                    changeSelection(
                        if (currentFocusableIndex == focusables.size - 1) {
                            0
                        } else {
                            currentFocusableIndex + 1
                        }
                    )
                }
            }

            override fun actionEnded(action: GuiAction) {}
        })
    }

    private fun changeSelection(newIndex: Int) {
        focusables[currentFocusableIndex].unfocus()
        focusables[newIndex].focus()
        currentFocusableIndex = newIndex
    }

    override fun add(focusable: Focusable) = focusables.add(focusable).also {
        if (focusables.size == 1) {
            currentFocusableIndex = 0
            focusables[currentFocusableIndex].focus()
        }
    }
}