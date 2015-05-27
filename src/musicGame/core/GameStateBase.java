package musicGame.core;

import org.newdawn.slick.state.BasicGameState;

public abstract class GameStateBase extends BasicGameState {

	protected static final String NO_XML_FILE = "";
	
	private String niftyXmlFile;
	
	public GameStateBase() {
		this(NO_XML_FILE);
	}
	
	public GameStateBase(String niftyXmlFile) {
		this.niftyXmlFile = niftyXmlFile;
		GameStateRegistry.getInstance().addEntry(this.getClass());
	}

	@Override
	public int getID() {
		return GameStateRegistry.getInstance().getEntry(this.getClass());
	}
}
