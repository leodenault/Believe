package musicGame.gui;

import java.util.HashMap;
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
	
	public enum Style {
		BORDER, BACKGROUND, TEXT;
	}

	private static final String DEFAULT_SELECTION_SOUND = "res/sfx/selection.ogg";
	private static final String DEFAULT_ACTIVATION_SOUND = "res/sfx/selection_picked.ogg";
	
	private HashMap<Style, String> styles;
	private HashMap<Style, String> activeStyles;
	private Sound selectionSound;
	private Sound activationSound;
	private MenuAction action;
	private Element border;
	private Element background;
	private Element text;
	
	private boolean playSound;
	private boolean isSelected;
	
	public MenuSelection()
			throws SlickException {
		this.playSound = false;
		this.isSelected = false;
		this.selectionSound = new Sound(DEFAULT_SELECTION_SOUND);
		this.activationSound = new Sound(DEFAULT_ACTIVATION_SOUND);
		this.styles = new HashMap<Style, String>();
		this.activeStyles = new HashMap<Style, String>();
		this.styles.put(Style.BORDER, "menuSelection-border");
		this.styles.put(Style.BACKGROUND, "menuSelection-background");
		this.styles.put(Style.TEXT, "menuSelection-text");
		this.activeStyles.put(Style.BORDER, "menuSelection-active-border");
		this.activeStyles.put(Style.BACKGROUND, "menuSelection-active-background");
		this.activeStyles.put(Style.TEXT, "menuSelection-active-text");
	}

	public void select() {
		if (this.playSound) {
			this.selectionSound.play();
		}
		this.border.setStyle(this.activeStyles.get(Style.BORDER));
		this.background.setStyle(this.activeStyles.get(Style.BACKGROUND));
		this.text.setStyle(this.activeStyles.get(Style.TEXT));
		this.isSelected = true;
	}
	
	public void deselect() {
		this.border.setStyle(this.styles.get(Style.BORDER));
		this.background.setStyle(this.styles.get(Style.BACKGROUND));
		this.text.setStyle(this.styles.get(Style.TEXT));
		this.isSelected = false;
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
	
	public void setStyle(Style style, String styleName) {
		this.styles.put(style, styleName);
	}
	
	public void setActiveStyle(Style style, String styleName) {
		this.activeStyles.put(style, styleName);
	}
	
	public boolean isSelected() {
		return this.isSelected;
	}
	
	public boolean isPlayingSound() {
		return playSound;
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
