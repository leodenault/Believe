package musicGame.menu;

import musicGame.GUI.DirectionalPanel;
import musicGame.GUI.MenuSelection;
import musicGame.GUI.MenuSelectionGroup;
import musicGame.core.GameStateBase;

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
