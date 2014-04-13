package musicGame.gui;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class VerticalKeyboardScrollpanel implements Controller {

	private Element childRoot;
	
	public void moveBy(int distance) {
		this.childRoot.startEffect(EffectEventId.onCustom, null, "move");
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen, Element element,
			Properties parameter, Attributes controlDefinitionAttributes) {
		this.childRoot = element.findElementByName("#childRoot");
	}

	@Override
	public void init(Properties parameter,
			Attributes controlDefinitionAttributes) {
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
