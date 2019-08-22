package believe.react;

import java.util.function.Supplier;

/**
 * Simple observer that updates its internal reference to an {@link Observable} and supplies the
 * value on demand.
 *
 * @param <T> the type of the value being observed.
 */
public final class ValueObserver<T> implements Observer<T>, Supplier<T> {
  private T value;

  public ValueObserver(T initialValue) {
    value = initialValue;
  }

  @Override
  public void valueChanged(T newValue) {
    value = newValue;
  }

  @Override
  public T get() {
    return value;
  }
}
