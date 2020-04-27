package believe.gui

import believe.geometry.Rectangle
import believe.util.Util.changeClipContext
import dagger.Reusable
import org.newdawn.slick.Font
import org.newdawn.slick.Graphics
import javax.inject.Inject

/** A component displaying text within the bounds of a box. */
class TextBox private constructor(
    private val config: Configuration,
    private val style: TextBoxStyle,
    private val rect: Rectangle,
    private val textFragments: List<TextFragment>
) : GuiElement {

    private var textColour = style.textColour

    override fun render(graphics: Graphics) {
        with(graphics) {
            val oldClip = changeClipContext(rect)
            val previousFont = font
            val previousColour = color
            font = config.font
            color = textColour
            textFragments.forEach { drawString(it.text, it.rect.x, it.rect.y) }
            font = previousFont
            color = previousColour
            changeClipContext(oldClip)
        }
    }

    override fun bind() {}

    override fun unbind() {}

    /**
     * Changes the state of this [TextBox] within the context of its surroundings being highlighted.
     */
    fun highlight() {
        textColour = style.highlightedTextColor
    }

    /**
     * Changes the state of this [TextBox] within the context of its surroundings being in a normal
     * state.
     */
    fun unhighlight() {
        textColour = style.textColour
    }

    /** Configures a [TextBox] with dependencies injected through a framework. */
    @Reusable
    class Configuration @Inject internal constructor(
        internal val font: Font
    )

    /** Builds instances of [TextBox]. */
    class Builder : LayoutBuilder<Configuration, TextBox> {
        /** The text displayed within the text box. */
        var text: String = ""
        /**
         * The [TextAlignment.Horizontal] determining how to align text on the X axis.
         *
         * Set to [TextAlignment.Horizontal.CENTERED] by default.
         */
        var horizontalAlignment = TextAlignment.Horizontal.CENTERED
        /**
         * The [TextAlignment.Vertical] determining how to align text on the Y axis.
         *
         * Set to [TextAlignment.Vertical.MIDDLE] by default.
         */
        var verticalAlignment = TextAlignment.Vertical.MIDDLE
        /** The style applied to the text. */
        var style: TextBoxStyle =
            TextBoxStyle(textColour = 0xffffff, highlightedTextColour = 0x000000)

        /** Adds this text to the [TextBox.Builder]. */
        operator fun String.unaryPlus() {
            text = this
        }

        override fun build(
            configuration: Configuration,
            guiLayoutFactory: GuiLayoutFactory,
            positionData: Rectangle
        ): TextBox {
            val fragments: List<String> = chopText(text, configuration.font, positionData.width)
            val textFragmentFactory = TextFragment.Factory(
                configuration.font,
                positionData,
                horizontalAlignment,
                verticalAlignment.calculateYPosition(
                    fragments.sumBy { configuration.font.getHeight(it) }, positionData
                )
            )

            return TextBox(configuration,
                style,
                positionData,
                fragments.mapIndexed { index, fragment ->
                    textFragmentFactory.create(
                        fragment, configuration.font.getHeight(fragment) * index
                    )
                })
        }

        companion object {
            private fun chopText(text: String, font: Font, maxWidth: Float): List<String> {
                val textFragments = mutableListOf<String>()
                var remainingText = text
                while (remainingText.isNotEmpty()) {
                    val fragment = generateFragment(remainingText, font, maxWidth)
                    textFragments.add(fragment)
                    remainingText = remainingText.substring(fragment.length).trim()
                }
                return textFragments
            }

            private fun generateFragment(text: String, font: Font, maxWidth: Float): String {
                val textWidth = font.getWidth(text)
                if (textWidth <= maxWidth) {
                    return text
                }
                // Find the index in the string that guarantees the string text won't exceed the
                // width of the component.
                var sliceIndex = ((maxWidth / textWidth) * text.length).toInt()
                while (font.getWidth(text.substring(0, sliceIndex)) > maxWidth) {
                    sliceIndex--
                }
                // Make sure to only slice the string on spaces. Words should ideally be fully
                // within view unless the word itself is wider than the width of the component.
                while (sliceIndex > -1 && text[sliceIndex] != ' ') {
                    sliceIndex--
                }
                // If we reach index -1, then we know the word is wider than the width of the
                // component. We have no choice but to clip the word.
                return if (sliceIndex < 0) text else text.substring(0, sliceIndex)
            }
        }
    }

    internal class TextFragment private constructor(
        val text: String, internal val rect: Rectangle
    ) {

        internal class Factory(
            private val font: Font,
            private val textBoxRect: Rectangle,
            private val horizontalAlignment: TextAlignment.Horizontal,
            private val baseY: Int
        ) {
            fun create(text: String, y: Int) = TextFragment(
                text, Rectangle(
                    horizontalAlignment.calculateXPosition(
                        font.getWidth(text), textBoxRect
                    ), baseY + y, font.getWidth(text), font.getHeight(text)
                )
            )
        }
    }
}
