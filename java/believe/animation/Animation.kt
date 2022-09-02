package believe.animation

import believe.animation.AnimationUpdater.FrameInfo.MultiIndexedFrameInfo
import believe.animation.AnimationUpdater.FrameInfo.SimpleFrameInfo
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.proto.AnimationProto.Animation.IterationMode.LINEAR
import believe.animation.proto.AnimationProto.Animation.IterationMode.PING_PONG
import believe.core.Updatable
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet

private typealias FrameUpdateStrategy = (Long) -> Unit

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

    /**
     * The number of frames involved in the animation.
     *
     * **Note**: This does not necessarily correspond to the number of iterated frames in a single
     * animation iteration.
     */
    val numFrames: Int

    /** Returns an [org.newdawn.slick.Animation] equivalent to this instance. */
    fun asSlickAnimation(): org.newdawn.slick.Animation

    /**
     * Adds [configureListeners] so its methods can be executed once the frame corresponding to
     * [frameIndex] is reached and left.
     *
     * **Note**: Since the first frame has index 0, it isn't actually reached until either the
     * animation ends or it loops around.
     */
    fun addFrameListener(frameIndex: Int, configureListeners: FrameListener.Builder.() -> Unit)

    /** Resets the animation to its first frame. */
    fun restart()
}

/** An object that listens for events on a given [Animation] frame. */
class FrameListener private constructor(val enterFrame: () -> Unit, val leaveFrame: () -> Unit) {
    /**
     * A builder for [FrameListener].
     *
     * Clients should define [enterFrame] with their own listener to listen for when an animation
     * frame is made active. Similarly, they should define [leaveFrame] to listen for when an
     * animation frame is about to be replaced.
     */
    class Builder {
        var enterFrame: () -> Unit = {}
        var leaveFrame: () -> Unit = {}
        internal fun build() = FrameListener(enterFrame, leaveFrame)
    }
}

private sealed class AnimationUpdater : Updatable {
    abstract fun addListener(frameIndex: Int, frameListener: FrameListener)
    abstract fun restart()

    private class SimpleUpdater(
        private val animation: org.newdawn.slick.Animation
    ) : AnimationUpdater() {
        override fun addListener(frameIndex: Int, frameListener: FrameListener) {}
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
        private val frameSequence: Sequence<FrameInfo>
    ) : AnimationUpdater() {
        private val updateFrameBeforeFirstIteration: FrameUpdateStrategy = { delta: Long ->
            val frameIterator = frameSequence.iterator()
            if (frameIterator.hasNext()) {
                val currentFrame: FrameInfo = frameIterator.next()
                listeners[currentFrame.index].forEach { it.enterFrame() }

                updateFrame =
                    PostInitializationFrameUpdateStrategy(frameIterator, listeners, currentFrame)
                updateFrame(delta)
            } else {
                updateFrame = DO_NOT_UPDATE_FRAME
            }
        }

        private var listeners = Array(size = animation.frameCount) {
            mutableListOf<FrameListener>()
        }
        private var updateFrame: FrameUpdateStrategy = updateFrameBeforeFirstIteration

        override fun addListener(frameIndex: Int, frameListener: FrameListener) {
            listeners[frameIndex].add(frameListener)
        }

        override fun update(delta: Long) {
            animation.update(delta)
            updateFrame(delta)
        }

        override fun restart() {
            updateFrame = updateFrameBeforeFirstIteration
            animation.restart()
            // Artificially update the animation by a single millisecond due to a bug in Slick code
            // where the first frame of an animation is attributed an extra millisecond due to an error
            // in a comparison between numbers (nextChange < 0 instead of nextChange <= 0).
            animation.update(1)
        }

        companion object {
            private val DO_NOT_UPDATE_FRAME: FrameUpdateStrategy = {}
        }
    }

