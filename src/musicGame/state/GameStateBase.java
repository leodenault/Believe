package musicGame.state;

import musicGame.core.GameStateRegistry;

import org.newdawn.slick.state.BasicGameState;

public abstract class GameStateBase extends BasicGameState {

	public GameStateBase() {
		GameStateRegistry.getInstance().addEntry(this);
	}
	
	public static int getStateID(Class<? extends GameStateBase> state) {
		return GameStateRegistry.getInstance().getEntryID(state);
	}
	
	public static <T extends GameStateBase> T getStateInstance(Class<T> state) {
		return GameStateRegistry.getInstance().getEntryState(state);
	}

	@Override
	public int getID() {
		return GameStateRegistry.getInstance().getEntryID(this.getClass());
	}
}
