package musicGame.menu;

import musicGame.core.GameStateBase;
import musicGame.core.action.ChangeStateAction;
import musicGame.gui.MenuSelection;
import musicGame.gui.MenuSelectionGroup;
import musicGame.menu.action.ExitGameAction;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;

public class MainMenuState extends GameStateBase implements KeyInputHandler {

	private static final String SCREEN_ID = "MainMenuState";
	
	private MenuSelectionGroup selections;
	private MenuSelection playDefaultLevel;
	private MenuSelection playCustomLevel;
	private MenuSelection exit;
	
	public MainMenuState() {
		System.out.println(this.getClass().getName());
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (key == Input.KEY_DOWN) {
			this.selections.selectNext();
		} else if (key == Input.KEY_UP) {
			this.selections.selectPrevious();
		} else if (key == Input.KEY_ENTER) {
//			this.selections.getCurrentSelection().activate();
		}
	}
	
	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("musicGame/menu/MainMenuState.xml", SCREEN_ID);
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.initGameAndGUI(container, game);

		Screen screen = this.getNifty().getScreen(SCREEN_ID);
		screen.addKeyboardInputHandler(new NiftyInputMapping() {
			
			@Override
			public NiftyInputEvent convert(KeyboardInputEvent inputEvent) {
				if (inputEvent != null && inputEvent.getKey() == KeyboardInputEvent.KEY_RETURN) {
					return NiftyInputEvent.Activate;
				}
				return null;
			}
		}, this);
		this.playDefaultLevel = screen.findControl("playDefaultLevel", MenuSelection.class);
		this.playCustomLevel = screen.findControl("playCustomLevel", MenuSelection.class);
		this.exit = screen.findControl("exit", MenuSelection.class);
		
		this.playCustomLevel.setMenuAction(new ChangeStateAction(FlowFilePickerMenuState.class, game));
		this.exit.setMenuAction(new ExitGameAction(container));
		
		this.selections = new MenuSelectionGroup();
		this.selections.add(this.playDefaultLevel);
		this.selections.add(this.playCustomLevel);
		this.selections.add(this.exit);
		
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}
	
	@Override
	protected void enterState(GameContainer container, StateBasedGame game) throws SlickException {
		super.enterState(container, game);
		this.selections.setPlaySound(false);
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
	public boolean keyEvent(NiftyInputEvent inputEvent) {
		if (inputEvent == NiftyInputEvent.Activate) {
			this.selections.getCurrentSelection().activate();
			return true;
		}
		return false;
	}
}