    private class PostInitializationFrameUpdateStrategy(
        private val frameIterator: Iterator<FrameInfo>,
        private val listeners: Array<out List<FrameListener>>,
        private var currentFrame: FrameInfo
    ) : FrameUpdateStrategy {

        private var timeUntilNextFrame = currentFrame.duration

        override fun invoke(delta: Long) {
            // Check whether we're at the end of a non-looping animation. Since a non-looping
            // animation never leaves the last frame, we shouldn't call leaveFrame for the last
            // frame's listeners.
            if (!frameIterator.hasNext()) return

            // In contrast, a looping animation always has a next frame and should therefore always
            // call the appropriate listeners.
            var remainingTime = delta - timeUntilNextFrame
            while (remainingTime >= 0) {
                listeners[currentFrame.index].forEach { it.leaveFrame() }
                currentFrame = frameIterator.next()
                timeUntilNextFrame = currentFrame.duration
                listeners[currentFrame.index].forEach { it.enterFrame() }
                remainingTime -= timeUntilNextFrame
            }
            timeUntilNextFrame = -remainingTime
        }
    }

    private sealed class FrameInfo(val index: Int, val duration: Long) {
        class SimpleFrameInfo(index: Int, duration: Long) : FrameInfo(index, duration)
        class MultiIndexedFrameInfo(
            val sequenceIndex: Int,
            index: Int,
            duration: Long
        ) : FrameInfo(index, duration)
    }

    companion object {
        fun createAnimationUpdater(
            animation: org.newdawn.slick.Animation,
            iterationMode: IterationMode,
            isLooping: Boolean
        ): AnimationUpdater = if (animation.frameCount == 0) {
            SimpleUpdater(animation)
        } else {
            when (iterationMode) {
                LINEAR -> {
                    val frameInfo = animation.durations.mapIndexed { index, duration ->
                        SimpleFrameInfo(index, duration.toLong())
                    }
                    generateSequence(frameInfo[0]) { previous ->
                        frameInfo[(previous.index + 1) % frameInfo.size]
                    }
                }
                PING_PONG -> {
                    if (animation.durations.size == 1) {
                        generateSequence { SimpleFrameInfo(0, animation.getDuration(0).toLong()) }
                    } else {
                        val rawFrameInfo = animation.durations.mapIndexed { index, duration ->
                            SimpleFrameInfo(index, duration.toLong())
                        }
                        val combinedFrameInfo =
                            rawFrameInfo + rawFrameInfo.slice(1..rawFrameInfo.size - 2).asReversed()

                        val indexedFrameInfo = combinedFrameInfo.mapIndexed { index, frameInfo ->
                            MultiIndexedFrameInfo(
                                index, frameInfo.index, frameInfo.duration
                            )
                        }
                        generateSequence(indexedFrameInfo[0]) { previous ->
                            indexedFrameInfo[(previous.sequenceIndex + 1) % indexedFrameInfo.size]
                        }
                    }
                }
            }.let {
                NotifyingUpdater(
                    animation,
                    if (isLooping) it else it.take(
                        if (iterationMode == LINEAR) {
                            animation.frameCount
                        } else {
                            animation.frameCount * 2 - 1
                        }
                    )
                )
            }
        }
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
    override val numFrames: Int = internalAnimation.frameCount

    override fun update(delta: Long) = animationUpdater.update(delta)

    override fun asSlickAnimation(): org.newdawn.slick.Animation = internalAnimation
    override fun addFrameListener(
        frameIndex: Int,
        configureListeners: FrameListener.Builder.() -> Unit
    ) {
        animationUpdater.addListener(
            frameIndex,
            FrameListener.Builder().apply(configureListeners).build()
        )
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
    override val numFrames: Int = 0

    override fun asSlickAnimation(): org.newdawn.slick.Animation = internalAnimation
    override fun addFrameListener(
        frameIndex: Int,
        configureListeners: FrameListener.Builder.() -> Unit
    ) {
    }

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
): Animation = animation(
    frameRange.map {
        spriteSheet.getSprite(
            it % spriteSheet.horizontalCount, it / spriteSheet.horizontalCount
        )
    },
    List(size = frameRange.count(), init = { frameDuration }), iterationMode, isLooping
)

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
    },
    iterationMode, isLooping
)

fun emptyAnimation(): Animation = EmptyAnimation
