package believe.react;

/**
 * An object whose state reacts to changes on an outside object.
 *
 * @param <T> the type of object being observed.
 */
public interface Observer<T> {
  /** Indicates that the observed value has changed to {@code newValue}. */
  void valueChanged(T newValue);
}
