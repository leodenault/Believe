package believe.animation.testing

import believe.animation.Animation
import believe.animation.emptyAnimation
import believe.core.Updatable
import believe.core.display.Graphics
import believe.core.display.RenderableV2
import com.google.common.truth.Fact
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.google.common.truth.Subject.Factory
import com.google.common.truth.Truth
import org.newdawn.slick.Image

/**
 * A [Subject] that allows making assertions about [Updatable] instances that delegate to
 * [Animation] instances.
 *
 * Begin assertions by doing the following:
 *
 * ```
 * assertThat(myUpdatable).updating { myUpdatable.animation }.generatesFrames(...)
 * ```
 */
open class AnimationWrapperSubject constructor(
    private val failureMetadata: FailureMetadata, private val actual: Updatable
) : Subject(failureMetadata, actual) {

    /**
     * Begins asserting about [provideAnimation].
     *
     * This method takes a provider of [Animation] rather than the animation directly in the case
     * that one wishes to test for the frames in multiple animations that are exchanged as time
     * passes.
     */
    fun updating(provideAnimation: () -> Animation) =
        AnimationProviderSubject(failureMetadata, provideAnimation, actual)

    /** Begins asserting about [animation]. */
    fun updating(animation: Animation): AnimationProviderSubject = updating { animation }

    companion object {
        internal fun animationWrappers(
        ): Factory<AnimationWrapperSubject, Updatable> = Factory { failureMetadata, actual ->
            AnimationWrapperSubject(failureMetadata, actual)
        }
    }
}

/** A [Subject] for running assertions on [Updatable] instances. */
class AnimationProviderSubject internal constructor(
    failureMetadata: FailureMetadata,
    private val actual: () -> Animation,
    private val wrapper: Updatable
) : Subject(failureMetadata, actual) {

    /**
     * Asserts that the animation generates frames matching those contained in [frameData] when
     * calling the animation's [Animation.update] method. The durations specified by each
     * instance in [FrameData] are used in updating the animation.
     */
    fun generatesFrames(frameData: Iterable<FrameData>) {
        val actualFrames = generateFrames(frameData.durations())

        if (actualFrames != frameData) {
            failWithActual(
                Fact.fact("generated frames",
                    actualFrames.joinToString(separator = "\n") { it.toString() }),
                Fact.fact("expected frames",
                    frameData.joinToString(separator = "\n") { it.toString() })
            )
        }
    }

    /**
     * Asserts that the animation generates durations matching those contained in [durations]
     * when calling the animation's [Animation.update] method. The durations specified by each
     * instance in [FrameData] are used in updating the animation.
     */
    fun generatesDurations(durations: Iterable<Int>) {
        val actualDurations = generateFrames(durations).durations()

        if (actualDurations != durations) {
            failWithActual(
                Fact.fact("generated durations",
                    actualDurations.joinToString(separator = "\n") { it.toString() }),
                Fact.fact("expected durations",
                    durations.joinToString(separator = "\n") { it.toString() })
            )
        }
    }

    private fun generateFrames(durations: Iterable<Int>): List<FrameData> = durations.map {
        val animation = actual()
        val frame = animation.currentFrameData
        wrapper.update(it.toLong())
        frame
    }
}

/** An [AnimationWrapperSubject] for [RenderableV2] instances extending from [Updatable]. */
class RenderingAnimationWrapperSubject<R>(
    failureMetadata: FailureMetadata, private val actual: R
) : AnimationWrapperSubject(failureMetadata, actual) where R : Updatable, R : RenderableV2 {
    /**
     * Asserts that the animation renders images matching those made available in [durations]
     * when calling the animation's [Animation.update] and its containing object's [Graphics.render]
     * methods. The durations specified by each instance in [FrameData] are used in updating the
     * animation.
     */
    fun rendersFrames(frames: Iterable<FrameData>) {
        var animation = emptyAnimation()
        val capturingGraphics = CapturingGraphics { animation = it }

        val renderedImages: List<Image> = frames.map {
            actual.render(capturingGraphics)
            val frame = animation.currentFrameData
            actual.update(it.duration.toLong())
            frame.image
        }

        val expectedImages = frames.images()
        if (renderedImages != expectedImages) {
            failWithActual(
                Fact.fact("rendered images",
                    renderedImages.joinToString(separator = "\n") { it.toString() }),
                Fact.fact("expected images",
                    expectedImages.joinToString(separator = "\n") { it.toString() })
            )
        }
    }

    private class CapturingGraphics(
        private val captureAnimation: (Animation) -> Unit
    ) : Graphics(org.newdawn.slick.Graphics()) {

        override fun drawAnimation(animation: Animation, x: Float, y: Float) {
            captureAnimation(animation)
        }
    }

    companion object {
        internal fun <R> renderingAnimationWrappers(
        ): Factory<RenderingAnimationWrapperSubject<R>, R> where R : Updatable, R : RenderableV2 =
            Factory { failureMetadata, actual ->
                RenderingAnimationWrapperSubject(failureMetadata, actual)
            }
    }
}

/**
 * Begins asserting about [updatable].
 *
 * This method takes a provider of [Animation] rather than the animation directly in the case that
 * one wishes to test for the frames in multiple animations that are exchanged as time passes.
 */
fun assertThat(updatable: Updatable): AnimationWrapperSubject =
    Truth.assertAbout(AnimationWrapperSubject.animationWrappers()).that(updatable)

/**
 * Begins asserting about [renderable].
 *
 * This method takes a provider of [Animation] rather than the animation directly in the case that
 * one wishes to test for the frames in multiple animations that are exchanged as time passes.
 *
 * @param R the type extending from [Updatable] and [RenderableV2] against which assertions will be
 *   made.
 */
fun <R> assertThat(
    renderable: R
): RenderingAnimationWrapperSubject<R> where R : Updatable, R : RenderableV2 =
    Truth.assertAbout(RenderingAnimationWrapperSubject.renderingAnimationWrappers<R>())
        .that(renderable)
