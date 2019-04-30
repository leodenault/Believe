package believe.action;

import believe.gamestate.temporarystate.TemporaryState;

/** Callback executed when exiting a {@link TemporaryState}. */
public interface ExitTemporaryStateAction {
  /** Exits the current temporary state, optionally transitioning to a new state. */
  void exitTemporaryState();
}
