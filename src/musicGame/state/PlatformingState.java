package musicGame.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import musicGame.character.PlayableCharacter;
import musicGame.character.PlayableCharacter.SynchedComboListener;
import musicGame.core.Music;
import musicGame.core.SynchedComboPattern;
import musicGame.gui.ComboSyncher;
import musicGame.gui.PlayArea;
import musicGame.map.LevelMap;

public class PlatformingState extends LevelState implements SynchedComboListener {
	private static final int BPM = 150;
	
	private ComboSyncher comboSyncher;
	private Music music;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		this.music = new Music(getMusicLocation(), BPM);
		this.comboSyncher = new ComboSyncher(container, BPM);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);
		
		if (ready) {
			comboSyncher.update();
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
				break;
		}
	}

	@Override
	public void setUp() throws SlickException {
		super.setUp();
		mapContainer.attachHudChildToFocus(comboSyncher, 0.05f, -0.08f, 0.3f, 0.05f);
		player.addComboListener(this);
		music.stop();
	}
	
	@Override
	public void reset() {
		super.reset();
		music.stop();
	}

	@Override
	public void activateCombo(SynchedComboPattern pattern) {
		comboSyncher.setPattern(pattern);
		comboSyncher.start(music);
	}

	@Override
	protected boolean isOnRails() {
		return false;
	}
	
	@Override
	protected String getMapName() {
		return "snow";
	}
	
	@Override
	protected String getMusicLocation() {
		return "/res/music/Passepied.ogg";
	}

	@Override
	protected PlayArea providePlayArea(GameContainer container, LevelMap map, PlayableCharacter player) {
		return new PlayArea(container, map, player);
	}
}
