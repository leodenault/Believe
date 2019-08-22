package believe.react;

import java.util.Collection;

/**
 * An object that can be observed by an outsider. Notifies the outsider of any changes to the state
 * of {@code T}.
 *
 * @param <T> the type of the object being observed.
 */
public interface Observable<T> {
  /**
   * Adds an {@link Observer} to this {@link Observable} that will be notified whenever this
   * instance's value changes.
   *
   * @return this for chaining method calls.
   */
  Observable<T> addObserver(Observer<T> observer);

  /**
   * Adds many {@link Observer} instances to this {@link Observable} that will be notified whenever
   * this instance's value changes.
   *
   * @return this for chaining method calls.
   */
  Observable<T> addAllObservers(Collection<? extends Observer<T>> observers);
}
