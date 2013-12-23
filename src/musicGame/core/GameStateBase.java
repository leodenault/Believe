package musicGame.core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;

public abstract class GameStateBase extends NiftyOverlayBasicGameState {

	public GameStateBase() {
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
	protected void enterState(GameContainer container, StateBasedGame game) throws SlickException {
		// Leave as a hook for subclasses.
	}

	@Override
	protected void leaveState(GameContainer container, StateBasedGame game) throws SlickException {
		// Leave as a hook for subclasses.
	}
}
