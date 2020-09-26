package believe.core;

/** An object whose state can be updated at regular intervals. */
public interface Updatable {
  /**
   * Updates the internal state of this instance.
   *
   * @param delta the time, in milliseconds, that has passed since the last update.
   */
  void update(long delta);
}
