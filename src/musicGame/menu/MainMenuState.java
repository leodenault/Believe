package musicGame.menu;

import musicGame.core.GameStateBase;
import musicGame.core.action.ChangeStateAction;
import musicGame.gui.MenuSelection;
import musicGame.gui.MenuSelectionGroup;
import musicGame.menu.action.ExitGameAction;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.screen.Screen;

public class MainMenuState extends GameStateBase {

	private static final String SCREEN_ID = "MainMenuState";
	
	private MenuSelectionGroup selections;
	private MenuSelection playDefaultLevel;
	private MenuSelection playCustomLevel;
	private MenuSelection exit;
	
	public MainMenuState(String niftyXmlFile) {
		super(niftyXmlFile);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		switch (key) {
			case Input.KEY_DOWN:
				this.selections.selectNext();
				break;
			case Input.KEY_UP:
				this.selections.selectPrevious();
				break;
			case Input.KEY_ENTER:
				this.selections.getCurrentSelection().activate();
				break;
		}
	}
	
	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.initGameAndGUI(container, game);

		Screen screen = this.getNifty().getScreen(SCREEN_ID);
		this.playDefaultLevel = screen.findControl("playDefaultLevel", MenuSelection.class);
		this.playCustomLevel = screen.findControl("playCustomLevel", MenuSelection.class);
		this.exit = screen.findControl("exit", MenuSelection.class);
		
		this.playCustomLevel.setMenuAction(new ChangeStateAction(FlowFilePickerMenuState.class, game));
		this.exit.setMenuAction(new ExitGameAction(container));
		
		this.selections = new MenuSelectionGroup();
		this.selections.add(this.playDefaultLevel);
		this.selections.add(this.playCustomLevel);
		this.selections.add(this.exit);
	}
	
	@Override
	protected void enterState(GameContainer container, StateBasedGame game) throws SlickException {
		this.updateScreen();
		this.selections.setPlaySound(false);
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}
}
