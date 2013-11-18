package musicGame.menu;

import java.io.File;
import java.io.FileFilter;

import musicGame.gui.MenuSelection;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class FlowFilePickerMenuState extends MenuState {

	private static final int DEFAULT_HEIGHT = 30;
	private static final String DEFAULT_DIRECTORY = "customFlowFiles";
	private static final String FILE_EXTENSION = ".lfl";
	private File[] files;
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		File parent = new File(DEFAULT_DIRECTORY);
		files = parent.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().toLowerCase().endsWith(FILE_EXTENSION);
			}
		});
		
		for (File file : files) {
			MenuSelection selection =
					new MenuSelection(container, file.getName(), container.getWidth(), DEFAULT_HEIGHT);
			this.selections.add(selection);
			this.selectionPanel.add(selection);
		}
		this.selectionPanel.setSpacing(10);
		this.selections.select(0);
		this.selections.setPlaySound(true);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		this.selectionPanel.render(container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (key == Input.KEY_DOWN) {
			this.selections.selectNext();
		} else if (key == Input.KEY_UP) {
			this.selections.selectPrevious();
		} else if (key == Input.KEY_ENTER) {
			this.selections.getCurrentSelection().activate();
		}
	}

}
