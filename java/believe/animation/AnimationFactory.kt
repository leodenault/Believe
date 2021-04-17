package believe.animation

/**
 * Creates an instance of [Animation] when invoked based on internal data. Each invocation results
 * in a new [Animation] instance based on identical data.
 */
typealias AnimationFactory = () -> Animation

/** A singleton [AnimationFactory] for creating empty animation instances. */
val EMPTY_ANIMATION_FACTORY: AnimationFactory = { emptyAnimation() }
