package musicGame;

import musicGame.core.ActivePlayGameState;
import musicGame.core.GameStateRegistry;
import musicGame.core.PausedPlayGameState;
import musicGame.core.PlayGameState;
import musicGame.menu.FlowFilePickerMenuState;
import musicGame.menu.MainMenuState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import de.lessvoid.nifty.slick2d.NiftyStateBasedGame;

public class BelieveGame extends NiftyStateBasedGame {
	
	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 600;
	
	public BelieveGame(String title) {
		super(title, true);
	}
	
	public static void main(String args[]) {
		BelieveGame b = new BelieveGame("Believe");
		b.run();
	}
	
	public void run() {
		try {
			AppGameContainer game = new AppGameContainer(this);
			game.setShowFPS(false);
			game.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
			game.start();
		}
		catch (SlickException e) {
			System.out.println("A Slick Exception! Here's the message: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new MainMenuState("musicGame/menu/MainMenuState.xml"));
		this.addState(new FlowFilePickerMenuState("musicGame/menu/FlowFilePickerMenuState.xml"));
		PlayGameState playGame = new PlayGameState();
		ActivePlayGameState activeGame = new ActivePlayGameState();
		PausedPlayGameState pausedGame = new PausedPlayGameState("musicGame/core/PausedPlayGameState.xml");
		playGame.setDefaultState(activeGame);
		playGame.addState(pausedGame);
		this.addState(playGame);
		this.addState(activeGame);
		this.addState(pausedGame);
		this.enterState(GameStateRegistry.getInstance().getEntry(MainMenuState.class));
	}
	
	
}
