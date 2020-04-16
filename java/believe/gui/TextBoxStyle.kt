package believe.gui

import org.newdawn.slick.Color

/**
 * An object that defines the styling on a [TextBox].
 *
 * @param textColour the colour of the text rendered in the [TextBox].
 */
class TextBoxStyle constructor(textColour: Int) {
    /** The [Color] of the text rendered in the [TextBox]. */
    val textColor = Color(textColour)
}
