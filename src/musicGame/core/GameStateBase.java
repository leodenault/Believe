package musicGame.core;

import org.newdawn.slick.state.BasicGameState;

public abstract class GameStateBase extends BasicGameState {

	protected static final String NO_XML_FILE = "";
	
	private String niftyXmlFile;
	
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
