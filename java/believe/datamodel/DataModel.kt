package believe.datamodel

/** Returns a [LoadableData] instance that always returns [loadedData]. */
fun <T> loadableDataOf(loadedData: T) = object : LoadableData<T> {
    override fun load(): T? = loadedData
}
