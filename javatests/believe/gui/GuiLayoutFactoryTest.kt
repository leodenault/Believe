package believe.gui

import believe.geometry.Rectangle
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test
import org.newdawn.slick.gui.GUIContext

internal class GuiLayoutFactoryTest {
    private val guiContext: GUIContext = mock() {
        on { width } doReturn GUI_CONTEXT_WIDTH
        on { height } doReturn GUI_CONTEXT_HEIGHT
    }
    private val factory = GuiLayoutFactory(setOf(CONTEXT_1, CONTEXT_2), guiContext)
    private val builder1: LayoutBuilder<String> = mock()
    private val builder2: LayoutBuilder<Int> = mock()
    private val unitBuilder: LayoutBuilder<Unit> = mock()
    private val noTypeBuilder: LayoutBuilder<Pair<Int, String>> = mock()

    @Test
    fun create_correctlyInvokesBuilder() {
        factory.create(builder1, POSITION_DATA)
        factory.create(builder2, POSITION_DATA)

        verify(builder1).build(CONTEXT_1, factory, POSITION_DATA)
        verify(builder2).build(CONTEXT_2, factory, POSITION_DATA)
    }

    @Test
    fun create_noPositionDataSupplied_usesGuiContextDimensions() {
        factory.create(builder1)

        verify(builder1).build(eq(CONTEXT_1), eq(factory), check {
            assertThat(it.x).isEqualTo(0f)
            assertThat(it.y).isEqualTo(0f)
            assertThat(it.width).isEqualTo(GUI_CONTEXT_WIDTH.toFloat())
            assertThat(it.height).isEqualTo(GUI_CONTEXT_HEIGHT.toFloat())
        })
    }

    @Test
    fun create_builderConfigurationIsUnit_succeedsInvocation() {
        factory.create(unitBuilder)

        verify(unitBuilder).build(eq(Unit), eq(factory), any())
    }

    @Test
    @VerifiesLoggingCalls
    fun create_builderDoesNotHaveMatchingConfiguration_doesNotInvokeAndLogsError(
        logSystem: VerifiableLogSystem
    ) {
        factory.create(noTypeBuilder)

        verifyZeroInteractions(noTypeBuilder)
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasSeverity(LogSeverity.ERROR).hasPattern(
                "Could not find configuration compatible with layout builder '${Regex.escape(
                    noTypeBuilder::class.simpleName!!
                )}'."
            )
    }

    companion object {
        private const val CONTEXT_1 = "context 1"
        private const val CONTEXT_2 = 123
        private const val GUI_CONTEXT_WIDTH = 190
        private const val GUI_CONTEXT_HEIGHT = 908
        private val POSITION_DATA = Rectangle(1f, 2f, 3f, 4f)
    }
}
