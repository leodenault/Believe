package believe.datamodel

/**
 * Manages data of type [D] to make it easier for clients to request loading and storing data in
 * memory.
 *
 * @param D the type of data managed by this instance.
 */
interface DataManager<D> {
    /** Returns data designated by [name] if it can be found, otherwise null. */
    fun getDataFor(name: String): D?
}