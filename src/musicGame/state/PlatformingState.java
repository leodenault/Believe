package musicGame.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import musicGame.character.PlayableCharacter;
import musicGame.character.PlayableCharacter.SynchedComboListener;
import musicGame.core.SynchedComboPattern;
import musicGame.gui.ComboSyncher;
import musicGame.gui.PlayArea;
import musicGame.map.LevelMap;

public class PlatformingState extends LevelState implements SynchedComboListener {
	private ComboSyncher comboSyncher;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		this.comboSyncher = new ComboSyncher(container, music.getBpm());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);
		
		if (ready) {
			comboSyncher.update();	
		}
	}

	@Override
	public void setUp() throws SlickException {
		super.setUp();
		mapContainer.attachHudChildToFocus(comboSyncher, 0.05f, -0.08f, 0.3f, 0.05f);
		player.addComboListener(this);
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
