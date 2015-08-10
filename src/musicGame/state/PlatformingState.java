package musicGame.state;

import musicGame.character.Character;
import musicGame.core.SynchedComboPattern;
import musicGame.core.action.PauseGameAction;
import musicGame.gui.ComboSyncher;
import musicGame.map.LevelMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PlatformingState extends GameStateBase implements PausableState {
	private static final int BPM = 160;

	private StateBasedGame game;
	private LevelMap map;
	private Character player;
	private Music music;
	private ComboSyncher combo;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
		this.music = new Music("/res/music/TimeOut_loop.ogg");
		SynchedComboPattern pattern = new SynchedComboPattern();
		pattern.addAction(0, 'a');
		pattern.addAction(1, 'a');
		pattern.addAction(2, 'a');
		pattern.addAction(3, 'a');
		pattern.addAction(4, 'a');
		pattern.addAction(5, 'a');
		combo = new ComboSyncher(container, pattern, BPM, 120, 120);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (map != null) {
			map.render();
			player.render();
		}
		combo.render(container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (map != null) {
			map.update(delta);
		}
		
		if (music.paused()) {
			music.resume();
		} else if (!music.playing()) {
			music.loop();
		}
		
		combo.update();
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (map != null) {
			map.scroll(key);
		}
		
		switch (key) {
			case Input.KEY_ESCAPE:
				music.pause();
				new PauseGameAction(this, game).componentActivated(null);
				break;
			case Input.KEY_LEFT:
			case Input.KEY_RIGHT:
				player.move();
				break;
			case Input.KEY_SPACE:
				combo.start(music);
				break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		if (map != null) {
			map.stopScroll(key);
		}

		if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
			player.stop();
		}
	}
	
	@Override
	public void reset() {
		map.reset();
		music.stop();
	}

	public void setUp() throws SlickException {
		map = new LevelMap("testMap");
		player = new Character(map.getPlayerStartX(), map.getPlayerStartY());
		music.stop();
	}
}
