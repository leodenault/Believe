package believe.gui.testing

import believe.core.display.Renderable
import believe.geometry.Rectangle
import believe.gui.GuiLayoutFactory
import believe.gui.LayoutBuilder

/** A fake implementation of [LayoutBuilder] for use in tests. */
class FakeLayoutBuilder<C>(private val builderResult: Renderable) : LayoutBuilder<C> {
    var receivedPositionData: Rectangle? = null

    override fun build(
        configuration: C, guiLayoutFactory: GuiLayoutFactory, positionData: Rectangle
    ): Renderable {
        receivedPositionData = positionData
        return builderResult
    }
}