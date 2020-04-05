package believe.gui

import believe.core.display.Renderable
import believe.geometry.Rectangle

/**
 * A builder object that constructs GUI layouts.
 *
 * It is intended to be paired with [GuiBuilders] to provide an intuitive API for constructing GUIs.
 */
interface LayoutBuilder<C> {
    /**
     * Builds the layout based on the contents of this builder instance.
     *
     * @param configuration the configuration object passed to the GUI component output by this
     *     builder. Use this to pass any dependencies that are injected by an outside framework.
     * @param guiLayoutFactory the [GuiLayoutFactory] used in constructing subsequent
     *     [LayoutBuilder] instances such as child GUI components.
     * @param positionData a [Rectangle] used to define the position and dimensions of the GUI
     *     component to be output by the builder.
     */
    fun build(
        configuration: C, guiLayoutFactory: GuiLayoutFactory, positionData: Rectangle
    ): Renderable
}