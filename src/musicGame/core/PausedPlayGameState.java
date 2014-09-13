package musicGame.core;

import musicGame.core.PlayGameState.SubPlayGameState;
import musicGame.core.action.ChangeStateAction;
import musicGame.gui.MenuSelection;
import musicGame.gui.MenuSelectionGroup;
import musicGame.levelFlow.FlowComponent;
import musicGame.menu.MainMenuState;
import musicGame.menu.action.MenuAction;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.screen.Screen;

public class PausedPlayGameState extends HierarchicalGameState implements SubPlayGameState {
	private static String SCREEN_ID = "PausedPlayGameState";
	public PausedPlayGameState(String niftyXmlFile) {
		super(niftyXmlFile);
	}

	private FlowComponent component;
	private ChangeStateAction activeAction;
	private MenuSelectionGroup selections;
	private MenuSelection exitLevel;
	private MenuSelection restart;

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
			case Input.KEY_ESCAPE:
				activeAction.performAction();
				break;
			case Input.KEY_ENTER:
				this.selections.getCurrentSelection().activate();
				break;
			case Input.KEY_UP:
				this.selections.selectNext();
				break;
			case Input.KEY_DOWN:
				this.selections.selectPrevious();
				break;
		}
	}
	
	public void reset() {
		this.selections.setPlaySound(false);
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}

	@Override
	public void setFlowComponent(FlowComponent component) {
		this.component = component;
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.initGameAndGUI(container, game);
		this.activeAction = new ChangeStateAction(ActivePlayGameState.class, game);
		Screen screen = getNifty().getScreen(SCREEN_ID);
		
		this.exitLevel = screen.findControl("exitLevel", MenuSelection.class);
		this.restart = screen.findControl("restart", MenuSelection.class);
		
		this.exitLevel.setMenuAction(new ChangeStateAction(MainMenuState.class, game));
		this.restart.setMenuAction(new MenuAction() {
			@Override
			public void performAction() {
				component.stop();
				component.play();
				activeAction.performAction();
			}
		});
		
		this.selections = new MenuSelectionGroup();
		this.selections.add(exitLevel);
		this.selections.add(restart);
	}

	@Override
	protected void enterState(GameContainer container, final StateBasedGame game)
			throws SlickException {
		super.enterState(container, game);
		updateScreen();
		this.selections.setPlaySound(false);
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}
	
	@Override
	public void renderGame(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		this.component.render(container, g);
	}
}
