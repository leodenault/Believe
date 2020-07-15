package believe.react;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link ObservableValue}. */
final class ObservableValueTest {
  private final ObservableValue<String> observableValue =
      ObservableValue.of("initial value", NotificationStrategy.ONLY_NOTIFY_ON_VALUE_CHANGE);
  private final FakeObserver observer = new FakeObserver();

  @Test
  void addObserver_immediatelyNotifiesObserver() {
    observableValue.addObserver(observer);

    assertThat(observer.currentValue).isEqualTo("initial value");
  }

  @Test
  void setValue_executesNotificationStrategy() {
    observableValue.addObserver(observer);

    observableValue.setValue("new value");
    assertThat(observer.currentValue).isEqualTo("new value");
    observableValue.setValue("newest value");
    assertThat(observer.currentValue).isEqualTo("newest value");
  }

  private static final class FakeObserver implements Observer<String> {
    String currentValue = "";

    @Override
    public void valueChanged(String newValue) {
      currentValue = newValue;
    }
  }
}
