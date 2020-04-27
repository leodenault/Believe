package believe.gui

import believe.geometry.Rectangle
import org.newdawn.slick.Graphics
import javax.inject.Inject
import kotlin.math.round

/** A container for grouping GUI elements together in a common layout. */
class GuiContainer private constructor(
    private val focusableGroup: FocusableGroup, private val children: List<GuiElement>
) : GuiElement {
    override fun render(g: Graphics) = children.forEach { it.render(g) }

    override fun bind() {
        children.forEach { it.bind() }
        focusableGroup.bind()
    }

    override fun unbind() {
        children.forEach { it.unbind() }
        focusableGroup.unbind()
    }

    class Configuration @Inject internal constructor(
        internal val focusableGroupImplFactory: FocusableGroup.Factory
    )

    /** A builder for constructing instances of [GuiContainer]. */
    class Builder : LayoutBuilder<Configuration, GuiContainer> {
        /** The children to be contained within the container built by this builder. */
        val children: MutableList<(GuiLayoutFactory, Rectangle, FocusableGroup) -> GuiElement> =
            mutableListOf()

        /** Visible for testing. */
        internal var focusableGroup: FocusableGroup? = null

        /** Adds the result of the [LayoutBuilder] as a child of this container. */
        inline operator fun <reified C, T> LayoutBuilder<C, T>.unaryPlus() where T : GuiElement, T : Focusable {
            children.add { guiLayoutFactory, positionData, focusableGroup ->
                guiLayoutFactory.create(
                    this, positionData
                ).also { focusableGroup.add(it) }
            }
        }

        override fun build(
            configuration: Configuration,
            guiLayoutFactory: GuiLayoutFactory,
            positionData: Rectangle
        ): GuiContainer {
            val childPositioner = ChildPositioner(children.size, positionData)
            val focusableGroup = focusableGroup ?: configuration.focusableGroupImplFactory.create()
            return GuiContainer(focusableGroup, children.map {
                it(
                    guiLayoutFactory, childPositioner.nextChildPositionData(), focusableGroup
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
