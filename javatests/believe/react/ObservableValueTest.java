package believe.react;

import static com.google.common.truth.Truth.assertThat;

import javax.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link ObservableValue}. */
final class ObservableValueTest {
  @Test
  void setValue_executesNotificationStrategy() {
    ObservableValue<String> observableValue =
        ObservableValue.of("initial value", NotificationStrategy.ONLY_NOTIFY_ON_VALUE_CHANGE);
    FakeObserver observer = new FakeObserver();
    observableValue.addObserver(observer);

    observableValue.setValue("new value");
    assertThat(observer.valueChanged).isTrue();
    observer.valueChanged = false;
    observableValue.setValue("new value");
    assertThat(observer.valueChanged).isFalse();
  }

  private static final class FakeObserver implements Observer<String> {
    boolean valueChanged = false;

    @Override
    public void valueChanged(String newValue) {
      valueChanged = true;
    }
  }
}
