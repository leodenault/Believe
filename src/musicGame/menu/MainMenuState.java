package musicGame.menu;

import musicGame.core.GameStateBase;
import musicGame.core.action.ChangeStateAction;
import musicGame.core.action.LoadGameAction;
import musicGame.gui.DirectionalPanel;
import musicGame.gui.MenuSelection;
import musicGame.gui.MenuSelectionGroup;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends GameStateBase {

	private DirectionalPanel panel;
	private MenuSelectionGroup selections;
	
	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
			case Input.KEY_DOWN:
				this.selections.selectNext();
				break;
			case Input.KEY_UP:
				this.selections.selectPrevious();
				break;
			case Input.KEY_ENTER:
				this.selections.getCurrentSelection().activate();
				break;
		}
	}

	@Override
	public void init(final GameContainer container, StateBasedGame game)
			throws SlickException {
		MenuSelection playDefaultLevel = new MenuSelection(container, "Play Default Level");
		MenuSelection playCustomLevel = new MenuSelection(container, "Play Custom Level");
		MenuSelection exit = new MenuSelection(container, "Exit");
		panel = new DirectionalPanel(container, 400, 100, 75);
		panel.addChild(playDefaultLevel);
		panel.addChild(playCustomLevel);
		panel.addChild(exit);
		
		playDefaultLevel.addListener(new LoadGameAction("levelFlowFiles/test.lfl", game));
		playCustomLevel.addListener(new ChangeStateAction(FlowFilePickerMenuState.class, game));
		exit.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				container.exit();
			}
		});
		
		this.selections = new MenuSelectionGroup();
		this.selections.add(playDefaultLevel);
		this.selections.add(playCustomLevel);
		this.selections.add(exit);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		panel.render(container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		this.selections.select(0);
	}
}
