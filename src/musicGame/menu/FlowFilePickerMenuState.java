package musicGame.menu;

import java.io.File;

import musicGame.core.GameStateBase;
import musicGame.core.Util;
import musicGame.core.action.ChangeStateAction;
import musicGame.gui.MenuSelection;
import musicGame.gui.TextComponent;
import musicGame.gui.VerticalKeyboardScrollpanel;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class FlowFilePickerMenuState extends GameStateBase {

	private MenuSelection back;
//	private Element scrollPanelElement;
	private TextComponent noFilesMessage;
	private VerticalKeyboardScrollpanel scrollPanel;
	
	public FlowFilePickerMenuState(String niftyXmlFile) {
		super(niftyXmlFile);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		switch (key) {
			case Input.KEY_LEFT:
			case Input.KEY_RIGHT:
				/*if (!this.scrollPanel.isEmpty()) {
					if (this.back.isSelected()) {
						this.scrollPanel.onFocus(true);
						this.back.deselect();
					} else {
						this.scrollPanel.onFocus(false);
						this.back.select();
					}
				}*/
				break;
			case Input.KEY_ENTER:
				if (this.back.isSelected()) {
					this.back.activate();
				}
				break;
		}
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		back = new MenuSelection(container, 10, 10, 200, 50, "Back");
		back.addListener(new ChangeStateAction(MainMenuState.class, game));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		this.resetUi();
		
		try {
			File[] files = Util.getFlowFiles();
			
			if (files == null || files.length == 0) {
				this.showMessage(container, "Looks like there aren't any flow files to load!");
				if (!this.back.isSelected()) {
					this.back.toggleSelect();
				}
			} else {
				/*if (this.noFilesMessage != null) {
					this.noFilesMessage.markForRemoval();
				}*/
				
				for (File file : files) {
					final String name = file.getName().substring(0, file.getName().lastIndexOf("."));

					/*ControlBuilder builder = new ControlBuilder(name, "menuSelection") {{
						parameter("label", name);
						style("menuSelectionFlowFile-border");
					}};*/

					/*MenuSelection selection = this.scrollPanel.add(builder);
					selection.setMenuAction(new LoadGameAction(file.getCanonicalPath(), game));
					selection.setStyle(MenuSelection.Style.BORDER, "menuSelectionFlowFile-border");
					selection.setActiveStyle(MenuSelection.Style.BORDER, "menuSelectionFlowFile-active-border");*/
					//this.scrollPanel.onFocus(true);
				}
			}
		} catch (SecurityException/* | IOException */e) {
			//this.showMessage("Something went wrong when trying to find the flow files!", screen);
		}
//		this.back.setMenuAction(new ChangeStateAction(MainMenuState.class, game));
		//this.scrollPanel.setPlaySound(true);
	}

	private void resetUi() {
		/*this.scrollPanel = screen.findControl("contentPanel", VerticalKeyboardScrollpanel.class);
		this.scrollPanel.onFocus(true);
		this.scrollPanelElement = screen.findElementByName("scrollPanel");
		this.back = screen.findControl("back", MenuSelection.class);*/
//		this.back.setPlaySound(true);
//		
//		this.back.deselect();
		//this.scrollPanel.clear();
	}
	
	private void showMessage(GameContainer container, String message) {
		this.noFilesMessage = new TextComponent(container, 260, 10, 500, 100, message);
	}

	@Override
	public void render(GameContainer context, StateBasedGame game, Graphics g)
			throws SlickException {
		back.render(context, g);
		this.noFilesMessage.render(context, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
	}
}
