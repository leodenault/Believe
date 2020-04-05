package believe.gui

/** A UI element that can gain focus. */
interface Focusable {
    /** Signals this UI element that it has be come the focus of the UI. */
    fun focus()
    /** Signals this UI element that it is no longer the focus of the UI. */
    fun unfocus()
}