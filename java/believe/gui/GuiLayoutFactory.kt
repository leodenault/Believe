package believe.gui

import believe.core.display.Renderable
import believe.geometry.Rectangle
import believe.input.InputAdapter
import dagger.Reusable
import org.newdawn.slick.gui.GUIContext
import org.newdawn.slick.util.Log
import javax.inject.Inject

/** Creates the layout of a UI by invoking a [LayoutBuilder]. */
@Reusable
class GuiLayoutFactory @Inject internal constructor(
    @GuiConfigurations val guiConfigurations: Set<*>, val guiContext: GUIContext
) {
    /**
     * Returns a [Renderable] representing a GUI layout.
     *
     * @param layoutBuilder the [LayoutBuilder] to use in creating the layout.
     * @param positionData the [Rectangle] instructing [layoutBuilder] of the area it occupies.
     */
    inline fun <reified C> create(
        layoutBuilder: LayoutBuilder<C>, positionData: Rectangle = Rectangle(
            /* x= */ 0f, /* y= */ 0f, guiContext.width.toFloat(), guiContext.height.toFloat()
        )
    ): Renderable = if (Unit is C) {
        layoutBuilder.build(Unit, this, positionData)
    } else {
        guiConfigurations.singleOrNull { it is C }.let {
            if (it == null) Renderable {}.also {
                Log.error(
                    "Could not find configuration compatible with layout builder '${layoutBuilder::class.simpleName}'."
                )
            }
            else layoutBuilder.build(it as C, this, positionData)
        }
    }
}