package musicGame.state;

import musicGame.core.action.ChangeStateAction;
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

public class GamePausedOverlay extends GameStateBase {
	
	private MenuSelectionGroup selections;
	private DirectionalPanel panel;
	private StateBasedGame game;
	private PausableState pausedState;
	private MenuSelection resume;
	private MenuSelection restart;
	private ChangeStateAction resumeAction;
	private ComponentListener restartAction;

	@Override
	public void init(GameContainer container, final StateBasedGame game)
			throws SlickException {
		this.game = game;
		panel = new DirectionalPanel(container, container.getWidth() / 2, (container.getHeight() - 200) / 3, 50);
		resume = new MenuSelection(container, "Resume");
		restart = new MenuSelection(container, "Restart");
		MenuSelection exitLevel = new MenuSelection(container, "Exit Level");
		
		exitLevel.addListener(new ChangeStateAction(MainMenuState.class, game));
		
		panel.addChild(resume);
		panel.addChild(restart);
		panel.addChild(exitLevel);
		
		this.selections = new MenuSelectionGroup();
		this.selections.add(resume);
		this.selections.add(restart);
		this.selections.add(exitLevel);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
			case Input.KEY_ENTER:
				this.selections.getCurrentSelection().activate();
				break;
			case Input.KEY_DOWN:
				this.selections.selectNext();
				break;
			case Input.KEY_UP:
				this.selections.selectPrevious();
				break;
			case Input.KEY_ESCAPE:
				new ChangeStateAction(PlayFlowFileState.class, game, 500).componentActivated(null);
				break;
		}
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
	public void enter(GameContainer container, final StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		this.selections.select(0);
		
		resumeAction = new ChangeStateAction(pausedState.getClass(), game, 500);
		restartAction = new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				pausedState.reset();
				new ChangeStateAction(pausedState.getClass(), game).componentActivated(null);
			}
		};
		resume.addListener(resumeAction);
		restart.addListener(restartAction);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);
		resume.removeListener(resumeAction);
		restart.removeListener(restartAction);
	}

	public void setPausedState(PausableState state) {
		this.pausedState = state;
	}
}
