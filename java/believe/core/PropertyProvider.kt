package believe.core

/** An object that provides property values when supplied with a corresponding key. */
interface PropertyProvider {
    /** Returns the value of a property corresponding to `key`. */
    fun getProperty(key: String): String?
}