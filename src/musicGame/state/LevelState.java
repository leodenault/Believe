package musicGame.state;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import musicGame.character.EnemyCharacter;
import musicGame.character.PlayableCharacter;
import musicGame.core.MapManager;
import musicGame.core.action.PauseGameAction;
import musicGame.gui.PlayArea;
import musicGame.gui.ProgressBar;
import musicGame.map.LevelMap;
import musicGame.physics.PhysicsManager;

public abstract class LevelState extends GameStateBase implements PausableState {
	private GameContainer container;
	private LevelMap map;
	private ProgressBar focusBar;
	private PhysicsManager physics;
	private MapManager mapManager;

	protected boolean ready;
	protected StateBasedGame game;
	protected PlayArea mapContainer;
	protected PlayableCharacter player;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.ready = false;
		this.container = container;
		this.game = game;
		this.focusBar = new ProgressBar(container);
		this.mapManager = MapManager.getInstance();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (ready) {
			mapContainer.render(container, g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (ready) {
			mapContainer.update(delta);
			map.update(delta);
			player.update(delta);
			physics.update(delta);
			focusBar.setProgress(player.getFocus());
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		switch (key) {
			case Input.KEY_ESCAPE:
				new PauseGameAction(this, game).componentActivated(null);
				break;
		}
	}
	
	@Override
	public void reset() {
		map.reset();
		player.setLocation(map.getPlayerStartX(), map.getPlayerStartY() - player.getHeight());
		player.setVerticalSpeed(0);
		initPhysics();
	}

	public void setUp() throws SlickException {
		map = mapManager.getMap(getMapName(), container);
		player = new PlayableCharacter(container, isOnRails(), map.getPlayerStartX(), map.getPlayerStartY());
		mapContainer = providePlayArea(container, map, player);
		focusBar.setText("Focus");
		mapContainer.addHudChild(focusBar, 0.02f, 0.05f, 0.15f, 0.07f);
		initPhysics();
		ready = true;
	}
	
	private void initPhysics() {
		physics = PhysicsManager.getInstance();
		physics.reset();
		physics.addStaticCollidables(map.getCollidableTiles());
		physics.addStaticCollidables(map.getCommands());
		List<EnemyCharacter> enemies = map.getEnemies();
		physics.addCollidables(enemies);
		physics.addCollidable(player);
		physics.addGravityObject(player);
		physics.addGravityObjects(enemies);
	}
	
	protected abstract boolean isOnRails();
	protected abstract String getMapName();
	protected abstract String getMusicLocation();
	protected abstract PlayArea providePlayArea(GameContainer container, LevelMap map, PlayableCharacter player);
}
