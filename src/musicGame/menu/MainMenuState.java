package musicGame.menu;

import musicGame.core.action.ChangeStateAction;
import musicGame.gui.MenuSelection;
import musicGame.menu.action.ExitGameAction;

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
		
		this.selectionPanel.setSpacing(75);
		
		MenuSelection playDefaultLevel = new MenuSelection(container, "Play Default Level", BUTTON_WIDTH, BUTTON_HEIGHT);
		MenuSelection selectCustomLevel = new MenuSelection(container, "Play Custom Level", BUTTON_WIDTH, BUTTON_HEIGHT);
		MenuSelection exit = new MenuSelection(container, "Exit", BUTTON_WIDTH, BUTTON_HEIGHT);
		selectCustomLevel.setMenuAction(new ChangeStateAction(FlowFilePickerMenuState.class, game));
		exit.setMenuAction(new ExitGameAction(container));
		
		this.selections.add(playDefaultLevel);
		this.selections.add(selectCustomLevel);
		this.selections.add(exit);
		this.selectionPanel.addAll(this.selections.getSelections());

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
		} else if (key == Input.KEY_ENTER) {
			this.selections.getCurrentSelection().activate();
		}
	}
}
