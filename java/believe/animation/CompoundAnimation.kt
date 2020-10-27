package believe.animation

import believe.animation.proto.AnimationProto.Animation.IterationMode
import org.newdawn.slick.Image
import org.newdawn.slick.util.Log
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException

interface CompoundAnimation : Animation {
    /**
     * Adds [frameReached] so it can be executed once the frame corresponding to [frameIndex] is
     * reached, where [frameIndex] is the index of a frame belonging to [subAnimation], one of the
     * animations composing this [CompoundAnimation].
     *
     * **Note**: Since the first frame has index 0, it isn't actually reached until either the
     * animation ends or it loops around.
     */
    fun addFrameListener(subAnimation: Animation, frameIndex: Int, frameReached: () -> Unit)
}

private class CompoundAnimationImpl(
    internalAnimation: Animation,
    private val subAnimationsToIndexFetchers: Map<Animation, (Int) -> List<Int>>
) : CompoundAnimation, Animation by internalAnimation {

    override fun addFrameListener(
        subAnimation: Animation, frameIndex: Int, frameReached: () -> Unit
    ) {
        if (!(0 until subAnimation.numFrames).contains(frameIndex)) throw IndexOutOfBoundsException()

        val fetchIndices = subAnimationsToIndexFetchers[subAnimation] ?: return Unit.also {
            Log.warn("Could not find index fetcher for sub-animation.")
        }

        fetchIndices(frameIndex).forEach {
            addFrameListener(it, frameReached)
        }
    }
}

private class DurationAndImage(val duration: Int, val image: Image)

/** Creates a [CompoundAnimation]. */
fun compoundAnimation(
    isLooping: Boolean, vararg iterationsAndAnimations: Pair<Int, Animation>
): CompoundAnimation {
    val durationsAndImages = generateDurationsAndImages(iterationsAndAnimations)
    val internalAnimation = animation(
        frames = durationsAndImages.map { it.image },
        frameDurations = durationsAndImages.map { it.duration },
        iterationMode = IterationMode.LINEAR,
        isLooping = isLooping
    )
    val subAnimationsToIndexRanges = generateIndexRanges(iterationsAndAnimations)
    return CompoundAnimationImpl(internalAnimation, subAnimationsToIndexRanges)
}

private fun generateDurationsAndImages(
    animationsAndIterations: Array<out Pair<Int, Animation>>
): List<DurationAndImage> = animationsAndIterations.flatMap {
    val (iterations, animation) = it
    if (iterations < 1) throw IllegalArgumentException(
        "Each animation in a CompundAnimation must be iterated at least once."
    )

    val frames: List<DurationAndImage> = animation.frames()
    if (frames.isEmpty()) {
        emptyList()
    } else when (animation.iterationMode) {
        IterationMode.LINEAR -> (0 until iterations).flatMap { frames }
        IterationMode.PING_PONG -> if (frames.size == 1) {
            listOf(frames.first())
        } else {
            val firstFrame = listOf(frames.first())
            val lastFrame = listOf(frames.last())
            val middleFrames = frames.slice(1 until frames.lastIndex)
            (0 until iterations).flatMap {
                firstFrame + middleFrames + lastFrame + middleFrames.asReversed()
            } + firstFrame
        }
    }
}

private fun Animation.frames(): List<DurationAndImage> = asSlickAnimation().let {
    (0 until it.frameCount).map { index ->
        DurationAndImage(
            it.durations[index], it.getImage(index)
        )
    }
}

private fun generateIndexRanges(
    animationsAndIterations: Array<out Pair<Int, Animation>>
): Map<Animation, (Int) -> List<Int>> {
    val subAnimationsToIndexFetchers = mutableMapOf<Animation, (Int) -> List<Int>>()
    var currentAnimationStartIndex = 0
    animationsAndIterations.forEach {
        val (iterations, animation) = it
        val finalCurrentAnimationStartIndex = currentAnimationStartIndex
        val numFramesInIteration = when (animation.iterationMode) {
            IterationMode.LINEAR -> animation.numFrames
            IterationMode.PING_PONG -> 2 * (animation.numFrames - 1)
        }
        val fetchIndices: (Int) -> List<Int> = when (animation.iterationMode) {
            IterationMode.LINEAR -> { index ->
                (0 until iterations).map { iteration ->
                    iteration * numFramesInIteration + index + finalCurrentAnimationStartIndex
                }
            }
            IterationMode.PING_PONG -> { index ->
                when (index) {
                    0 -> (0..iterations).map { iteration ->
                        iteration * numFramesInIteration + finalCurrentAnimationStartIndex
                    }
                    animation.numFrames - 1 -> (0 until iterations).map { iteration ->
                        iteration * numFramesInIteration + index + finalCurrentAnimationStartIndex
                    }
                    else -> {
                        val mirrorIndex = numFramesInIteration - index
                        (0 until iterations).flatMap { iteration ->
                            val firstIndexInIteration = iteration * numFramesInIteration
                            listOf(
                                firstIndexInIteration + index + finalCurrentAnimationStartIndex,
                                firstIndexInIteration + mirrorIndex + finalCurrentAnimationStartIndex
                            )
                        }
                    }
                }
            }
        }
        subAnimationsToIndexFetchers[animation] = fetchIndices
        currentAnimationStartIndex += numFramesInIteration * iterations
        if (animation.iterationMode == IterationMode.PING_PONG) {
            currentAnimationStartIndex += 1
        }
    }
    return subAnimationsToIndexFetchers
}

