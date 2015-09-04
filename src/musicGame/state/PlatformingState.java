package musicGame.state;

import musicGame.character.Character;
import musicGame.core.PhysicsManager;
import musicGame.core.SynchedComboPattern;
import musicGame.core.action.PauseGameAction;
import musicGame.gui.ComboSyncher;
import musicGame.gui.PlayArea;
import musicGame.map.LevelMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PlatformingState extends GameStateBase implements PausableState {
	private static final int BPM = 160;

	private GameContainer container;
	private StateBasedGame game;
	private LevelMap map;
	private PlayArea mapContainer;
	private Character player;
	private Music music;
	private ComboSyncher combo;
	private PhysicsManager physics;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.container = container;
		this.game = game;
		this.music = new Music("/res/music/TimeOut_loop.ogg");
		SynchedComboPattern pattern = new SynchedComboPattern();
		pattern.addAction(0, 's');
		pattern.addAction(1, 's');
		pattern.addAction(2, 's');
		pattern.addAction(3, 's');
		pattern.addAction(4, 'a');
		pattern.addAction(4.5f, 'd');
		pattern.addAction(5, 'a');
		pattern.addAction(6, 'a');
		pattern.addAction(6.5f, 'd');
		pattern.addAction(7, 'a');
		combo = new ComboSyncher(container, pattern, BPM, 120, 120);
		physics = new PhysicsManager();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (mapContainer != null) {
			mapContainer.render(container, g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (mapContainer != null) {
			mapContainer.update(delta);
		}
		
		if (player != null) {
			player.update(delta);
		}
		
		if (music.paused()) {
			music.resume();
		} else if (!music.playing()) {
			music.loop();
		}
		
		combo.update();
		physics.update(delta);
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		switch (key) {
			case Input.KEY_ESCAPE:
				music.pause();
				new PauseGameAction(this, game).componentActivated(null);
				break;
			case Input.KEY_SPACE:
				combo.start(music);
				break;
		}
	}
	
	@Override
	public void reset() {
		map.reset();
		music.stop();
	}

	public void setUp() throws SlickException {
		map = new LevelMap(container, "testMap");
		player = new Character(container, map.getPlayerStartX(), map.getPlayerStartY());
		mapContainer = new PlayArea(container, map);
		mapContainer.addChild(map);
		mapContainer.addChild(player);
		mapContainer.addChild(combo);
		physics.addStaticCollidables(map.getCollidableTiles());
		physics.addDynamicCollidable(player);
		music.stop();
	}
}
