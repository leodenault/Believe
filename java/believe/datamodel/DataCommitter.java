package believe.datamodel;

/** Model data whose contents can be committed to disk by calling {@link #commit()}. */
public interface DataCommitter {
  /** Commits the data held by this {@link MutableData} to disk. */
  void commit();
}
