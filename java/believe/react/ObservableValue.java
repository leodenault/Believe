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
  private final NotificationStrategy notificationStrategy;

  /**
   * Instantiates an {@link ObservableValue}.
   *
   * @param initialValue the initial value contained within this instance.
   */
  private ObservableValue(T initialValue, NotificationStrategy notificationStrategy) {
    value = initialValue;
    this.notificationStrategy = notificationStrategy;
    observers = new HashSet<>();
  }

  public static <T> ObservableValue<T> of(T initialValue) {
    return of(initialValue, NotificationStrategy.ONLY_NOTIFY_ON_VALUE_CHANGE);
  }

  public static <T> ObservableValue<T> of(
      T initialValue, NotificationStrategy notificationStrategy) {
    return new ObservableValue<>(initialValue, notificationStrategy);
  }

  /**
   * Sets the new value to be contained within this instance.
   *
   * <p>Notification of observers depends on the {@link NotificationStrategy} defined for this
   * instance.
   */
  public void setValue(T newValue) {
    T oldValue = value;
    value = newValue;
    notificationStrategy.execute(oldValue, value, observers);
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
