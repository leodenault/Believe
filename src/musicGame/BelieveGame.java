package musicGame;

import musicGame.state.FlowFilePickerMenuState;
import musicGame.state.GamePausedOverlay;
import musicGame.state.GameStateBase;
import musicGame.state.MainMenuState;
import musicGame.state.PlayGameState;

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
			game.setDisplayMode(game.getScreenWidth(), game.getScreenHeight(), true);
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
		this.enterState(GameStateBase.getStateID(MainMenuState.class));
	}
	
	
}
