package believe.datamodel;

/**
 * A {@link DataProvider} with the ability to commit its contents to disk.
 *
 * @param <T> the type of data this class provides.
 */
public interface DataCommitter<T> extends DataProvider<T> {
  /** Commits the data held by this {@link DataProvider} to disk. */
  void commit();
}
