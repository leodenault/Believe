package believe.datamodel;

import java.util.function.Supplier;

/**
 * Provides an instance of a data object of type {@code T}. Allows updating the instance in-memory.
 *
 * @param <T> the type of data this class provides.
 */
public interface DataProvider<T> extends Supplier<T> {
  /** Returns the instance of type {@code T} this instances holds. */
  T get();

  /** Updates the instance of type {@code T} held by this {@link DataProvider} with {@code data}. */
  void update(T data);
}
