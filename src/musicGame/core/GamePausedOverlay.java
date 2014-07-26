package musicGame.core;

import musicGame.gui.MenuSelection;
import musicGame.gui.MenuSelectionGroup;

import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;

public class GamePausedOverlay {
	
	private static final String SCREEN_NAME = "GamePausedOverlay";
	
	private PlayGameState containingState;
	private MenuSelectionGroup selections;
	private Element layer;
	private MenuSelection exitLevel;
	private MenuSelection restart;

	public GamePausedOverlay(PlayGameState containingState) {
		this.containingState = containingState;
	}
	
	public void init(Nifty nifty, StateBasedGame game) {
		/*Screen screen = nifty.getScreen(SCREEN_NAME);
		this.layer = screen.findElementByName("layer");
		
		this.exitLevel = screen.findControl("exitLevel", MenuSelection.class);
		this.restart = screen.findControl("restart", MenuSelection.class);
		
		this.exitLevel.setMenuAction(new ChangeStateAction(MainMenuState.class, game));
		this.restart.setMenuAction(new MenuAction() {
			@Override
			public void performAction() {
				containingState.restart();
			}
		});
		
		this.selections = new MenuSelectionGroup();
		this.selections.add(this.exitLevel);
		this.selections.add(this.restart);
		
		this.selections.select(0);
		this.selections.setPlaySound(true);*/
	}
	
	public void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("musicGame/core/GamePausedOverlay.xml", SCREEN_NAME);
	}
	
	public void keyPressed(int key) {
		if (this.layer.isVisible()) {
			switch (key) {
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
	}
	
	public boolean isVisible() {
		return this.layer.isVisible();
	}
	
	public void setVisible(boolean visible) {
		this.layer.setVisible(visible);
	}
	
	public void reset() {
		this.selections.setPlaySound(false);
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}
}
