package believe.animation.testing

import believe.animation.Animation
import believe.core.Updatable
import com.google.common.truth.Fact
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.google.common.truth.Subject.Factory
import com.google.common.truth.Truth

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
class UpdatableAnimationWrapperSubject private constructor(
    private val failureMetadata: FailureMetadata, private val actual: Updatable
) : Subject(failureMetadata, actual) {

    /**
     * Begins asserting about [provideAnimation].
     *
     * This method takes a provider of [Animation] rather than the animation directly in the case
     * that one wishes to test for the frames in multiple animations that are exchanged as time
     * passes.
     */
    fun updating(
        provideAnimation: () -> Animation
    ) = AnimationProviderSubject(failureMetadata, provideAnimation, actual)

    /** Begins asserting about [animation]. */
    fun updating(animation: Animation): AnimationProviderSubject = updating { animation }

    companion object {
        internal fun updatableAnimationWrappers(
        ): Factory<UpdatableAnimationWrapperSubject, Updatable> =
            Factory { failureMetadata, actual ->
                UpdatableAnimationWrapperSubject(
                    failureMetadata, actual
                )
            }
    }

    /** A [Subject] for running assertions on [Animation] providers. */
    class AnimationProviderSubject internal constructor(
        failureMetadata: FailureMetadata,
        private val actual: () -> Animation,
        private val updatable: Updatable
    ) : Subject(failureMetadata, actual) {

        /**
         * Asserts that the animation generates frames matching those contained in [frameData] when
         * calling the animations [Animation.update] method. The durations specified by each
         * instance in [FrameData] are used in updating the animation.
         */
        fun generatesFrames(frameData: Iterable<FrameData>) {
            val actualFrames = frameData.map {
                val animation = actual()
                val frame = animation.currentFrameData
                updatable.update(it.duration.toLong())
                frame
            }

            if (actualFrames != frameData) {
                failWithActual(
                    Fact.fact("generated frames",
                        actualFrames.joinToString(separator = "\n") { it.toString() }),
                    Fact.fact("expected frames",
                        frameData.joinToString(separator = "\n") { it.toString() })
                )
            }
        }
    }
}

/**
 * Begins asserting about [updatable].
 *
 * This method takes a provider of [Animation] rather than the animation directly in the case that
 * one wishes to test for the frames in multiple animations that are exchanged as time passes.
 */
fun assertThat(updatable: Updatable): UpdatableAnimationWrapperSubject = Truth.assertAbout(
    UpdatableAnimationWrapperSubject.updatableAnimationWrappers()
).that(updatable)
