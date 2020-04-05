package believe.gui

import org.newdawn.slick.Color

/**
 * An object that defines the styling on a [MenuSelectionV2] in its various states.
 *
 * @param colour the colour filling the area occupied by the [MenuSelectionV2].
 * @param borderColour the colour of the border of the [MenuSelectionV2].
 * @param borderSize the size, in pixels, of the [MenuSelectionV2].
 */
class MenuSelectionStyle constructor(colour: Int, borderColour: Int, val borderSize: Int) {
    /** The [Color] filling the area occupied by the [MenuSelectionV2]. */
    val color = Color(colour)
    /** The [Color] of the border of the [MenuSelectionV2]. */
    val borderColor = Color(borderColour)
}
