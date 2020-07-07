package believe.character

import org.newdawn.slick.Animation
import org.newdawn.slick.Image

internal class BidirectionalAnimation internal constructor(
    internal val leftAnimation: Animation, internal val rightAnimation: Animation
) {
    companion object {
        internal fun from(animation: Animation) = BidirectionalAnimation(
            animation.asList().let {
                Animation(
                    it.map { frame -> frame.first.getFlippedCopy(true, false) }.toTypedArray(),
                    it.map { frame -> frame.second }.toIntArray(),
                    false
                )
            }, animation
        ).also { animation.setCurrentFrame(0) }

        private fun Animation.asList(): List<Pair<Image, Int>> = List(frameCount) { i ->
            setCurrentFrame(i).let { Pair(currentFrame, getDuration(i)) }
        }
    }
}