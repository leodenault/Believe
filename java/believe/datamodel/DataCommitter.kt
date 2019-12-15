package believe.datamodel

/**
 * Commits data to disk.
 *
 * @param D the type of data being committed to disk.
 */
interface DataCommitter<D> {
    /** Commits [data] to disk.  */
    fun commit(data: D)
}
