package believe.gamestate;

import org.newdawn.slick.state.GameState;

/**
 * A state which is considered temporary, like a activate screen. This type if state retains a
 * reference to the state which preceded it so it can optionally return to its preceding state.
 */
interface TemporaryState<PrecedingStateT extends PrecedingState> extends GameState {
  /** Sets the state that precedes this one. */
  void setPrecedingState(PrecedingStateT state);
}
