package believe.datamodel

import java.util.function.Supplier

/**
 * Provides an instance of a data object of type [T]. Allows updating the instance in-memory.
 *
 * @param T the type of data this class handles.
 */
interface MutableData<T> : Supplier<T> {
    /** Updates the instance of type [T] held by this [MutableData] with [data].  */
    fun update(data: T)
}
