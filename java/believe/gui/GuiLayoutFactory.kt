package believe.gui

import believe.geometry.Rectangle
import dagger.Reusable
import org.newdawn.slick.gui.GUIContext
import javax.inject.Inject

/** Creates the layout of a UI by invoking a [LayoutBuilder]. */
@Reusable
class GuiLayoutFactory @Inject internal constructor(
    @GuiConfigurations val guiConfigurations: Set<*>, val guiContext: GUIContext
) {
    /**
     * Returns a [T] representing a GUI layout.
     *
     * @param layoutBuilder the [LayoutBuilder] to use in creating the layout.
     * @param positionData the [Rectangle] instructing [layoutBuilder] of the area it occupies.
     * @param C the type of configuration used to instantiate elements built by [layoutBuilder].
     * @param T the type of [GuiElement] created by [layoutBuilder].
     */
    inline fun <reified C, T : GuiElement> create(
        layoutBuilder: LayoutBuilder<C, T>, positionData: Rectangle = Rectangle(
            x = 0, y = 0, width = guiContext.width, height = guiContext.height
        )
    ): T = if (Unit is C) {
        layoutBuilder.build(Unit, this, positionData)
    } else {
        guiConfigurations.singleOrNull { it is C }.let {
            if (it == null) {
                throw IllegalStateException(
                    "Could not find configuration compatible with layout builder '${layoutBuilder::class.qualifiedName}'."
                )
            }
            layoutBuilder.build(it as C, this, positionData)
        }
    }
}