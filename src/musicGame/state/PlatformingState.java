package musicGame.state;

public class PlatformingState extends LevelState {
	@Override
	protected boolean isOnRails() {
		return false;
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
