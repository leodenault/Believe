package musicGame.core.action;

import musicGame.state.GameStateBase;

import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

public class ChangeStateAction implements ComponentListener {

	private Class<? extends GameStateBase> state;
	
	protected StateBasedGame game;
	
	public ChangeStateAction(Class<? extends GameStateBase> state, StateBasedGame game) {
		this.state = state;
		this.game = game;
	}

	@Override
	public void componentActivated(AbstractComponent component) {
		game.enterState(GameStateBase.getStateID(state));
	}

}
