package musicGame.gui;

import java.util.Properties;

import musicGame.menu.action.MenuAction;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class MenuSelection implements Controller {

	private static final String DEFAULT_SELECTION_SOUND = "res/sfx/selection.ogg";
	private static final String DEFAULT_ACTIVATION_SOUND = "res/sfx/selection_picked.ogg";
		
	private Sound selectionSound;
	private Sound activationSound;
	private MenuAction action;
	private Element border;
	private Element background;
	private Element text;
	
	private boolean playSound;
	
	public MenuSelection()
			throws SlickException {
		this.playSound = false;
		this.selectionSound = new Sound(DEFAULT_SELECTION_SOUND);
		this.activationSound = new Sound(DEFAULT_ACTIVATION_SOUND);
	}

	public void select() {
		if (this.playSound) {
			this.selectionSound.play();
		}
		this.border.setStyle("menuSelection-active-border");
		this.background.setStyle("menuSelection-active-background");
		this.text.setStyle("menuSelection-active-text");
	}
	
	public void deselect() {
		this.border.setStyle("menuSelection-border");
		this.background.setStyle("menuSelection-background");
		this.text.setStyle("menuSelection-text");
	}
	
	public void activate() {
		this.activationSound.play();
		if (this.action != null) {
			this.action.performAction();
		}
	}
	
	public void setPlaySound(boolean playSound) {
		this.playSound = playSound;
	}
	
	public void setMenuAction(MenuAction action) {
		this.action = action;
	}

	@Override
	public void bind(Nifty nifty, Screen screen, Element element,
			Properties parameter, Attributes controlDefinitionAttributes) {
		this.border = element;
		this.background = this.border.findElementByName("#background");
		this.text = this.border.findElementByName("#text");
	}

	@Override
	public void init(Properties parameter, Attributes controlDefinitionAttributes) {
		
	}

	@Override
	public boolean inputEvent(NiftyInputEvent inputEvent) {
		return true;
	}

	@Override
	public void onFocus(boolean getFocus) {
		
	}

	@Override
	public void onStartScreen() {
		
	}
}
