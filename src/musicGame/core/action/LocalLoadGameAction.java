package musicGame.core.action;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;

import musicGame.state.GameStateBase;
import musicGame.state.LevelState;

public class LocalLoadGameAction extends ChangeStateAction {
	
	private Class<? extends LevelState> state;

	public LocalLoadGameAction(StateBasedGame game, Class<? extends LevelState> state) {
		super(state, game);
		this.state = state;
	}
	
	@Override
	public void componentActivated(AbstractComponent component) {
		try {
			GameStateBase.getStateInstance(state).setUp();
		} catch (SlickException e) {
			// TODO: Properly handle the exception
			e.printStackTrace();
		}
		super.componentActivated(component);
	}
}
