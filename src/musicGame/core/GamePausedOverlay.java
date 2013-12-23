package musicGame.core;

import musicGame.gui.MenuSelection;
import musicGame.gui.MenuSelectionGroup;

import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class GamePausedOverlay implements ScreenController {

	private static final String SCREEN_NAME = "GamePausedOverlay";
	
	private MenuSelectionGroup selections;
	private Element layer;
	private MenuSelection exitLevel;
	
	public void init(Nifty nifty) {
		Screen screen = nifty.getScreen(SCREEN_NAME);
		this.layer = screen.findElementByName("layer");
		
		this.exitLevel = screen.findControl("exitLevel", MenuSelection.class);
		
		this.selections = new MenuSelectionGroup();
		this.selections.add(this.exitLevel);
		
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}
	
	public void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("src/musicGame/core/GamePausedOverlay.xml", SCREEN_NAME);
	}
	
	public void keyPressed(int key) {
		switch (key) {
			case Input.KEY_ESCAPE:
				this.layer.setVisible(!this.layer.isVisible());
				if (this.layer.isVisible()) {
					this.reset();
				}
				break;
		}
	}
	
	public boolean isVisible() {
		return this.layer.isVisible();
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen) {
	}

	@Override
	public void onEndScreen() {
	}

	@Override
	public void onStartScreen() {
	}

	private void reset() {
		this.selections.setPlaySound(false);
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}
}
