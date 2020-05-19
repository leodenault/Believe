package believe.gui

/** Builder methods for constructing UIs. */
object GuiBuilders {
    /**
     * Creates a [MenuSelectionV2].
     *
     * @param configure the receiver for configuring the details of the menu selection.
     */
    fun menuSelection(
        configure: MenuSelectionV2.Builder.() -> Unit
    ): MenuSelectionV2.Builder = MenuSelectionV2.Builder().apply(configure)

    fun textBox(
        configure: TextBox.Builder.() -> Unit
    ): TextBox.Builder = TextBox.Builder().apply(configure)

    /**
     * Creates a [VerticalContainer].
     *
     * @param configure the receiver for configuring the details of the container.
     */
    fun verticalContainer(
        configure: VerticalContainer.Builder.() -> Unit
    ): VerticalContainer.Builder = VerticalContainer.Builder().apply(configure)

    /**
     * Creates a [CanvasContainer].
     *
     * @param configure the receiver for configuring the details of the container.
     */
    fun <G: GuiElement> canvasContainer(
        configure: CanvasContainerV2.Builder<G>.() -> Unit
    ): CanvasContainerV2.Builder<G> = CanvasContainerV2.Builder<G>().apply(configure)

    /**
     * Creates a [NumberPickerV2].
     *
     * @param configure the receiver for configuring the details of the number picker.
     */
    fun numberPicker(
        configure: NumberPickerV2.Builder.() -> Unit
    ): NumberPickerV2.Builder = NumberPickerV2.Builder().apply(configure)
}