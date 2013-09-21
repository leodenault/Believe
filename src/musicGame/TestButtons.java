package musicGame;

import musicGame.GUI.MenuSelection;
import musicGame.GUI.MenuSelectionGroup;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class TestButtons extends BasicGame {
	
	private MenuSelectionGroup selections;
	
	public TestButtons(String title) {
		super(title);
	}
	
	public static void main(String args[]) {
		TestButtons b = new TestButtons("");
		b.run();
	}
	
	public void run() {
		try {
			AppGameContainer game = new AppGameContainer(this);
			game.setShowFPS(false);
			game.setDisplayMode(800, 600, false);
			game.start();
		}
		catch (SlickException e) {}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		for (MenuSelection selection : this.selections) {
			selection.render(container, g);
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		selections = new MenuSelectionGroup();
		
		MenuSelection playDefaultLevel = new MenuSelection(container, "Play Default Level", 0, 0, 500, 100);
		MenuSelection selectCustomLevel = new MenuSelection(container, "Play Custom Level", 0, 0, 500, 100);
		MenuSelection exit = new MenuSelection(container, "Exit", 0, 0, 500, 100);
		selections.add(playDefaultLevel);
		selections.add(selectCustomLevel);
		selections.add(exit);
		
		int i = 0;
		for (MenuSelection selection : this.selections) {
			selection.setActiveBackgroundColor(new Color(0xFF0000));
			selection.setActiveTextColor(new Color(0xFFFFFF));
			selection.setLocation((800 - 500) / 2, 100 + (i * 150));
			i++;
		}
		
		selections.select(0);
		selections.setPlaySound(true);
	}

	@Override
	public void update(GameContainer container, int arg1) throws SlickException {
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (key == Input.KEY_DOWN) {
			this.selections.selectNext();
		} else if (key == Input.KEY_UP) {
			this.selections.selectPrevious();
		}
	}
}
