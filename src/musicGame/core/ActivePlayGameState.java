package musicGame.core;

import musicGame.core.PlayGameState.SubPlayGameState;
import musicGame.core.action.ChangeStateAction;
import musicGame.levelFlow.FlowComponent;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ActivePlayGameState extends HierarchicalGameState implements SubPlayGameState {
	
	private FlowComponent component;
	private ChangeStateAction pauseAction;
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (key == Input.KEY_ESCAPE) {
			if (this.component != null) {
				if (this.component.isPlaying()) {
					this.component.pause();
					this.pauseAction.performAction();
				} else if (this.component.isPaused()) {
					this.component.play();
				}
			}
		}
	}

	@Override
	public void renderGame(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		this.component.render(container, g);
	}

	@Override
	public void updateGame(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		this.component.update();
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.initGameAndGUI(container, game);
		this.pauseAction = new ChangeStateAction(PausedPlayGameState.class, game);
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enterState(container, game);
		updateScreen();
		this.component.play();
	}

	@Override
	public void setFlowComponent(FlowComponent component) {
		this.component = component;
	}
}
