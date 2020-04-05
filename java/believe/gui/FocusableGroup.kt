package believe.gui

/** A grouping of [Focusable] instances that ensures only a single one is ever focused at a time. */
interface FocusableGroup {
    /**
     * Adds [focusable] to the group.
     *
     * @return whether [focusable] was added to this group or not.
     */
    fun add(focusable: Focusable): Boolean
}