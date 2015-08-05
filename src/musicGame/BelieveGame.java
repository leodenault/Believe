package musicGame;

import musicGame.state.FlowFilePickerMenuState;
import musicGame.state.GamePausedOverlay;
import musicGame.state.GameStateBase;
import musicGame.state.MainMenuState;
import musicGame.state.OptionsMenuState;
import musicGame.state.PlatformingState;
import musicGame.state.PlayFlowFileState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class BelieveGame extends StateBasedGame {
	
	private static final int DEBUG_WIDTH = 800;
	private static final int DEBUG_HEIGHT = 600;
	
	private boolean debug;
	
	public BelieveGame(String title) {
		super(title);
		debug = false;
	}
	
	public void run() {
		try {
			AppGameContainer game = new AppGameContainer(this);
			game.setShowFPS(false);
			
			int width = debug ? DEBUG_WIDTH : game.getScreenWidth();
			int height = debug ? DEBUG_HEIGHT : game.getScreenHeight();
			
			game.setDisplayMode(width, height, !debug);
			game.setMouseGrabbed(!debug);
			game.start();
		}
		catch (SlickException e) {
			System.out.println("A Slick Exception! Here's the message: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new MainMenuState());
		this.addState(new OptionsMenuState());
		this.addState(new FlowFilePickerMenuState());
		this.addState(new PlayFlowFileState());
		this.addState(new GamePausedOverlay());
		this.addState(new PlatformingState());
		this.enterState(GameStateBase.getStateID(MainMenuState.class));
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
