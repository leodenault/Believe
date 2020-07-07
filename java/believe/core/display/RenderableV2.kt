package believe.core.display

/** An object that can be drawn to a screen.  */
interface RenderableV2 {
    /**
     * Draws the object to the screen.
     *
     * @param g the [Graphics] instance that will be used to draw this object to the screen.
     */
    fun render(g: Graphics)

    /** @depredated use [render]. */
    @Deprecated("Rendering with Slick Graphics is deprecated.", ReplaceWith("render"))
    fun render(g: org.newdawn.slick.Graphics) = render(Graphics(g))
}