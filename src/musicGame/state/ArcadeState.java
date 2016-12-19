package musicGame.state;

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
}
