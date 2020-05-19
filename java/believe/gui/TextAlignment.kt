package believe.gui

import believe.geometry.Rectangle

/** Contains enums for aligning text across multiple dimensions. */
object TextAlignment {
    /** Enums for aligning text on the x axis. */
    enum class Horizontal(internal val calculateXPosition: (Int, Rectangle) -> Float) {
        /** Aligns text with the left side of its containing component. */
        LEFT({ _, rect -> rect.x }),
        /** Aligns text with the center of its containing component. */
        CENTERED({ textWidth, rect -> rect.x + (rect.width - textWidth) / 2 });
    }

    /** Enums for aligning text on the y axis. */
    enum class Vertical(internal val calculateYPosition: (Int, Rectangle) -> Float) {
        /** Aligns text with the top of its containing component. */
        TOP({ _, rect -> rect.y }),
        /** Aligns text with the middle of its containing component. */
        MIDDLE({ totalTextFragmentHeight, rect ->
            rect.y + (rect.height - totalTextFragmentHeight) / 2
        });
    }
}