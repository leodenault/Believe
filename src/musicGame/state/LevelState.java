package musicGame.state;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import musicGame.character.EnemyCharacter;
import musicGame.character.PlayableCharacter;
import musicGame.character.PlayableCharacter.SynchedComboListener;
import musicGame.core.MapManager;
import musicGame.core.Music;
import musicGame.core.SynchedComboPattern;
import musicGame.core.action.PauseGameAction;
import musicGame.gui.ComboSyncher;
import musicGame.gui.PlayArea;
import musicGame.gui.ProgressBar;
import musicGame.map.LevelMap;
import musicGame.physics.PhysicsManager;

public abstract class LevelState extends GameStateBase implements PausableState, SynchedComboListener {
	protected static final int BPM = 150;

	private boolean ready;
	private GameContainer container;
	private StateBasedGame game;
	private LevelMap map;
	private PlayArea mapContainer;
	private PlayableCharacter player;
	private ProgressBar focusBar;
	private PhysicsManager physics;
	private MapManager mapManager;
	private ComboSyncher comboSyncher;

	protected Music music;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.ready = false;
		this.container = container;
		this.game = game;
		this.music = new Music(getMusicLocation(), BPM);
		this.comboSyncher = new ComboSyncher(container, music.getBpm());
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
			comboSyncher.update();
			focusBar.setProgress(player.getFocus());
			
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
		map = mapManager.getMap(getMapName(), container);
		player = new PlayableCharacter(
				container, music, isOnRails(), map.getPlayerStartX(), map.getPlayerStartY());
		player.addComboListener(this);
		mapContainer = new PlayArea(container, map, player);
		focusBar.setText("Focus");
		mapContainer.addHudChild(focusBar, 0.02f, 0.05f, 0.15f, 0.07f);
		mapContainer.attachHudChildToFocus(comboSyncher, 0.05f, -0.08f, 0.3f, 0.05f);
		initPhysics();
		music.stop();
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

	@Override
	public void activateCombo(SynchedComboPattern pattern) {
		comboSyncher.setPattern(pattern);
		comboSyncher.start(music);
	}
	
	protected abstract boolean isOnRails();
	protected abstract String getMapName();
	protected abstract String getMusicLocation();
}