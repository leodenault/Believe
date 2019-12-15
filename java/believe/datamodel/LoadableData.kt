package believe.datamodel

/** Data that can be loaded from disk.  */
interface LoadableData<D> {
    /** Loads the data from disk and returns it on success.  */
    fun load(): D?
}
