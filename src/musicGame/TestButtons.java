package musicGame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class TestButtons extends BasicGame {
	public TestButtons(String title) {
		super(title);
	}
	
	public static void main(String args[]) {
		TestButtons b = new TestButtons("");
		b.run();
	}
	
	MenuSelection sel;
	
	public void run() {
		try {
			AppGameContainer game = new AppGameContainer(this);
			game.setShowFPS(false);
			game.setDisplayMode(800, 600, false);
			game.start();
		}
		catch (SlickException e) {

		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		sel.render(container, g);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		sel = new MenuSelection(container, "Blabloubli", 10, 10, 100, 50);
		sel.setActiveBackgroundColor(new Color(0xFF0000));
		sel.setActiveTextColor(new Color(0xFFFFFF));
	}

	boolean isSelected = false;
	@Override
	public void update(GameContainer container, int arg1) throws SlickException {
		Input i = container.getInput();
		if (i.isKeyPressed(Input.KEY_SPACE)) {
			if (!isSelected) {
				sel.select();
				isSelected = true;
			}
			else {
				sel.deselect();
				isSelected = false;
			}
		}
	}
}
