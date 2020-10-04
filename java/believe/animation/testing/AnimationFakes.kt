package believe.animation.testing

import believe.animation.Animation
import believe.animation.animation
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.proto.AnimationProto.Animation.IterationMode.LINEAR
import believe.animation.proto.AnimationProto.Animation.IterationMode.PING_PONG
import believe.gui.testing.FakeImage
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet

/**
 * Generates an [Animation] using fake images.
 *
 * @param frameDurations the duration, in milliseconds, that each frame should be displayed.
 */
fun fakeAnimation(
    iterationMode: IterationMode, isLooping: Boolean, vararg frameDurations: Int
): Animation = animation(
    (frameDurations.indices).map { FakeImage() }, frameDurations.toList(), iterationMode, isLooping
)

/** Returns [FrameData] for the frame at [index]. */
fun Animation.frameAt(index: Int): FrameData =
    with(asSlickAnimation()) { FrameData(index, getImage(index), getDuration(index)) }

/** Creates a [FrameData] instance based on the currently-active frame in the [Animation]. */
val Animation.currentFrameData: FrameData
    get() = asSlickAnimation().let {
        FrameData(
            it.frame, it.currentFrame, it.getDuration(it.frame)
        )
    }

/** Returns a list of [FrameData] to help with tests. */
fun Animation.frames(iterations: Int = 1): List<FrameData> = if (iterations <= 0) {
    emptyList()
} else {
    val actualIterations = if (isLooping) iterations else 1
    when (iterationMode) {
        LINEAR -> asSlickAnimation().let {
            val baseFrames = (0 until it.frameCount).map { index ->
                FrameData(index, it.getImage(index), it.getDuration(index))
            }
            (0 until actualIterations).flatMap { baseFrames }
        }
        PING_PONG -> asSlickAnimation().let { slickAnimation ->
            when (slickAnimation.frameCount) {
                0 -> emptyList<FrameData>()
                1 -> (0 until actualIterations).map {
                    FrameData(0, slickAnimation.getImage(0), slickAnimation.getDuration(0))
                }
                else -> {
                    val lastFrameIndex = slickAnimation.frameCount - 1
                    val firstFrame = listOf(
                        FrameData(
                            0, slickAnimation.getImage(0), slickAnimation.getDuration(0)
                        )
                    )
                    val middleFrames = (1 until lastFrameIndex).map { index ->
                        FrameData(
                            index, slickAnimation.getImage(index), slickAnimation.getDuration(index)
                        )
                    }
                    val lastFrame = listOf(
                        FrameData(
                            lastFrameIndex,
                            slickAnimation.getImage(lastFrameIndex),
                            slickAnimation.getDuration(lastFrameIndex)
                        )
                    )
                    firstFrame + (0 until actualIterations).flatMap {
                        middleFrames + lastFrame + middleFrames.asReversed() + firstFrame
                    }
                }
            }
        }
    }
}

/** Returns a list of images corresponding to each of the frames in this list. */
fun List<FrameData>.images(): List<Image> = map { it.image }

/** Returns a list of integers indicating each of the durations of the frames in this list. */
fun List<FrameData>.durations(): List<Int> = map { it.duration }
