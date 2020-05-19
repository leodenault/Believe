package believe.gui

import believe.input.InputAdapter
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided

/** A grouping of [Focusable] instances that ensures only a single one is ever focused at a time. */
@AutoFactory(allowSubclasses = true)
class FocusableGroupImpl internal constructor(
    @Provided private val inputAdapter: InputAdapter<GuiAction>
) : FocusableGroup {

    private val focusables: MutableList<Focusable> = mutableListOf()
    private val selectUp = selectUp@ {
        if (groupIsEmptyOrHasFewerThan2Members()) return@selectUp

        changeSelection(
            if (currentFocusableIndex == 0) {
                focusables.size - 1
            } else {
                currentFocusableIndex - 1
            }
        )
    }
    private val selectDown = selectDown@ {
        if (groupIsEmptyOrHasFewerThan2Members()) return@selectDown

        changeSelection(
            if (currentFocusableIndex == focusables.size - 1) {
                0
            } else {
                currentFocusableIndex + 1
            }
        )
    }

    private fun groupIsEmptyOrHasFewerThan2Members() =
        currentFocusableIndex < 0 || focusables.size < 2

    private var currentFocusableIndex = -1

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

    override fun bind() {
        inputAdapter.addActionStartListener(GuiAction.SELECT_UP, selectUp)
        inputAdapter.addActionStartListener(GuiAction.SELECT_DOWN, selectDown)
    }

    override fun unbind() {
        inputAdapter.removeActionStartListener(GuiAction.SELECT_UP, selectUp)
        inputAdapter.removeActionStartListener(GuiAction.SELECT_DOWN, selectDown)
    }
}