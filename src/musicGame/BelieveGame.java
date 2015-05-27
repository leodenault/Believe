package musicGame;

import musicGame.core.GamePausedOverlay;
import musicGame.core.GameStateRegistry;
import musicGame.core.PlayGameState;
import musicGame.menu.FlowFilePickerMenuState;
import musicGame.menu.MainMenuState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class BelieveGame extends StateBasedGame {
	
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
		this.addState(new MainMenuState());
		this.addState(new FlowFilePickerMenuState());
		this.addState(new PlayGameState());
		this.addState(new GamePausedOverlay());
		this.enterState(GameStateRegistry.getInstance().getEntry(MainMenuState.class));
	}
	
	
}
