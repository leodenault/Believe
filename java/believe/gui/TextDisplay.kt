package believe.gui

/** A [GuiElement] with the ability to display text. */
interface TextDisplay : GuiElement {
    /**
     * Changes the state of this [TextDisplay] within the context of its surroundings being
     * highlighted.
     */
    fun highlight()

    /**
     * Changes the state of this [TextDisplay] within the context of its surroundings being in a
     * normal state.
     */
    fun unhighlight()
}