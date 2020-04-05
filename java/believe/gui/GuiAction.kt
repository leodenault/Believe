package believe.gui

/** The exhaustive list of actions that can be executed within a GUI based on user input. */
enum class GuiAction {
    /** Indicates an error in mapping to this type of action. */
    UNKNOWN,
    /** The user selected a GUI component further up. */
    SELECT_UP,
    /** The user selected a GUI component further down. */
    SELECT_DOWN,
    /** The user selected a GUI component to the left. */
    SELECT_LEFT,
    /** The user selected a GUI component to the right. */
    SELECT_RIGHT,
    /**
     * The user provided input indicating that they want to execute the action associated with the
     * GUI component.
     */
    EXECUTE_ACTION
}