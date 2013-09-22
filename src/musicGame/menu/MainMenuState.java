package musicGame.menu;

import musicGame.GUI.DirectionalPanel;
import musicGame.GUI.MenuSelection;
import musicGame.GUI.MenuSelectionGroup;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends MenuState {

	private static final int BUTTON_WIDTH = 500;
	private static final int BUTTON_HEIGHT = 100;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		
		this.selections = new MenuSelectionGroup();
		this.selectionPanel = new DirectionalPanel<MenuSelection>(container);
		this.selectionPanel.setSpacing(75);
		
		MenuSelection playDefaultLevel = new MenuSelection(container, "Play Default Level", 0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
		MenuSelection selectCustomLevel = new MenuSelection(container, "Play Custom Level", 0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
		MenuSelection exit = new MenuSelection(container, "Exit", 0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
		this.selections.add(playDefaultLevel);
		this.selections.add(selectCustomLevel);
		this.selections.add(exit);
		for (MenuSelection selection : this.selections) {
			this.selectionPanel.add(selection);
			selection.setActiveBackgroundColor(new Color(0xFF0000));
			selection.setActiveTextColor(new Color(0xFFFFFF));
		}
		
		this.selectionPanel.setLocation(
				(container.getWidth() - this.selectionPanel.getWidth()) / 2,
				(container.getHeight() - this.selectionPanel.getHeight()) / 2);
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		for (MenuSelection selection : this.selections) {
			selection.render(container, g);
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
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
