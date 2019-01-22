package believe.core;

public class Timer {
  private enum Status { STOPPED, PLAYING, PAUSED }

  private long start;
  private long current;
  private Status status;

  public Timer() {
    start = 0;
    current = 0;
    status = Status.STOPPED;
  }

  public void update(int delta) {
    if (status == Status.PLAYING) {
      current += delta;
    }
  }

  public void play() {
    status = Status.PLAYING;
  }

  public void pause() {
    status = Status.PAUSED;
  }

  public void stop() {
    if (status != Status.STOPPED) {
      status = Status.STOPPED;
      start = 0;
      current = 0;
    }
  }

  public long getElapsedTime() {
    return current - start;
  }
}
