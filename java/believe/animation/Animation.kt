package believe.animation

import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.proto.AnimationProto.Animation.IterationMode.LINEAR
import believe.animation.proto.AnimationProto.Animation.IterationMode.PING_PONG
import believe.core.Updatable
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet

/**
 * An object containing multiple frames that draw to the screen in sequence to create a moving
 * image.
 */
interface Animation : Updatable {
    /** The [IterationMode] determining the iteration behaviour of this [Animation]. */
    val iterationMode: IterationMode

    /** Whether the animation loops endlessly through its frames. */
    val isLooping: Boolean

    /** The width of the animation, in pixels. */
    val width: Float

    /** The height of the animation, in pixels. */
    val height: Float

    /** Returns an [org.newdawn.slick.Animation] equivalent to this instance. */
    fun asSlickAnimation(): org.newdawn.slick.Animation

    /** Adds [loopEnded] so it can be executed at the end of every animation loop. */
    fun addAnimationEndedListener(loopEnded: () -> Unit)

    /** Resets the animation to its first frame. */
    fun restart()
}

private sealed class AnimationUpdater : Updatable {
    abstract fun addListener(listener: () -> Unit)
    abstract fun restart()

    private class SimpleUpdater(
        private val animation: org.newdawn.slick.Animation
    ) : AnimationUpdater() {
        override fun addListener(listener: () -> Unit) {}
        override fun update(delta: Long) = animation.update(delta)
        override fun restart() {
            animation.restart()
            // Artificially update the animation by a single millisecond due to a bug in Slick code
            // where the first frame of an animation is attributed an extra millisecond due to an error
            // in a comparison between numbers (nextChange < 0 instead of nextChange <= 0).
            animation.update(1)
        }
    }

    private class NotifyingUpdater(
        private val animation: org.newdawn.slick.Animation,
        private val firstIterationAnimationTime: Long,
        private val subsequentIterationAnimationTime: Long
    ) : AnimationUpdater() {

        private var listeners = mutableListOf<() -> Unit>(this::changeAnimationTimes)
        private var elapsedTime = 0L
        private var totalAnimationTime = firstIterationAnimationTime

        override fun addListener(listener: () -> Unit) {
            listeners.add(listener)
        }

        override fun update(delta: Long) {
            elapsedTime += delta
            animation.update(delta)
            while (elapsedTime >= totalAnimationTime) {
                elapsedTime -= totalAnimationTime
                listeners.forEach { it() }
            }
        }

        override fun restart() {
            totalAnimationTime = firstIterationAnimationTime
            elapsedTime = 0
            if (listeners.isEmpty() || listeners[0] != this::changeAnimationTimes) {
                listeners =
                    (listOf<() -> Unit>(this::changeAnimationTimes) + listeners).toMutableList()
            }
            animation.restart()
            // Artificially update the animation by a single millisecond due to a bug in Slick code
            // where the first frame of an animation is attributed an extra millisecond due to an error
            // in a comparison between numbers (nextChange < 0 instead of nextChange <= 0).
            animation.update(1)
        }

        private fun changeAnimationTimes() {
            totalAnimationTime = subsequentIterationAnimationTime
            listeners = if (listeners.isEmpty()) {
                mutableListOf()
            } else {
                listeners.slice(1 until listeners.size).toMutableList()
            }
        }
    }

    companion object {
        fun createAnimationUpdater(
            animation: org.newdawn.slick.Animation, iterationMode: IterationMode, isLooping: Boolean
        ): AnimationUpdater = when (iterationMode) {
            LINEAR -> animation.durations.sum().let { Pair(it, it) }
            PING_PONG -> when (animation.durations.size) {
                0 -> Pair(0, 0)
                1 -> Pair(animation.durations.first(), animation.durations.first())
                else -> Pair(
                    animation.durations.sum() + animation.durations.slice(
                        0..animation.durations.size - 2
                    ).sum(), animation.durations.slice(
                        1..animation.durations.size - 2
                    ).sum() * 2 + animation.durations.last() + animation.durations.first()
                )
            }
        }.takeIf { it.first > 0 && it.second > 0 }?.let {
            NotifyingUpdater(
                animation,
                it.first.toLong(),
                if (isLooping) it.second.toLong() else it.first.toLong()
            )
        } ?: SimpleUpdater(animation)
    }
}

