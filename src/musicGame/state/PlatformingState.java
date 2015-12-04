package musicGame.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import musicGame.character.PlayableCharacter;
import musicGame.core.MapManager;
import musicGame.core.Music;
import musicGame.core.PhysicsManager;
import musicGame.core.action.PauseGameAction;
import musicGame.gui.PlayArea;
import musicGame.map.LevelMap;

public class PlatformingState extends GameStateBase implements PausableState {
	private static final int BPM = 150;

	private boolean ready;
	private GameContainer container;
	private StateBasedGame game;
	private LevelMap map;
	private PlayArea mapContainer;
	private PlayableCharacter player;
	private Music music;
	private PhysicsManager physics;
	private MapManager mapManager;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.ready = false;
		this.container = container;
		this.game = game;
		this.music = new Music("/res/music/Evasion.ogg", BPM);
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
			
			if (music.paused()) {
				music.resume();
			} else if (!music.playing()) {
				music.loop();
			}
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		switch (key) {
			case Input.KEY_ESCAPE:
				music.pause();
				new PauseGameAction(this, game).componentActivated(null);
				break;
		}
	}
	
	@Override
	public void reset() {
		map.reset();
		music.stop();
		player.setLocation(map.getPlayerStartX(), map.getPlayerStartY() - player.getHeight());
		player.setVerticalSpeed(0);
		initPhysics();
	}

	public void setUp() throws SlickException {
		map = mapManager.getMap("pipeTown", container);
		player = new PlayableCharacter(container, music, map.getPlayerStartX(), map.getPlayerStartY());
		mapContainer = new PlayArea(container, map, player);
		initPhysics();
		music.stop();
		ready = true;
	}
	
	private void initPhysics() {
		physics = new PhysicsManager();
		physics.addStaticCollidables(map.getCollidableTiles());
		physics.addDynamicCollidables(map.getEnemies());
		physics.addDynamicCollidable(player);
	}
}
