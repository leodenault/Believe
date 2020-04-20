package believe.gui

import believe.core.display.Renderable
import believe.geometry.Rectangle
import dagger.Reusable
import org.newdawn.slick.gui.GUIContext
import javax.inject.Inject
import kotlin.reflect.KClass

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
    inline fun <reified C, T : Renderable> create(
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