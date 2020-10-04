package believe.animation

/** An animation that can be displayed in two horizontal directions. */
class BidirectionalAnimation private constructor(
    /** The [Animation] facing towards the left. */
    val leftAnimation: Animation,
    /** The [Animation] facing towards the right. */
    val rightAnimation: Animation
) {
    companion object {
        /**
         * Creates a [BidirectionalAnimation] based on [animation]. This assumes that [animation]
         * faces towards the right.
         */
        fun from(animation: Animation) = with(animation.asSlickAnimation()) {
            BidirectionalAnimation(
                leftAnimation = animation(
                    frames = (0 until frameCount).map { getImage(it).getFlippedCopy(true, false) },
                    frameDurations = (0 until frameCount).map { getDuration(it) },
                    iterationMode = animation.iterationMode,
                    isLooping = animation.isLooping
                ), rightAnimation = animation
            )
        }
    }
}
