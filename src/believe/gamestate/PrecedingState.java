package believe.gamestate;

import org.newdawn.slick.state.GameState;

/**
 * A state which precedes another one, allowing the following state to optionally return to this
 * one.
 */
public interface PrecedingState extends GameState {
  /**
   * Used for resetting a {@link PrecedingState} without necessarily loading everything from files.
   * The formal way to think about it is that {@link GameState#enter} is used when entering the
   * {@link PrecedingState} when it wasn't already currently paused, and this method is used when
   * the {@link PrecedingState} was currently paused.
   */
  void reset();

  /**
   * Notifies this state that the next state is exiting and is not coming directly back to this
   * one.
   */
  void exitingFollowingState();
}
