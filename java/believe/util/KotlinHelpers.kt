package believe.util

import java.util.*

/** Helper functions for writing Kotlin code throughout the project. */
object KotlinHelpers {
    /** Invokes [whenNull] when the receiver is null, and returns the receiver. */
    inline fun <T> T?.whenNull(whenNull: () -> Unit): T? {
        if (this == null) {
            whenNull()
        }
        return this
    }

    /**
     * Invokes [whenEmpty] when the receiver is an empty Optional instance and returns null,
     * otherwise returns the result of the receiver's [Optional.get] invocation.
     */
    inline fun <T> Optional<T>.whenEmpty(whenEmpty: () -> Unit): T? =
        takeIf { isPresent }.whenNull(whenEmpty)?.get()
}
