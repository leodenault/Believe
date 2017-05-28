package musicGame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

import musicGame.core.FontLoader;
import musicGame.core.JarClasspathLocation;
import musicGame.state.ArcadeState;
import musicGame.state.FlowFilePickerMenuState;
import musicGame.state.GamePausedOverlay;
import musicGame.state.GameStateBase;
import musicGame.state.MainMenuState;
import musicGame.state.OptionsMenuState;
import musicGame.state.PlatformingState;
import musicGame.state.PlayFlowFileState;

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
			ResourceLoader.addResourceLocation(new JarClasspathLocation());
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
		loadResources();
		this.addState(new MainMenuState(container, this));
		this.addState(new OptionsMenuState(container, this));
		this.addState(new FlowFilePickerMenuState(container, this));
		this.addState(new PlayFlowFileState(this));
		this.addState(new GamePausedOverlay(container, this));
		this.addState(new PlatformingState(container, this));
		this.addState(new ArcadeState(container, this));
		this.enterState(GameStateBase.getStateID(MainMenuState.class));
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	private void loadResources() {
		Log.info("Loading resources...");
		FontLoader.getInstance().load();
		Log.info("Finished loading resources.");
	}
}
