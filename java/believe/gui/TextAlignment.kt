package believe.gui

import believe.geometry.Rectangle

/** Contains enums for aligning text across multiple dimensions. */
object TextAlignment {
    /** Enums for aligning text on the x axis. */
    enum class Horizontal(internal val calculateXPosition: (Int, Rectangle) -> Int) {
        /** Aligns text with the left side of its containing component. */
        LEFT({ _, rect -> rect.x.toInt() }),
        /** Aligns text with the center of its containing component. */
        CENTERED({ textWidth, rect -> rect.centerX.toInt() - textWidth / 2 });
    }

    /** Enums for aligning text on the y axis. */
    enum class Vertical(internal val calculateYPosition: (Int, Rectangle) -> Int) {
        /** Aligns text with the top of its containing component. */
        TOP({ _, rect -> rect.y.toInt() }),
        /** Aligns text with the middle of its containing component. */
        MIDDLE({ totalTextFragmentHeight, rect -> rect.centerY.toInt() - (totalTextFragmentHeight / 2) });
    }
}