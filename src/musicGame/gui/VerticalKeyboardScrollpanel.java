package musicGame.gui;

import java.util.Properties;

import musicGame.menu.action.MenuAction;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class VerticalKeyboardScrollpanel extends AbstractController implements Controller, KeyInputHandler {

	private boolean focus;
	private Nifty nifty;
	private Screen screen;
	private Element childRoot;
	private MenuSelectionGroup selections;
	
	public void moveBy(int distance) {
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen, Element element,
			Properties parameter, Attributes controlDefinitionAttributes) {
		this.nifty = nifty;
		this.screen = screen;
		this.childRoot = element.findElementByName("#childRoot");
		
		screen.addKeyboardInputHandler(new NiftyInputMapping() {
			@Override
			public NiftyInputEvent convert(KeyboardInputEvent inputEvent) {
				if (inputEvent.isKeyDown()) {
					switch (inputEvent.getKey()) {
						case KeyboardInputEvent.KEY_DOWN:
							return NiftyInputEvent.NextInputElement;
						case KeyboardInputEvent.KEY_UP:
							return NiftyInputEvent.PrevInputElement;
						case KeyboardInputEvent.KEY_RETURN:
							return NiftyInputEvent.Activate;
					}
				}
				return null;
			}
		}, this);
	}

	@Override
	public void init(Properties parameter, Attributes controlDefinitionAttributes) {
		this.selections = new MenuSelectionGroup();
	}

	@Override
	public boolean inputEvent(NiftyInputEvent inputEvent) {
		return false;
	}
	
	@Override
	public boolean keyEvent(NiftyInputEvent inputEvent) {
		if (inputEvent== null) {
			return false;
		}
		
		if (this.focus) {
			switch (inputEvent) {
				case NextInputElement:
					moveBy(1);
					this.selections.selectNext();
					break;
				case PrevInputElement:
					this.selections.selectPrevious();
					break;
				case Activate:
					this.selections.getCurrentSelection().activate();
					break;
				default:
					break;
			}
		}
		return true;
	}
	
	@Override
	public void onFocus(boolean getFocus) {
		this.focus = getFocus;
		if (this.selections.getCurrentSelection() != null) {
			if (getFocus) {
				this.selections.getCurrentSelection().select();
			} else {
				this.selections.getCurrentSelection().deselect();
			}
		}
	}

	@Override
	public void onStartScreen() {
	}
	
	public void add(ControlBuilder controlBuilder, MenuAction action) {
		Element menuSelection = controlBuilder.build(this.nifty, this.screen, this.childRoot);
		MenuSelection selection = menuSelection.getControl(MenuSelection.class);
		selection.setMenuAction(action);
		selection.setStyle(MenuSelection.Style.BORDER, "menuSelectionFlowFile-border");
		selection.setActiveStyle(MenuSelection.Style.BORDER, "menuSelectionFlowFile-active-border");
		this.selections.add(selection);
	}
	
	public void clear() {
		for (Element element : this.childRoot.getElements()) {
			element.markForRemoval();
		}
		this.selections.clear();
	}
	
	public void setPlaySound(boolean playSound) {
		this.selections.setPlaySound(playSound);
	}
}
