package believe.datamodel;

/**
 * A {@link MutableData} encapsulating a single value.
 *
 * @param <T> the type of the mutable value.
 */
public final class MutableValue<T> implements MutableData<T> {
  private T value;

  private MutableValue(T initialValue) {
    this.value = initialValue;
  }

  public static <T> MutableValue<T> of(T initialValue) {
    return new MutableValue<>(initialValue);
  }

  @Override
  public T get() {
    return value;
  }

  @Override
  public void update(T data) {
    value = data;
  }
}
