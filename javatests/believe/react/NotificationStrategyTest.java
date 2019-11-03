package believe.react;

import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

final class NotificationStrategyTest {
  private final FakeObserver observer = new FakeObserver();

  @Test
  void onlyNotifyOnValueChange_valueChanged_notifiesObservers() {
    NotificationStrategy.ONLY_NOTIFY_ON_VALUE_CHANGE.execute("old", "new", hashSetOf(observer));

    assertThat(observer.valueChanged).isTrue();
  }

  @Test
  void onlyNotifyOnValueChange_valueDidNotChange_doesNotNotifyObservers() {
    NotificationStrategy.ONLY_NOTIFY_ON_VALUE_CHANGE.execute("old", "old", hashSetOf(observer));

    assertThat(observer.valueChanged).isFalse();
  }

  @Test
  void alwaysNotify_valueChanged_notifiesObservers() {
    NotificationStrategy.ALWAYS_NOTIFY.execute("old", "new", hashSetOf(observer));

    assertThat(observer.valueChanged).isTrue();
  }

  @Test
  void alwaysNotify_valueDidNotChange_notifiesObservers() {
    NotificationStrategy.ALWAYS_NOTIFY.execute("old", "old", hashSetOf(observer));

    assertThat(observer.valueChanged).isTrue();
  }

  private static final class FakeObserver implements Observer<String> {
    boolean valueChanged;

    @Override
    public void valueChanged(String newValue) {
      valueChanged = true;
    }
  }
}
