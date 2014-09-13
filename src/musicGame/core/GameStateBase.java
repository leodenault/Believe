package musicGame.core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;

public abstract class GameStateBase extends NiftyOverlayBasicGameState {

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
	
	@Override
	protected void initGameAndGUI(GameContainer container, final StateBasedGame game)
			throws SlickException {
		initNifty(container, game, new PlainSlickInputSystem());
	}
	
	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		if (!this.niftyXmlFile.equals(NO_XML_FILE)) {
			nifty.addXml(this.niftyXmlFile);
		}
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame game) throws SlickException {
		// Leave as a hook for subclasses.
	}

	@Override
	protected void leaveState(GameContainer container, StateBasedGame game) throws SlickException {
		// Leave as a hook for subclasses.
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		// Leave as a hook for subclasses.
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		// Leave as a hook for subclasses.
	}
	
	protected void updateScreen() {
		Nifty nifty = this.getNifty();
		nifty.gotoScreen(this.getClass().getSimpleName());
		nifty.update();
	}
}
