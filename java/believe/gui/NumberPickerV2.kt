package believe.gui

import believe.audio.Sound
import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.geometry.rectangle
import believe.input.InputAdapter
import org.newdawn.slick.Color
import org.newdawn.slick.geom.Polygon
import org.newdawn.slick.geom.Shape
import org.newdawn.slick.geom.Transform
import javax.inject.Inject

class NumberPickerV2 private constructor(
    private val menuSelection: MenuSelectionV2
) : GuiElement, Focusable {

    override fun render(g: Graphics) = menuSelection.render(g)
    override fun bind() = menuSelection.bind()
    override fun unbind() = menuSelection.unbind()
    override fun focus() = menuSelection.focus()
    override fun unfocus() = menuSelection.unfocus()

    class Configuration @Inject internal constructor(
        internal val inputAdapter: InputAdapter<GuiAction>,
        @ArrowPressedSound
        internal val arrowPressSound: Sound,
        @ArrowDepressedSound
        internal val arrowDepressSound: Sound, @SelectedSound internal val numberConfirmSound: Sound
    )

    class Builder : LayoutBuilder<Configuration, NumberPickerV2> {
        private var text = ""

        /** The initial value displayed in the number picker. */
        var initialValue = 0

        /** The minimum value that can be selected. */
        var minValue = Int.MIN_VALUE

        /** The maximum value that can be selected. */
        var maxValue = Int.MAX_VALUE

        /** Adds this text to be displayed above the number being picked. */
        operator fun String.unaryPlus() {
            text = this
        }

        /** The callback to execute when a number has been confirmed. */
        var confirmNumber: (Int) -> Unit = {}

        /** Visible for testing. */
        internal var createInnerTextDisplay: (
            String, Int, GuiLayoutFactory, Rectangle
        ) -> TextDisplay = { text, value, guiLayoutFactory, positionData ->
            InternalText.create(text, value, guiLayoutFactory, positionData)
        }

        override fun build(
            configuration: Configuration,
            guiLayoutFactory: GuiLayoutFactory,
            positionData: Rectangle
        ): NumberPickerV2 {
            var textDisplay: NumberPickerTextDisplay? = null

            return NumberPickerV2(
                guiLayoutFactory.create(
                    MenuSelectionV2.Builder().apply {
                        executeSelectionAction = { textDisplay?.activate() }
                        createTextDisplay = { _, positionData, _ ->
                            NumberPickerTextDisplay(
                                configuration,
                                guiLayoutFactory,
                                positionData,
                                createInnerTextDisplay,
                                text,
                                initialValue,
                                minValue,
                                maxValue,
                                StyleSet(ACTIVE, INACTIVE),
                                confirmNumber
                            ).also { textDisplay = it }
                        }
                    }, positionData
                )
            )
        }
    }

    private class InternalText private constructor(
        private val container: CanvasContainerV2<TextBox>
    ) : TextDisplay {

        override fun highlight() = container.children.forEach(TextBox::highlight)

        override fun unhighlight() = container.children.forEach(TextBox::unhighlight)

        override fun render(g: Graphics) = container.render(g)

        override fun bind() = container.bind()

        override fun unbind() = container.unbind()

        companion object {
            fun create(
                infoText: String,
                value: Int,
                guiLayoutFactory: GuiLayoutFactory,
                positionData: Rectangle
            ): InternalText {
                val textBoxStyle = TextBoxStyle(
                    textColour = 0xcf0498, highlightedTextColour = 0xffffff
                )
                val infoTextBox = TextBox.Builder().apply {
                    +infoText
                    style = textBoxStyle
                }
                val valueTextBox = TextBox.Builder().apply {
                    +"$value"
                    style = textBoxStyle
                }
                val container: CanvasContainerV2<TextBox> =
                    guiLayoutFactory.create(CanvasContainerV2.Builder<TextBox>().apply {
                        add { infoTextBox from p(0f, 0.25f) to p(1f, 0.5f) }
                        add { valueTextBox from p(0f, 0.5f) to p(1f, 0.75f) }
                    }, positionData)
                return InternalText(container)
            }
        }
    }

    private class NumberPickerTextDisplay(
        private val configuration: Configuration,
        guiLayoutFactory: GuiLayoutFactory,
        positionData: Rectangle,
        createTextDisplay: (String, Int, GuiLayoutFactory, Rectangle) -> TextDisplay,
        text: String,
        initialValue: Int,
        minValue: Int,
        maxValue: Int,
        styleSet: StyleSet,
        private val confirmNumber: (Int) -> Unit
    ) : TextDisplay {

        private val leftArrow: Arrow
        private val rightArrow: Arrow
        private val boxedValue: BoxedValue
        private val updateTextDisplay: (Int) -> Unit

        private var valueTextDisplay: TextDisplay

        init {
            val leftArrowCenterX = positionData.x + positionData.width * (ARROW_RELATIVE_WIDTH / 2)
            val rightArrowCenterX =
                positionData.maxX - positionData.width * (ARROW_RELATIVE_WIDTH / 2)
            val textAreaWidth = TEXT_RELATIVE_WIDTH * positionData.width
            val textAreaPositionData = rectangle(
                positionData.centerX - (textAreaWidth / 2),
                positionData.y,
                textAreaWidth,
                positionData.height
            )
            updateTextDisplay = { newValue ->
                valueTextDisplay = createTextDisplay(
                    text, newValue, guiLayoutFactory, textAreaPositionData
                )
                valueTextDisplay.highlight()
            }
            boxedValue = BoxedValue(
                initialValue,
                minValue,
                maxValue,
                updateText = updateTextDisplay,
                handleMinReached = this::hideLeftArrow,
                handleMinLeft = this::showLeftArrow,
                handleMaxReached = this::hideRightArrow,
                handleMaxLeft = this::showRightArrow
            )
            valueTextDisplay =
                createTextDisplay(text, initialValue, guiLayoutFactory, textAreaPositionData)
            val arrowTransform = Transform.createScaleTransform(
                ARROW_RELATIVE_WIDTH * positionData.width, positionData.height
            )
            leftArrow = Arrow(
                IDENTITY_ARROW.transform(arrowTransform).apply {
                    centerX = leftArrowCenterX
                    centerY = positionData.centerY
                }, styleSet, GuiAction.SELECT_LEFT, configuration, boxedValue::decrement
            )
            rightArrow = Arrow(
                IDENTITY_ARROW.transform(
                    arrowTransform.concatenate(Transform.createScaleTransform(-1f, 1f))
                ).apply {
                    centerX = rightArrowCenterX
                    centerY = positionData.centerY
                }, styleSet, GuiAction.SELECT_RIGHT, configuration, boxedValue::increment
            )
        }

        override fun highlight() {
            valueTextDisplay.highlight()
        }

        override fun unhighlight() {
            valueTextDisplay.unhighlight()
        }

        override fun render(g: Graphics) {
            valueTextDisplay.render(g)
            leftArrow.render(g)
            rightArrow.render(g)
        }

        override fun bind() {}

        override fun unbind() {}

        fun activate() {
            configuration.inputAdapter.pushListeners()
            configuration.inputAdapter.addActionStartListener(
                GuiAction.EXECUTE_ACTION, this::deactivate
            )
            if (!boxedValue.atMin()) leftArrow.makeVisible()
            if (!boxedValue.atMax()) rightArrow.makeVisible()
        }

        fun deactivate() {
            leftArrow.makeInvisible()
            rightArrow.makeInvisible()
            configuration.inputAdapter.removeActionStartListener(
                GuiAction.EXECUTE_ACTION, this::deactivate
            )
            configuration.inputAdapter.popListeners()
            configuration.numberConfirmSound.play()
            confirmNumber(boxedValue.currentValue)
        }

        private fun hideLeftArrow() {
            leftArrow.makeInvisible()
        }

        private fun showLeftArrow() {
            leftArrow.makeVisible()
        }

        private fun hideRightArrow() {
            rightArrow.makeInvisible()
        }

        private fun showRightArrow() {
            rightArrow.makeVisible()
        }
    }

    private class BoxedValue(
        initialValue: Int,
        private val minValue: Int,
        private val maxValue: Int,
        private val updateText: (Int) -> Unit,
        private val handleMinReached: () -> Unit,
        private val handleMinLeft: () -> Unit,
        private val handleMaxReached: () -> Unit,
        private val handleMaxLeft: () -> Unit
    ) {
        internal var currentValue = initialValue

        internal fun increment(): () -> Unit {
            if (currentValue >= maxValue) return DO_NOTHING

            currentValue++
            updateText(currentValue)
            return when (currentValue) {
                maxValue -> handleMaxReached
                minValue + 1 -> handleMinLeft
                else -> DO_NOTHING
            }
        }

        internal fun decrement(): () -> Unit {
            if (currentValue <= minValue) return DO_NOTHING

            currentValue--
            updateText(currentValue)
            return when (currentValue) {
                minValue -> handleMinReached
                maxValue - 1 -> handleMaxLeft
                else -> DO_NOTHING
            }
        }

        internal fun atMax(): Boolean = currentValue == maxValue
        internal fun atMin(): Boolean = currentValue == minValue

        companion object {
            private val DO_NOTHING = {}
        }
    }

    private class Arrow(
        private val shape: Shape,
        private val styleSet: StyleSet,
        private val selectionAction: GuiAction,
        private val configuration: Configuration,
        private inline val updateValue: () -> () -> Unit
    ) {

        private var handleUpdatedResult: () -> Unit = {}
        private val pressArrow = {
            handleUpdatedResult = updateValue()
            style = styleSet.active
            configuration.arrowPressSound.play()
            configuration.inputAdapter.addActionEndListener(selectionAction, depressArrow)
        }
        private val depressArrow = object : () -> Unit {
            override fun invoke() {
                style = styleSet.inactive
                configuration.arrowDepressSound.play()
                configuration.inputAdapter.removeActionEndListener(selectionAction, this)
                handleUpdatedResult()
            }
        }
        private val renderArrow: (Graphics) -> Unit = {
            it.fill(shape, style.arrowColour)
        }
        private var style: Style = styleSet.inactive

        internal var render: (Graphics) -> Unit = RENDER_NOTHING
            private set

        internal fun makeVisible() {
            render = renderArrow
            configuration.inputAdapter.addActionStartListener(selectionAction, pressArrow)
        }

        internal fun makeInvisible() {
            render = RENDER_NOTHING
            configuration.inputAdapter.removeActionStartListener(selectionAction, pressArrow)
        }

        companion object {
            private val RENDER_NOTHING: (Graphics) -> Unit = {}
        }
    }

    class Style(arrowColour: Int) {
        /** The colour of the arrows. */
        val arrowColour = Color(arrowColour)
    }

    internal class StyleSet(val active: Style, val inactive: Style)

    companion object {
        private const val TEXT_RELATIVE_WIDTH = 0.5f
        private const val ARROW_RELATIVE_WIDTH = 0.25f
        private val IDENTITY_ARROW = Polygon().apply {
            addPoint(-1f / 3, 0f)
            addPoint(1f / 6, -0.15f)
            addPoint(1f / 6, 0.15f)
        }
        private val ACTIVE = Style(arrowColour = 0xaaaaaa)
        private val INACTIVE = Style(arrowColour = 0xffffff)
    }
}