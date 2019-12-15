package believe.datamodel

/**
 * A [MutableData] encapsulating a single value.
 *
 * @param [T] the type of the mutable value.
 */
class MutableValue<T> private constructor(private var value: T) : MutableData<T> {
    override fun get(): T {
        return value
    }

    override fun update(data: T) {
        value = data
    }

    companion object {
        /**
         * Creates an instance of [MutableValue] using [initialValue] as its initial value.
         */
        @JvmStatic
        fun <T> of(initialValue: T): MutableValue<T> {
            return MutableValue(initialValue)
        }
    }
}
