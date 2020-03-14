package believe.app

import believe.gamestate.StateController

/** Selects a state upon app startup. */
interface StateSelector {
    /**
     * Select a state at app startup.
     *
     * @param stateController the [StateController] used in selecting a state.
     */
    fun selectState(stateController: StateController)
}