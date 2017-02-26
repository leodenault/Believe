package musicGame.state;

import org.newdawn.slick.GameContainer;

import musicGame.character.PlayableCharacter;
import musicGame.gui.PlayArea;
import musicGame.map.LevelMap;

public class ArcadeState extends LevelState {
	@Override
	protected boolean isOnRails() {
		return true;
	}
	
	@Override
	protected String getMapName() {
		return "pipeTown";
	}
	
	@Override
	protected String getMusicLocation() {
		return "/res/music/Evasion.ogg";
	}

	@Override
	protected PlayArea providePlayArea(GameContainer container, LevelMap map, PlayableCharacter player) {
		return new PlayArea(container, map, player, 0.8f, 1f);
	}
}
