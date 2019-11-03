package believe.react;

import java.util.Objects;
import java.util.Set;

/** A strategy executed notifying observers when an {@link ObservableValue} has changed. */
public enum NotificationStrategy {
  ONLY_NOTIFY_ON_VALUE_CHANGE(
      new Implementation() {
        @Override
        public <T> void notifyObservers(T oldValue, T newValue, Set<Observer<T>> observers) {
          if (!Objects.equals(oldValue, newValue)) {
            Implementation.notifyObservers(newValue, observers);
          }
        }
      }),
  ALWAYS_NOTIFY(
      new Implementation() {
        @Override
        public <T> void notifyObservers(T oldValue, T newValue, Set<Observer<T>> observers) {
          Implementation.notifyObservers(newValue, observers);
        }
      });

  private interface Implementation {
    <T> void notifyObservers(T oldValue, T newValue, Set<Observer<T>> observers);

    static <T> void notifyObservers(T newValue, Set<Observer<T>> observers) {
      for (Observer<T> observer : observers) {
        observer.valueChanged(newValue);
      }
    }
  }

  private final Implementation implementation;

  NotificationStrategy(Implementation implementation) {
    this.implementation = implementation;
  }

  <T> void execute(T oldValue, T newValue, Set<Observer<T>> observers) {
    implementation.notifyObservers(oldValue, newValue, observers);
  }
}
