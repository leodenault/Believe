package musicGame.core.action;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;

import musicGame.state.GameStateBase;
import musicGame.state.PlatformingState;

public class LocalLoadGameAction extends ChangeStateAction {

	public LocalLoadGameAction(StateBasedGame game) {
		super(PlatformingState.class, game);
	}
	
	@Override
	public void componentActivated(AbstractComponent component) {
		try {
			GameStateBase.getStateInstance(PlatformingState.class).setUp();
		} catch (SlickException e) {
			// TODO: Properly handle the exception
			e.printStackTrace();
		}
		super.componentActivated(component);
	}
}
