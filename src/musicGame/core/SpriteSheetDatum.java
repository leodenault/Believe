package musicGame.core;

import java.util.LinkedList;
import java.util.List;

public class SpriteSheetDatum {
	public static class FrameSequence {
		public int start;
		public int end;
		public String name;
		
		public FrameSequence() {}
		public FrameSequence(String name, int start, int end) {
			this.name = name;
			this.start = start;
			this.end = end;
		}
	}
	
	public int frameWidth;
	public int frameHeight;
	public int frameLength;
	public String name;
	public String sheet;
	public List<FrameSequence> sequences;
	
	public SpriteSheetDatum() {
		this(null, null, 0, 0, 0);
	}

	public SpriteSheetDatum(String name, String sheet, int frameWidth,
			int frameHeight, int frameLength) {
		this.name = name;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameLength = frameLength;
		this.sheet = sheet;
		this.sequences = new LinkedList<FrameSequence>();
	}
	
	public void addSequence(FrameSequence sequence) {
		sequences.add(sequence);
	}
	
	public List<FrameSequence> getSequences() {
		return sequences;
	}
}
