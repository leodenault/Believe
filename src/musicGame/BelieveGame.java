package musicGame;

import musicGame.core.GameStateRegistry;
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
		super(title);
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
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new MainMenuState());
		this.addState(new FlowFilePickerMenuState());
		this.addState(new PlayGameState());
		this.enterState(GameStateRegistry.getInstance().getEntry(MainMenuState.class));
	}
	
	
}
