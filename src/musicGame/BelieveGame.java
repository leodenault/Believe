package musicGame;

import musicGame.menu.MainMenuState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class BelieveGame extends StateBasedGame {
	
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
		catch (SlickException e) {}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		MainMenuState mainMenu = new MainMenuState();
		this.addState(mainMenu);
		this.enterState(mainMenu.getID());
	}
}
