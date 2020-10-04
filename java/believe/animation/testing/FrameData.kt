package believe.animation.testing

import believe.animation.Animation
import org.newdawn.slick.Image

/** Data about a frame in an [Animation]. */
data class FrameData internal constructor(
    /** The index of the frame within the sequence of an animation. */
    val index: Int,
    /** The [Image] rendered when the frame is active. */
    val image: Image,
    /** The amount of time the frame should be displayed. */
    val duration: Int
)
