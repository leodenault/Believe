package believe.gui

/** A grouping of [Focusable] instances that ensures only a single one is ever focused at a time. */
interface FocusableGroup : Bindable {
    /**
     * Adds [focusable] to the group.
     *
     * @return whether [focusable] was added to this group or not.
     */
    fun add(focusable: Focusable): Boolean

    /** A factory for creating instances of [FocusableGroup]. */
    interface Factory {
        /** Returns a new [FocusableGroup] instance. */
        fun create(): FocusableGroup
    }
}