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

    /**
     * Creates a [GuiContainer] with a vertical layout.
     *
     * @param configure the receiver for configuring the details of the container.
     */
    fun verticalLayoutContainer(
        configure: GuiContainer.Builder.() -> Unit
    ): GuiContainer.Builder = GuiContainer.Builder().apply(configure)
}