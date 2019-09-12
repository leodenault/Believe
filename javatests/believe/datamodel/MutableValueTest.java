package believe.datamodel;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link MutableValue}. */
final class MutableValueTest {
  private final MutableValue<String> mutableValue = MutableValue.of("initial value");

  @Test
  void get_noUpdates_returnsInitialValue() {
    assertThat(mutableValue.get()).isEqualTo("initial value");
  }

  @Test
  void get_valueUpdated_returnsUpdatedValue() {
    mutableValue.update("updated value");

    assertThat(mutableValue.get()).isEqualTo("updated value");
  }
}
