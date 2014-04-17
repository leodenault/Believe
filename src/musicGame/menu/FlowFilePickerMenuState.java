package musicGame.menu;

import java.io.File;
import java.io.IOException;

import musicGame.core.Util;
import musicGame.core.action.ChangeStateAction;
import musicGame.core.action.LoadGameAction;
import musicGame.gui.MenuSelection;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.layout.manager.CenterLayout;
import de.lessvoid.nifty.layout.manager.VerticalLayout;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class FlowFilePickerMenuState extends MenuState implements ScreenController {

	private static final String SCREEN_ID = "FlowFilePickerMenuState";

	private MenuSelection back;
	private Element contentPanel;
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		switch (key) {
			case Input.KEY_DOWN:
				if (!this.back.isSelected()) {
					this.selections.selectNext();
				}
				break;
			case Input.KEY_UP:
				if (!this.back.isSelected()) {
					this.selections.selectPrevious();
				}
				break;
			case Input.KEY_LEFT:
			case Input.KEY_RIGHT:
				MenuSelection selection;
				if ((selection = this.selections.getCurrentSelection()) != null) {
					if (this.back.isSelected()) {
						selection.select();
						this.back.deselect();
					} else {
						selection.deselect();
						this.back.select();
					}
				}
				break;
			case Input.KEY_ENTER:
				if (this.back.isSelected()) {
					this.back.activate();
				} else {
					this.selections.getCurrentSelection().activate();
				}
				break;
		}
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("musicGame/menu/FlowFilePickerMenuState.xml", SCREEN_ID);
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.initGameAndGUI(container, game);
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enterState(container, game);
		// TODO: Enable scrolling
		// TODO: Take care of really long file names.
		Screen screen = this.getNifty().getScreen(SCREEN_ID);
		resetUi(screen);
		
		try {
			File[] files = Util.getFlowFiles();
			
			if (files == null || files.length == 0) {
				this.showMessage("Looks like there aren't any flow files to load!", screen);
			} else {
				for (final File file : files) {
					final String name = file.getName().substring(0, file.getName().lastIndexOf("."));

					ControlBuilder builder = new ControlBuilder(name, "menuSelection") {{
						parameter("label", name);
						marginBottom("10px");
						style("menuSelectionFlowFile-border");
					}};

					this.contentPanel.setLayoutManager(new VerticalLayout());
					MenuSelection selection = builder.build(this.getNifty(), screen, this.contentPanel)
							.getControl(MenuSelection.class);
					this.selections.add(selection);
					selection.setMenuAction(new LoadGameAction(file.getCanonicalPath(), game));
					selection.setStyle(MenuSelection.Style.BORDER, "menuSelectionFlowFile-border");
					selection.setActiveStyle(MenuSelection.Style.BORDER, "menuSelectionFlowFile-active-border");
				}
			}
		} catch (SecurityException | IOException e) {
			this.showMessage("Something went wrong when trying to find the flow files!", screen);
		}
		
		if (!this.selections.getSelections().isEmpty()) {
			this.selections.select(0);
		} else {
			this.back.select();
		}
		this.selections.setPlaySound(true);
		this.back.setMenuAction(new ChangeStateAction(MainMenuState.class, game));
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		
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
	
	private void resetUi(Screen screen) {
		this.contentPanel = screen.findElementByName("contentPanel");
		this.back = screen.findControl("back", MenuSelection.class);
		this.back.setPlaySound(true);
		
		this.back.deselect();
		this.selections.clear();
		for (Element element : contentPanel.getElements()) {
			element.markForRemoval();
		}
	}
	
	private void showMessage(final String message, Screen screen) {
		ControlBuilder builder = new ControlBuilder("textMessage") {{
			parameter("label", message);
		}};
		
		this.contentPanel.setLayoutManager(new CenterLayout());
		builder.build(this.getNifty(), screen, this.contentPanel);
	}
}
