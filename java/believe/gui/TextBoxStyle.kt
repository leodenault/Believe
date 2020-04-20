package believe.gui

import org.newdawn.slick.Color

/**
 * An object that defines the styling on a [TextBox].
 *
 * @param textColour the colour of the text rendered in the [TextBox].
 * @param highlightedTextColour the colour of the text when its surroundings are highlighted.
 */
class TextBoxStyle constructor(textColour: Int, highlightedTextColour: Int) {
    /** The [Color] of the text rendered in the [TextBox]. */
    val textColour = Color(textColour)
    /** The [Color] of the text when its surroundings are highlighted. */
    val highlightedTextColor = Color(highlightedTextColour)
}
