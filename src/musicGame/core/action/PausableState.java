package musicGame.core.action;

import org.newdawn.slick.state.GameState;

public interface PausableState extends GameState {
  /**
   * Used for resetting a {@link PausableState} without necessarily loading everything from files.
   *
   * The formal way to think about it is that {@link GameState#enter} is used when entering the
   * {@link PausableState} when it wasn't already currently paused, and
   * {@link PausableState#reset} is used when the {@link PausableState} was currently paused.
   */
  void reset();
  void exitFromPausedState();
}
