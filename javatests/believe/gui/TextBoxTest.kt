package believe.gui

import believe.geometry.Rectangle
import believe.gui.GuiBuilders.textBox
import believe.gui.testing.DaggerGuiTestComponent
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.newdawn.slick.Color
import org.newdawn.slick.Font
import org.newdawn.slick.Graphics
import kotlin.math.round

internal class TextBoxTest {
    private val initialGraphicsFont: Font = mock()
    private val initialGraphicsColour = Color(0x121212)
    private val font: Font = mock {
        on { it.getWidth(any()) } doAnswer { (it.arguments[0] as String).length }
        on { it.getHeight(any()) } doReturn STRING_HEIGHT
    }
    private val graphics: Graphics = mock {
        on { it.font } doReturn initialGraphicsFont
        on { it.color } doReturn initialGraphicsColour
    }
    private val layoutFactory: GuiLayoutFactory =
        DaggerGuiTestComponent.builder().addGuiConfiguration(TextBox.Configuration(font)).build()
            .guiLayoutFactory
    private var layoutBuilder: TextBox.Builder = textBox { style = STYLE }
    private val textBox: TextBox by lazy {
        layoutFactory.create(layoutBuilder, CONTAINER)
    }

    @Test
    fun render_unhighlighted_rendersTextNormally() {
        textBox.render(graphics)

        verify(graphics).color = STYLE.textColour
    }

    @Test
    fun render_highlighted_rendersTextAsHighlighted() {
        textBox.highlight()
        textBox.render(graphics)

        verify(graphics).color = STYLE.highlightedTextColor
    }

    @Test
    fun render_usesFontAndStyleAndRestoresPrevious() {
        textBox.render(graphics)

        inOrder(graphics) {
            verify(graphics).font = font
            verify(graphics).color = STYLE.textColour
            verify(graphics).font = initialGraphicsFont
            verify(graphics).color = initialGraphicsColour
        }
    }

    @Test
    fun render_wordIsTooLong_doesNotSplit() {
        layoutBuilder = textBox { +"waylongerthanthewidth" }

        textBox.render(graphics)

        verify(graphics).drawString(eq("waylongerthanthewidth"), any(), any())
    }

    @Test
    fun render_singleLine_centerMiddle_rendersCorrectly() {
        val text = "one two"
        layoutBuilder = textBox { +text }

        textBox.render(graphics)

        verify(graphics).drawString(
            text, centeredXPositionOf(text), middleYPositionFor(numLines = 1, lineIndex = 0)
        )
    }

    @Test
    fun render_singleLine_centerTop_rendersCorrectly() {
        val text = "one two"
        layoutBuilder = textBox {
            +text
            verticalAlignment = TextAlignment.Vertical.TOP
        }

        textBox.render(graphics)

        verify(graphics).drawString(text, centeredXPositionOf(text), CONTAINER.y)
    }

    @Test
    fun render_singleLine_leftMiddle_rendersCorrectly() {
        val text = "one two"
        layoutBuilder = textBox {
            +text
            horizontalAlignment = TextAlignment.Horizontal.LEFT
        }

        textBox.render(graphics)

        verify(graphics).drawString(
            text, CONTAINER.x, middleYPositionFor(numLines = 1, lineIndex = 0)
        )
    }

    @Test
    fun render_singleLine_leftTop_rendersCorrectly() {
        val text = "one two"
        layoutBuilder = textBox {
            +text
            horizontalAlignment = TextAlignment.Horizontal.LEFT
            verticalAlignment = TextAlignment.Vertical.TOP
        }

        textBox.render(graphics)

        verify(graphics).drawString(text, CONTAINER.x, CONTAINER.y)
    }

    @Test
    fun render_multiline_centerMiddle_rendersCorrectly() {
        layoutBuilder = textBox {
            +"a really long piece of text"
        }

        textBox.render(graphics)

        inOrder(graphics) {
            verify(graphics).drawString(
                "a really",
                centeredXPositionOf("a really"),
                middleYPositionFor(numLines = 3, lineIndex = 0)
            )
            verify(graphics).drawString(
                "long piece",
                centeredXPositionOf("long piece"),
                middleYPositionFor(numLines = 3, lineIndex = 1)
            )
            verify(graphics).drawString(
                "of text",
                centeredXPositionOf("of text"),
                middleYPositionFor(numLines = 3, lineIndex = 2)
            )
        }
    }

    @Test
    fun render_multiline_centerTop_rendersCorrectly() {
        layoutBuilder = textBox {
            +"a really long piece of text"
            verticalAlignment = TextAlignment.Vertical.TOP
        }

        textBox.render(graphics)

        inOrder(graphics) {
            verify(graphics).drawString(
                "a really", centeredXPositionOf("a really"), CONTAINER.y
            )
            verify(graphics).drawString(
                "long piece", centeredXPositionOf("long piece"), CONTAINER.y + STRING_HEIGHT
            )
            verify(graphics).drawString(
                "of text", centeredXPositionOf("of text"), CONTAINER.y + STRING_HEIGHT * 2
            )
        }
    }

    @Test
    fun render_multiline_leftMiddle_rendersCorrectly() {
        layoutBuilder = textBox {
            +"a really long piece of text"

            horizontalAlignment = TextAlignment.Horizontal.LEFT
        }

        textBox.render(graphics)

        inOrder(graphics) {
            verify(graphics).drawString(
                "a really", CONTAINER.x, middleYPositionFor(numLines = 3, lineIndex = 0)
            )
            verify(graphics).drawString(
                "long piece", CONTAINER.x, middleYPositionFor(numLines = 3, lineIndex = 1)
            )
            verify(graphics).drawString(
                "of text", CONTAINER.x, middleYPositionFor(numLines = 3, lineIndex = 2)
            )
        }
    }

    @Test
    fun render_multiline_leftTop_rendersCorrectly() {
        layoutBuilder = textBox {
            +"a really long piece of text"
            horizontalAlignment = TextAlignment.Horizontal.LEFT
            verticalAlignment = TextAlignment.Vertical.TOP
        }

        textBox.render(graphics)

        inOrder(graphics) {
            verify(graphics).drawString("a really", CONTAINER.x, CONTAINER.y)
            verify(graphics).drawString("long piece", CONTAINER.x, CONTAINER.y + STRING_HEIGHT)
            verify(graphics).drawString("of text", CONTAINER.x, CONTAINER.y + STRING_HEIGHT * 2)
        }
    }

    companion object {
        private val STYLE = TextBoxStyle(textColour = 0x323232, highlightedTextColour = 0x989898)
        private val CONTAINER = Rectangle(100, 1000, 10, 10)
        private const val STRING_HEIGHT = 4

        fun centeredXPositionOf(text: String): Float =
            round(((CONTAINER.width - text.length) / 2) + CONTAINER.x)

        fun middleYPositionFor(numLines: Int, lineIndex: Int): Float =
            round(((CONTAINER.height - (STRING_HEIGHT * numLines)) / 2) + CONTAINER.y + lineIndex * STRING_HEIGHT)
    }
}