package musicGame.menu;

import musicGame.core.GameStateBase;
import musicGame.gui.DirectionalPanel;
import musicGame.gui.MenuSelection;
import musicGame.gui.MenuSelectionGroup;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public abstract class MenuState extends GameStateBase {

	protected MenuSelectionGroup selections;
	protected DirectionalPanel<MenuSelection> selectionPanel;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.selections = new MenuSelectionGroup();
		this.selectionPanel = new DirectionalPanel<MenuSelection>(container);
	}
}
