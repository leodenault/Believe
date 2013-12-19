package musicGame.menu;

import musicGame.core.GameStateBase;
import musicGame.gui.DirectionalPanel;
import musicGame.gui.MenuSelectionGroup;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;

public abstract class MenuState extends GameStateBase {

	protected MenuSelectionGroup selections;
	protected DirectionalPanel<AbstractComponent> selectionPanel;
	
	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.initGameAndGUI(container, game);
		this.selections = new MenuSelectionGroup();
		this.selectionPanel = new DirectionalPanel<AbstractComponent>(container);
	}
}
