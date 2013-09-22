package musicGame.core;

import org.newdawn.slick.state.BasicGameState;

public abstract class GameStateBase extends BasicGameState {

	public GameStateBase() {
		GameStateRegistry.getInstance().addEntry(this.hashCode());
	}

	@Override
	public int getID() {
		return GameStateRegistry.getInstance().getEntry(this.hashCode());
	}

}
