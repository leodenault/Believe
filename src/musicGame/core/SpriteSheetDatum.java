package musicGame.core;

public class SpriteSheetDatum {
	public int frameWidth;
	public int frameHeight;
	public int frameLength;
	public String name;
	public String sheet;
	
	public SpriteSheetDatum() {}

	public SpriteSheetDatum(String name, String sheet, int frameWidth,
			int frameHeight, int frameLength) {
		this.name = name;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameLength = frameLength;
		this.sheet = sheet;
	}
}
