package musicGame.levelFlow.parsing;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import musicGame.levelFlow.Beat;
import musicGame.levelFlow.FlowComponent;
import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.GUIContext;

public class FlowComponentBuilder {
	
	private static final int INVALID_BPM = -1;
	private static final int DEFAULT_OFFSET = 0;
	private static final int DEFAULT_FRAME_WIDTH = 64;
	private static final int DEFAULT_FRAME_HEIGHT = 64;
	private static final int DEFAULT_FRAME_DURATION = 40;

	private GUIContext container;
	private Music song;
	private List<Beat>[] beats;
	private Animation[] beatAnimations;
	private HashMap<Integer, Integer> indexTable;
	private char[] inputKeys;
	private int subdivisions;
	private int laneWidth;
	private int bpm;
	private int offset;
	
	public FlowComponentBuilder(GUIContext container, int laneWidth) {
		this.container = container;
		this.laneWidth = laneWidth;
		this.bpm = INVALID_BPM;
		this.offset = DEFAULT_OFFSET;
	}
	
	public FlowComponentBuilder subdivisionImages(List<String> values)
			throws FlowComponentBuilderException, SlickException {
		if (values.isEmpty()) {
			throw new FlowComponentBuilderException("Expected at least one value for subdivision images, got none");
		}
		this.beatAnimations = new Animation[values.size()];
		this.subdivisions = (int)Math.pow(2, this.beatAnimations.length - 1);
		for (int i = 0; i < this.beatAnimations.length; i++) {
			SpriteSheet sheet = new SpriteSheet(values.get(i), DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
			this.beatAnimations[i] = new Animation(sheet, DEFAULT_FRAME_DURATION);
		}
		return this;
	}
	
	public FlowComponentBuilder song(List<String> values) throws SlickException, FlowComponentBuilderException {
		if (values.size() != 1) {
			throw new FlowComponentBuilderException(
					String.format("Expected one value for song, got %d", values.size()));
		}
		this.song = new Music(values.get(0));
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public FlowComponentBuilder inputKeys(List<String> values) throws FlowComponentBuilderException {
		if (values.isEmpty()) {
			throw new FlowComponentBuilderException("Expected at least one value for keys, got none");
		}
		this.inputKeys = new char[values.size()];
		for (int i = 0; i < this.inputKeys.length; i++) {
			char[] current = values.get(i).toCharArray();
			if (current.length != 1) {
				throw new FlowComponentBuilderException(
						String.format("Key value [%s] is not a character", values.get(i)));
			}
			this.inputKeys[i] = current[0];
		}
		this.beats = new List[this.inputKeys.length];
		for (int i = 0; i < this.beats.length; i++) {
			this.beats[i] = new LinkedList<Beat>();
		}
		return this;
	}
	
	public FlowComponentBuilder tempo(List<String> values)
			throws FlowComponentBuilderException, NumberFormatException {
		if (values.size() != 1) {
			throw new FlowComponentBuilderException(
					String.format("Expected one value for tempo, got %d", values.size()));
		}
		int bpm = Integer.parseInt(values.get(0));
		if (bpm < 1) {
			throw new FlowComponentBuilderException("The tempo must have a value greater than 0");
		}
		this.bpm = bpm;
		return this;
	}
	
	public FlowComponentBuilder offset(List<String> values) throws FlowComponentBuilderException {
		if (values.size() != 1) {
			throw new FlowComponentBuilderException(
					String.format("Expected one value for offset, got %d", values.size()));
		}
		this.offset = Integer.parseInt(values.get(0));
		return this;
	}
	
	public void addBeatLine(String line, int position)
			throws IOException, FlowFileParserException, FlowComponentBuilderException {
		if (this.beats == null) {
			throw new FlowComponentBuilderException("The keys were not set, and therefore beats cannot be added");
		}
		else if (this.indexTable == null) {
			this.buildIndexTable();
		}
		
		this.addBeatLine(line, position, this.indexTable.get(position % this.subdivisions));
	}
	
	public void addBeatLine(String line, int position, int imageIndex) 
			throws FlowComponentBuilderException, IOException, FlowFileParserException {
		if (this.beats == null) {
			throw new FlowComponentBuilderException("The keys were not set, and therefore beats cannot be added");
		}
		
		LineParser parser = new LineParser(new StringReader(line));
		int counter = 0;
		while (counter < this.beats.length && parser.hasNext()) {
			if (parser.next()) {
				this.beats[counter].add(new Beat(this.container, this.beatAnimations[imageIndex], position));
			}
			counter++;
		}
		if (parser.hasNext()) {
			throw new FlowComponentBuilderException("The number of beats in a line cannot be greater than the " +
					"number of keys specified in the configuration");
		}
		else if (counter < this.beats.length) {
			throw new FlowComponentBuilderException("The number of beats in a line cannot be less than the " +
					"number of keys specified in the configuration");
		}
	}
	
	public FlowComponent buildFlowComponent() throws FlowComponentBuilderException {
		List<String> missingFields = this.getMissingFields();
		if (missingFields.size() > 0) {
			this.generateError(missingFields);
		}
		FlowComponent component = new FlowComponent(this.container, this.song, this.inputKeys,
				this.inputKeys.length, this.laneWidth, this.subdivisions, this.bpm, this.offset);
		component.addBeats(this.convertBeats());
		return component;
	}
	
	private Beat[][] convertBeats() {
		Beat[][] converted = new Beat[this.beats.length][];
		for (int i = 0; i < converted.length; i++) {
			converted[i] = this.beats[i].toArray(new Beat[this.beats[i].size()]);
		}
		return converted;
	}
	
	private List<String> getMissingFields() {
		List<String> fields = new LinkedList<String>();
		if (this.beatAnimations == null) {
			fields.add("SubdivisionImages");
		}
		if (this.song == null) {
			fields.add("Song");
		}
		if (this.inputKeys == null || this.inputKeys.length == 0) {
			fields.add("Keys");
		}
		if (this.bpm == INVALID_BPM) {
			fields.add("Tempo");
		}
		return fields;
	}
	
	private void generateError(List<String> missingFields) throws FlowComponentBuilderException {
		StringBuilder builder = new StringBuilder();
		builder.append("The following required configuration values are missing:\n");
		for (String field : missingFields) {
			builder.append(field);
			builder.append("\n");
		}
		throw new FlowComponentBuilderException(builder.toString());
	}
	
	private void buildIndexTable() throws FlowComponentBuilderException {
		this.indexTable = new HashMap<Integer, Integer>();
		for (int i = 0; i < this.subdivisions; i++) {
			this.indexTable.put(i, this.getImageIndex(i));
		}
	}
	
	protected int getImageIndex(int position) throws FlowComponentBuilderException {
		if (this.beatAnimations == null) {
			throw new FlowComponentBuilderException("Tried to add beats, but the subdivision images haven't been defined");
		}
		
		int index = position % this.subdivisions;
		if (index == 0) {
			return 0;
		}
		else if (index % 2 == 1) {
			return this.beatAnimations.length - 1;
		}
		else {
			return this.recursiveGetImageIndex(index, this.subdivisions / 2, this.subdivisions / 2, 1);
		}
	}

	protected int recursiveGetImageIndex(int beatIndex, int currentIndex, int step, int imageIndex) {
		if (beatIndex == currentIndex) {
			return imageIndex;
		}
		else if (beatIndex > currentIndex) {
			return this.recursiveGetImageIndex(beatIndex, currentIndex + step, step / 2, imageIndex + 1);
		}
		else {
			return this.recursiveGetImageIndex(beatIndex, currentIndex - step, step / 2, imageIndex + 1);
		}
	}
}
