package believe.datamodel;

/**
 * A {@link MutableData} with the ability to commit its contents to disk.
 *
 * @param <T> the type of data this managed by this {@link MutableDataCommitter}.
 */
public interface MutableDataCommitter<T> extends MutableData<T>, DataCommitter {}
