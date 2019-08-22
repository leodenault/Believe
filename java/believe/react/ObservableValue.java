package believe.react;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A value whose state is observable by others.
 *
 * @param <T> the type of the value being observed.
 */
public final class ObservableValue<T> implements Observable<T>, Supplier<T> {
  private final Set<Observer<T>> observers;

  private T value;

  /**
   * Instantiates an {@link ObservableValue}.
   *
   * @param initialValue the initial value contained within this instance.
   */
  public ObservableValue(T initialValue) {
    value = initialValue;
    observers = new HashSet<>();
  }

  /**
   * Sets the new value to be contained within this instance. If the {@code newValue} is different
   * from the one contained within this instance be an {@link Object#equals(Object)} comparison,
   * then all {@link Observer} instances associated with this instance are notified of the change.
   */
  public void setValue(T newValue) {
    if (value.equals(newValue)) {
      return;
    }

    value = newValue;
    for (Observer<T> observer : observers) {
      observer.valueChanged(value);
    }
  }

  @Override
  public ObservableValue<T> addObserver(Observer<T> observer) {
    observers.add(observer);
    return this;
  }

  @Override
  public ObservableValue<T> addAllObservers(Collection<? extends Observer<T>> observers) {
    this.observers.addAll(observers);
    return this;
  }

  @Override
  public T get() {
    return value;
  }
}
