package believe.react;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link ValueObserver}. */
final class ValueObserverTest {
  private final ValueObserver<String> valueObserver = new ValueObserver<>("initial value");

  @Test
  void get_valueNeverChanged_returnsInitialValue() {
    assertThat(valueObserver.get()).isEqualTo("initial value");
  }

  @Test
  void get_valueChanged_returnsNewValue() {
    valueObserver.valueChanged("new value");

    assertThat(valueObserver.get()).isEqualTo("new value");
  }
}
