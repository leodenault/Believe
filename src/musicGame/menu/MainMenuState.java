package musicGame.menu;

import musicGame.core.action.ChangeStateAction;
import musicGame.gui.MenuSelection;
import musicGame.menu.action.ExitGameAction;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MainMenuState extends MenuState implements ScreenController {

	private MenuSelection playDefaultLevel;
	private MenuSelection playCustomLevel;
	private MenuSelection exit;
	
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
	
	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("src/musicGame/menu/MainMenuState.xml", "MainMenuState");
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.initGameAndGUI(container, game);

		this.playDefaultLevel = this.getNifty().getScreen("MainMenuState").findControl("playDefaultLevel", MenuSelection.class);
		this.playCustomLevel = this.getNifty().getScreen("MainMenuState").findControl("playCustomLevel", MenuSelection.class);
		this.exit = this.getNifty().getScreen("MainMenuState").findControl("exit", MenuSelection.class);
		
		this.playCustomLevel.setMenuAction(new ChangeStateAction(FlowFilePickerMenuState.class, game));
		this.exit.setMenuAction(new ExitGameAction(container));
		
		this.selections.add(this.playDefaultLevel);
		this.selections.add(this.playCustomLevel);
		this.selections.add(this.exit);
		
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.playDefaultLevel = screen.findControl("playDefaultLevel", MenuSelection.class);
		this.playCustomLevel = screen.findControl("playCustomLevel", MenuSelection.class);
		this.exit = screen.findControl("exit", MenuSelection.class);
	}

	@Override
	public void onEndScreen() {
		
	}

	@Override
	public void onStartScreen() {

	}
}
