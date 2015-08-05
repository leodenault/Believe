package musicGame.core.action;

import musicGame.state.GameStateBase;
import musicGame.state.PlatformingState;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;

public class LocalLoadGameAction extends ChangeStateAction {

	public LocalLoadGameAction(StateBasedGame game) {
		super(PlatformingState.class, game);
	}
	
	@Override
	public void componentActivated(AbstractComponent component) {
		try {
			GameStateBase.getStateInstance(PlatformingState.class).reset();
		} catch (SlickException e) {
			// TODO: Properly handle the exception
			e.printStackTrace();
		}
		super.componentActivated(component);
	}
}
