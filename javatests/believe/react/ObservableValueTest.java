package believe.react;

import static com.google.common.truth.Truth.assertThat;

import javax.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link ObservableValue}. */
final class ObservableValueTest {
  private static final class FakeObserver implements Observer<String> {
    @Nullable String value;

    @Override
    public void valueChanged(String newValue) {
      value = newValue;
    }
  }

  private final FakeObserver observer = new FakeObserver();
  private final ObservableValue<String> observableValue = new ObservableValue<>("initial value");

  @BeforeEach
  void setUp() {
    observableValue.addObserver(observer);
  }

  @Test
  void setValue_valueIsSame_doesNothing() {
    observableValue.setValue("initial value");

    assertThat(observer.value).isNull();
  }

  @Test
  void setValue_valueIsDifferent_notifiesObservers() {
    observableValue.setValue("new value");

    assertThat(observer.value).isEqualTo("new value");
  }
}
