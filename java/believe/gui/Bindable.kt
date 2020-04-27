package believe.gui

/** An object that can be bound and unbound to some element displayed on screen. */
interface Bindable {
    /**
     * Associates any references that need to be made in order for this instance to work properly.
     */
    fun bind()

    /** Removes any references that this instance would otherwise hold onto. */
    fun unbind()
}