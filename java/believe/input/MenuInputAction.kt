package believe.input

/**
 * The exhaustive set of signals that can be received by an arbitrary input system within a menu.
 */
enum class MenuInputAction {
    /** Signals an intention to move up. */
    UP,
    /** Signals an intention to move down. */
    DOWN,
    /** Signals an intention to move left. */
    LEFT,
    /** Signals an intention to move right. */
    RIGHT,
    /** Signals an intention to select a menu item. */
    SELECT
}