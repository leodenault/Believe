package musicGame.menu;

import java.io.File;

import musicGame.core.GameStateBase;
import musicGame.core.Util;
import musicGame.gui.MenuSelection;
import musicGame.gui.VerticalKeyboardScrollpanel;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class FlowFilePickerMenuState extends GameStateBase {

	private MenuSelection back;
	/*private Element scrollPanelElement;
	private Element noFilesMessage;*/
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
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		this.resetUi();
		
		try {
			File[] files = Util.getFlowFiles();
			
			if (files == null || files.length == 0) {
				this.showMessage("Looks like there aren't any flow files to load!");
//				this.back.select();
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
	
	private void showMessage(final String message) {
		/*ControlBuilder builder = new ControlBuilder("textMessage") {{
			parameter("label", message);
		}};
		
		this.noFilesMessage = builder.build(this.getNifty(), screen, this.scrollPanelElement);*/
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		
	}
}
