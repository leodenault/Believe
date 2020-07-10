package believe.gui

import believe.core.display.Graphics
import believe.geometry.rectangle
import believe.gui.GuiBuilders.textBox
import believe.gui.testing.DaggerGuiTestComponent
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.newdawn.slick.Font

internal class TextBoxTest {
    private val font: Font = mock {
        on { it.getWidth(any()) } doAnswer { (it.arguments[0] as String).length }
        on { it.getHeight(any()) } doReturn STRING_HEIGHT
    }
    private val graphics: Graphics = mock()
    private val layoutFactory: GuiLayoutFactory =
        DaggerGuiTestComponent.builder().addGuiConfiguration(TextBox.Configuration(font)).build()
            .guiLayoutFactory
    private var layoutBuilder: TextBox.Builder = textBox { style = STYLE }
    private val textBox: TextBox by lazy {
        layoutFactory.create(layoutBuilder, CONTAINER)
    }

    @Test
    fun render_unhighlighted_rendersTextNormally() {
        layoutBuilder = textBox {
            +"stuff"
            style = STYLE
        }

        textBox.render(graphics)

        verify(graphics).drawString(eq("stuff"), any(), any(), eq(STYLE.textColour), eq(font))
    }

    @Test
    fun render_highlighted_rendersTextAsHighlighted() {
        layoutBuilder = textBox {
            +"stuff"
            style = STYLE
        }
        textBox.highlight()
        textBox.render(graphics)

        verify(graphics).drawString(
            eq("stuff"), any(), any(), eq(STYLE.highlightedTextColor), eq(font)
        )
    }

    @Test
    fun render_wordIsTooLong_doesNotSplit() {
        layoutBuilder = textBox { +"waylongerthanthewidth" }

        textBox.render(graphics)

        verify(graphics).drawString(eq("waylongerthanthewidth"), any(), any(), any(), any())
    }

    @Test
    fun render_singleLine_centerMiddle_rendersCorrectly() {
        val text = "one two"
        layoutBuilder = textBox { +text }

        textBox.render(graphics)

        verify(graphics).drawString(
            eq(text),
            eq(centeredXPositionOf(text)),
            eq(middleYPositionFor(numLines = 1, lineIndex = 0)),
            any(),
            any()
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

        verify(graphics).drawString(
            eq(text), eq(centeredXPositionOf(text)), eq(CONTAINER.y), any(), any()
        )
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
            eq(text),
            eq(CONTAINER.x),
            eq(middleYPositionFor(numLines = 1, lineIndex = 0)),
            any(),
            any()
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

        verify(graphics).drawString(
            eq(text), eq(CONTAINER.x), eq(CONTAINER.y), any(), any()
        )
    }

    @Test
    fun render_multiline_centerMiddle_rendersCorrectly() {
        layoutBuilder = textBox {
            +"a really long piece of text"
        }

        textBox.render(graphics)

        verify(graphics).drawString(
            eq("a really"),
            eq(centeredXPositionOf("a really")),
            eq(middleYPositionFor(numLines = 3, lineIndex = 0)),
            any(),
            any()
        )
        verify(graphics).drawString(
            eq("long piece"),
            eq(centeredXPositionOf("long piece")),
            eq(middleYPositionFor(numLines = 3, lineIndex = 1)),
            any(),
            any()
        )
        verify(graphics).drawString(
            eq("of text"),
            eq(centeredXPositionOf("of text")),
            eq(middleYPositionFor(numLines = 3, lineIndex = 2)),
            any(),
            any()
        )
    }

    @Test
    fun render_multiline_centerTop_rendersCorrectly() {
        layoutBuilder = textBox {
            +"a really long piece of text"
            verticalAlignment = TextAlignment.Vertical.TOP
        }

        textBox.render(graphics)

        verify(graphics).drawString(
            eq("a really"), eq(centeredXPositionOf("a really")), eq(CONTAINER.y), any(), any()
        )
        verify(graphics).drawString(
            eq("long piece"),
            eq(centeredXPositionOf("long piece")),
            eq(CONTAINER.y + STRING_HEIGHT),
            any(),
            any()
        )
        verify(graphics).drawString(
            eq("of text"),
            eq(centeredXPositionOf("of text")),
            eq(CONTAINER.y + STRING_HEIGHT * 2),
            any(),
            any()
        )
    }

    @Test
    fun render_multiline_leftMiddle_rendersCorrectly() {
        layoutBuilder = textBox {
            +"a really long piece of text"

            horizontalAlignment = TextAlignment.Horizontal.LEFT
        }

        textBox.render(graphics)

        verify(graphics).drawString(
            eq("a really"),
            eq(CONTAINER.x),
            eq(middleYPositionFor(numLines = 3, lineIndex = 0)),
            any(),
            any()
        )
        verify(graphics).drawString(
            eq("long piece"),
            eq(CONTAINER.x),
            eq(middleYPositionFor(numLines = 3, lineIndex = 1)),
            any(),
            any()
        )
        verify(graphics).drawString(
            eq("of text"),
            eq(CONTAINER.x),
            eq(middleYPositionFor(numLines = 3, lineIndex = 2)),
            any(),
            any()
        )
    }

    @Test
    fun render_multiline_leftTop_rendersCorrectly() {
        layoutBuilder = textBox {
            +"a really long piece of text"
            horizontalAlignment = TextAlignment.Horizontal.LEFT
            verticalAlignment = TextAlignment.Vertical.TOP
        }

        textBox.render(graphics)

        verify(graphics).drawString(
            eq("a really"), eq(CONTAINER.x), eq(CONTAINER.y), any(), any()
        )
        verify(graphics).drawString(
            eq("long piece"), eq(CONTAINER.x), eq(CONTAINER.y + STRING_HEIGHT), any(), any()
        )
        verify(graphics).drawString(
            eq("of text"), eq(CONTAINER.x), eq(CONTAINER.y + STRING_HEIGHT * 2), any(), any()
        )
    }

    companion object {
        private val STYLE = TextBoxStyle(textColour = 0x323232, highlightedTextColour = 0x989898)
        private val CONTAINER = rectangle(100f, 1000f, 10f, 10f)
        private const val STRING_HEIGHT = 4

        fun centeredXPositionOf(text: String): Float =
            ((CONTAINER.width - text.length) / 2) + CONTAINER.x

        fun middleYPositionFor(numLines: Int, lineIndex: Int): Float =
            ((CONTAINER.height - (STRING_HEIGHT * numLines)) / 2) + CONTAINER.y + lineIndex * STRING_HEIGHT
    }
}