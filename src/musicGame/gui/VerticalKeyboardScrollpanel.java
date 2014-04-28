package musicGame.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class VerticalKeyboardScrollpanel extends AbstractController implements Controller, KeyInputHandler {

	private static final int DEFAULT_GAP = 10;
	
	private boolean focus;
	private SizeValue gap = SizeValue.px(DEFAULT_GAP);
	private Nifty nifty;
	private Screen screen;
	private Element childRoot;
	private MenuSelectionGroup selections;
	private Element lastSelection;
	private Element firstSelection;
	private Element scroller;
	private Map<MenuSelection, Element> selectionElementMap;
	
	@Override
	public void bind(Nifty nifty, Screen screen, Element element,
			Properties parameter, Attributes controlDefinitionAttributes) {
		this.nifty = nifty;
		this.screen = screen;
		this.childRoot = element.findElementByName("#childRoot");
		this.scroller = element.findElementByName("#scroller");
		this.scroller.hide();
		
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
		this.selectionElementMap = new HashMap<MenuSelection, Element>();
		
		String gap = controlDefinitionAttributes.get("gap");
		if (gap != null) {
			this.gap = new SizeValue(gap);
		}
	}

	@Override
	public boolean inputEvent(NiftyInputEvent inputEvent) {
		return false;
	}
	
	@Override
	public boolean keyEvent(NiftyInputEvent inputEvent) {
		if (inputEvent == null) {
			return false;
		}
		
		if (this.focus) {
			switch (inputEvent) {
				case NextInputElement:
					this.scrollDown();
					this.selections.selectNext();
					break;
				case PrevInputElement:
					this.scrollUp();
					this.selections.selectPrevious();
					break;
				case Activate:
					MenuSelection current;
					if ((current = this.selections.getCurrentSelection()) != null) { 
						current.activate();
					}
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
	
	/**
	 * Builds the control as a {@link MenuSelection} and returns the built control
	 * 
	 * @param controlBuilder The builder to use to build the control
	 * @return The associated {@link MenuSelection}
	 */
	public MenuSelection add(ControlBuilder controlBuilder) {
		controlBuilder.marginBottom(this.gap.toString());
		Element menuSelection = controlBuilder.build(this.nifty, this.screen, this.childRoot);
		MenuSelection selection = menuSelection.getControl(MenuSelection.class);
		this.selections.add(selection);
		this.selectionElementMap.put(selection, menuSelection);
		this.lastSelection = menuSelection;
		if (this.firstSelection == null) {
			this.firstSelection = menuSelection;
		}
		
		// Show the scroller if the elements exceed the height of the container
		int lastSelectionBottom = this.lastSelection.getHeight() + this.lastSelection.getY();
		Element container = menuSelection.getParent().getParent();
		int containerY = container.getY();
		int containerHeight = container.getHeight();
		if (lastSelectionBottom - containerY > containerHeight) {
			this.scroller.show();
		}
		return selection;
	}
	
	public void clear() {
		for (Element element : this.childRoot.getElements()) {
			element.markForRemoval();
		}
		this.selections.clear();
		this.lastSelection = null;
		this.firstSelection = null;
		this.scroller.hide();
	}
	
	public void setPlaySound(boolean playSound) {
		this.selections.setPlaySound(playSound);
	}
	
	// Used for testing
	protected void setScroller(Element scroller) {
		this.scroller = scroller;
	}
	
	protected boolean shouldScrollDown() {
		if (this.lastSelection == null) {
			return false;
		}
		
		Element container = this.lastSelection.getParent().getParent();
		Element selection = this.selectionElementMap.get(this.selections.getCurrentSelection());
		int containerHeight = container.getHeight();
		int containerY = container.getY();
		int lastSelectionBottom = this.lastSelection.getY() + this.lastSelection.getHeight();
		int selectionBottom = selection.getY() + selection.getHeight();
		
		return this.isPastMiddle(true, selectionBottom, containerY, containerHeight)
				&& lastSelectionBottom - containerY > containerHeight;
	}
	
	protected boolean shouldScrollUp() {
		if (this.firstSelection == null) {
			return false;
		}

		Element container = this.firstSelection.getParent().getParent();
		Element selection = this.selectionElementMap.get(this.selections.getCurrentSelection());
		int containerY = container.getY();
		int selectionY = selection.getY();
		
		return this.isPastMiddle(false, selectionY, containerY, container.getHeight())
				&& containerY > this.firstSelection.getY();
	}
	
	protected boolean isPastMiddle(boolean goingDown, int selectionY, int parentY, int parentHeight) {
		if (goingDown) {
			return selectionY >= (parentY + (parentHeight / 2));
		}
		
		return selectionY <= (parentY + (parentHeight / 2));
	}
	
	protected int getScrollDistance() {
		MenuSelection menuSelection = this.selections.getCurrentSelection();
		if (menuSelection == null) {
			return 0;
		}
		
		Element selection = this.selectionElementMap.get(menuSelection);
		return this.gap.getValueAsInt(selection.getParent().getHeight()) + selection.getHeight();
	}
	
	private void scrollUp() {
		Element selection = this.selectionElementMap.get(this.selections.getCurrentSelection());
		int newPosition;
		
		// Check if we're wrapping
		int lastSelectionBottom = this.lastSelection.getHeight() + this.lastSelection.getY();
		int containerWindowBottom = this.childRoot.getHeight() + this.childRoot.getY();
		if (selection.equals(this.firstSelection) && lastSelectionBottom > containerWindowBottom) {
			newPosition = containerWindowBottom - lastSelectionBottom;
			this.setScroller(1);
			this.layoutElements(newPosition);
		} else if (this.shouldScrollUp()) {
			int distance = this.getScrollDistance();
			
			// Clip to top of first selection if needed
			if (this.firstSelection.getY() + distance >= 0) {
				newPosition = 0;
				this.setScroller(0);
			} else {
				int position = this.childRoot.getConstraintY().getValueAsInt(0);
				newPosition = position + distance;
				// Set the scroller
				float newScrollerPosition = this.childRoot.getParent().getY() -
						this.firstSelection.getY() - distance;
				float scrollableDistance = this.lastSelection.getY() - this.firstSelection.getY() +
						this.lastSelection.getHeight() - this.childRoot.getHeight();
				this.setScroller(newScrollerPosition / scrollableDistance);
			}
			this.layoutElements(newPosition);
		}
	}
	
	private void scrollDown() {
		Element selection = this.selectionElementMap.get(this.selections.getCurrentSelection());
		int newPosition = 0;
		
		// Check if we're wrapping
		if (selection.equals(this.lastSelection)) {
			this.setScroller(0);
			this.layoutElements(newPosition);
		} else if (this.shouldScrollDown()) {
			int distance = this.getScrollDistance();
			
			// Clip to bottom of last selection if needed
			if (this.lastSelection.getY() + this.lastSelection.getHeight() - distance
					<= this.childRoot.getHeight() + this.childRoot.getParent().getY()) {
				newPosition = this.childRoot.getHeight() + this.childRoot.getY()
						- (this.lastSelection.getHeight() + this.lastSelection.getY());
				this.setScroller(1);
			} else {
				int position = this.childRoot.getConstraintY().getValueAsInt(0);
				newPosition = position - distance;
				// Set the scroller
				float newScrollerPosition = this.childRoot.getParent().getY() -
						this.firstSelection.getY() + distance;
				float scrollableDistance = this.lastSelection.getY() - this.firstSelection.getY() +
						this.lastSelection.getHeight() - this.childRoot.getHeight();
				this.setScroller(newScrollerPosition / scrollableDistance);
			}
			this.layoutElements(newPosition);
		}
	}

	private void setScroller(float percentage) {
		int position = (int)Math.floor((this.childRoot.getHeight() - this.scroller.getHeight()) * percentage);
		this.scroller.setConstraintY(SizeValue.px(position));
	}
	
	private void layoutElements(int childRootPosition) {
		this.childRoot.setConstraintY(SizeValue.px(childRootPosition));
		this.childRoot.getParent().layoutElements();
		this.scroller.getParent().layoutElements();
	}
}