private class AnimationImpl(
    private val internalAnimation: org.newdawn.slick.Animation,
    override val iterationMode: IterationMode,
    override val isLooping: Boolean
) : Animation {

    private val animationUpdater: AnimationUpdater =
        AnimationUpdater.createAnimationUpdater(internalAnimation, iterationMode, isLooping)

    override val width: Float =
        if (internalAnimation.frameCount == 0) 0f else internalAnimation.width.toFloat()
    override val height: Float =
        if (internalAnimation.frameCount == 0) 0f else internalAnimation.height.toFloat()

    override fun update(delta: Long) = animationUpdater.update(delta)

    override fun asSlickAnimation(): org.newdawn.slick.Animation = internalAnimation
    override fun addAnimationEndedListener(loopEnded: () -> Unit) {
        animationUpdater.addListener(loopEnded)
    }

    override fun restart() {
        animationUpdater.restart()
        if (iterationMode == PING_PONG) {
            // The Slick Animation code has a bug where calling restart() doesn't reset the
            // direction. This affects ping pong animations in that the code behaves as if the
            // animation will end after displaying the first frame. We therefore need to update the
            // animation so that it finishes displaying the first frame so that Slick thinks that
            // the ping pong animation has ended, then call restart() again to properly set up the
            // animation.
            internalAnimation.update(
                internalAnimation.getDuration(internalAnimation.frame).toLong()
            )
            animationUpdater.restart()
        }
    }
}

private object EmptyAnimation : Animation {
    private val internalAnimation = org.newdawn.slick.Animation()
    override val iterationMode: IterationMode = LINEAR
    override val isLooping: Boolean = false
    override val width: Float = 0f
    override val height: Float = 0f

    override fun asSlickAnimation(): org.newdawn.slick.Animation = internalAnimation
    override fun addAnimationEndedListener(loopEnded: () -> Unit) {}
    override fun update(delta: Long) {}
    override fun restart() {}
}

/**
 * Returns an [Animation].
 *
 * @param spriteSheet the [SpriteSheet] storing the animation data.
 * @param frameRange the range of indices of frames from the [spriteSheet] included in the
 *   animation.
 * @param frameDuration the amount of time, in milliseconds, that each frame within the animation
 *   should be displayed.
 * @param iterationMode the [IterationMode] determining how the animation should iterate through
 *   frames. Defaults to [IterationMode.LINEAR].
 */
fun animation(
    spriteSheet: SpriteSheet,
    frameRange: IntRange,
    frameDuration: Int,
    iterationMode: IterationMode = LINEAR,
    isLooping: Boolean = true
): Animation = animation(frameRange.map {
    spriteSheet.getSprite(
        it % spriteSheet.horizontalCount, it / spriteSheet.horizontalCount
    )
}, List(size = frameRange.count(), init = { frameDuration }), iterationMode, isLooping)

/**
 * Returns an [Animation].
 *
 * @param frames the [Image] instances to be used as individual frames in the animation.
 * @param frameDurations the amount of time, in milliseconds, that each frame within the animation
 *   should be displayed.
 * @param iterationMode the [IterationMode] determining how the animation should iterate through
 *   frames. Defaults to [IterationMode.LINEAR].
 */
fun animation(
    frames: Collection<Image>,
    frameDurations: Collection<Int>,
    iterationMode: IterationMode = LINEAR,
    isLooping: Boolean = true
): Animation = AnimationImpl(
    // Use the constructor where we generate our own images. Slick's constructors that attempt to
    // parse a SpriteSheet based on image indices doesn't work.
    org.newdawn.slick.Animation(
        frames.toTypedArray(), frameDurations.toIntArray(),
        /* autoUpdate= */
        false
    ).also {
        it.setLooping(isLooping)
        it.setPingPong(iterationMode == PING_PONG)
        // Artificially update the animation by a single millisecond due to a bug in Slick code
        // where the first frame of an animation is attributed an extra millisecond due to an error
        // in a comparison between numbers (nextChange < 0 instead of nextChange <= 0).
        it.update(1)
    }, iterationMode, isLooping
)

fun emptyAnimation(): Animation = EmptyAnimation
