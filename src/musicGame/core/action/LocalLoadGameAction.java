package musicGame.core.action;

import musicGame.state.ArcadeState;
import musicGame.state.GameStateBase;
import musicGame.state.LevelState;
import musicGame.state.PlatformingState;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;

public class LocalLoadGameAction extends ChangeStateAction {

	private boolean platforming;
	
	public LocalLoadGameAction(StateBasedGame game, boolean platforming) {
		super(PlatformingState.class, game);
		this.platforming = platforming;
	}
	
	@Override
	public void componentActivated(AbstractComponent component) {
		try {
			Class<? extends LevelState> state =
					platforming ? PlatformingState.class : ArcadeState.class;
			GameStateBase.getStateInstance(state).setUp();
		} catch (SlickException e) {
			// TODO: Properly handle the exception
			e.printStackTrace();
		}
		super.componentActivated(component);
	}
}
