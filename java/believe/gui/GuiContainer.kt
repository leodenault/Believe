package believe.gui

import believe.core.display.Renderable
import believe.geometry.Rectangle
import org.newdawn.slick.Graphics
import kotlin.math.round

/** A container for grouping GUI elements together in a common layout. */
class GuiContainer private constructor(
    private val children: List<Renderable>
) : Renderable {

    override fun render(g: Graphics) = children.forEach { it.render(g) }

    /** A builder for constructing instances of [GuiContainer]. */
    class Builder : LayoutBuilder<Unit, GuiContainer> {
        /** The children to be contained within the container built by this builder. */
        val children: MutableList<(GuiLayoutFactory, Rectangle) -> Renderable> = mutableListOf()

        /** Adds the result of the [LayoutBuilder] as a child of this container. */
        inline operator fun <reified T> LayoutBuilder<T, *>.unaryPlus() {
            children.add { guiLayoutFactory, positionData ->
                guiLayoutFactory.create(
                    this, positionData
                )
            }
        }

        override fun build(
            configuration: Unit, guiLayoutFactory: GuiLayoutFactory, positionData: Rectangle
        ): GuiContainer {
            val childPositioner = ChildPositioner(children.size, positionData)
            return GuiContainer(children.map {
                it(
                    guiLayoutFactory, childPositioner.nextChildPositionData()
                )
            })
        }
    }

    internal class ChildPositioner(private val numChildren: Int, positionData: Rectangle) {
        private val spacing = (positionData.height * SPACING_FRACTION) / (numChildren + 1)
        private val width = round(positionData.width / 2).toInt()
        private val height =
            round((positionData.height * (1 - SPACING_FRACTION)) / numChildren).toInt()
        private val x = round(positionData.width / 4).toInt()
        private val baseY = round(positionData.y).toInt()

        private var currentChildIndex = 0

        fun nextChildPositionData(): Rectangle {
            if (currentChildIndex >= numChildren) {
                throw IndexOutOfBoundsException("No more children to position.")
            }

            return Rectangle(
                x,
                baseY + round(spacing + (spacing + height) * currentChildIndex++).toInt(),
                width,
                height
            )
        }

        companion object {
            private const val SPACING_FRACTION = 0.25f
        }
    }
}
