package believe.core.display

/** An object that can be drawn to a screen.  */
interface RenderableV2 {
    /**
     * Draws the object to the screen.
     *
     * @param g the [Graphics] instance that will be used to draw this object to the screen.
     */
    fun render(g: Graphics)
}