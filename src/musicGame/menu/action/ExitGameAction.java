package musicGame.menu.action;

import org.newdawn.slick.GameContainer;

public class ExitGameAction implements MenuAction {

	private GameContainer container;
	
	public ExitGameAction(GameContainer container) {
		this.container = container;
	}
	
	@Override
	public void performAction() {
		container.exit();
	}
}
