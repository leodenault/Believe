package musicGame.levelFlow.parsing;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import musicGame.levelFlow.Beat;
import musicGame.levelFlow.FlowComponent;
import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class FlowComponentBuilder {
	
	private static final int INVALID_BPM = -1;
	private static final int DEFAULT_OFFSET = 0;
	private static final int DEFAULT_SUBDIVISIONS = 4;

	private GUIContext container;
	private Image topBarImage;
	private Music song;
	private List<Beat>[] beats;
	private char[] inputKeys;
	private int laneWidth;
	private int bpm;
	private int offset;
	private int subdivisions;
	
	public FlowComponentBuilder(GUIContext container, int laneWidth) {
		this.container = container;
		this.laneWidth = laneWidth;
		this.bpm = INVALID_BPM;
		this.offset = DEFAULT_OFFSET;
		this.subdivisions = DEFAULT_SUBDIVISIONS;
	}
	
	public FlowComponentBuilder topBarImage(List<String> values)
			throws SlickException, FlowComponentBuilderException {
		if (values.size() != 1) {
			throw new FlowComponentBuilderException(
					String.format("Expected one value for top bar image, got %d", values.size()));
		}
		this.topBarImage = new Image(values.get(0));
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
	
	public void addBeatLine(String line, Animation animation, int position)
			throws IOException, FlowFileParserException, FlowComponentBuilderException {
		if (this.beats == null) {
			throw new FlowComponentBuilderException("The keys were not set, and therefore beats cannot be added");
		}
		
		LineParser parser = new LineParser(new StringReader(line));
		int counter = 0;
		while (counter < this.beats.length && parser.hasNext()) {
			if (parser.next()) {
				this.beats[counter].add(new Beat(this.container, animation, position));
			}
			counter++;
		}
		if (parser.hasNext()) {
			throw new FlowComponentBuilderException("The number of beats in a line cannot be less than the" +
					"number of keys specified in the configuration");
		}
		else if (counter < this.beats.length) {
			throw new FlowComponentBuilderException("The number of beats in a line cannot be greater than the" +
					"number of keys specified in the configuration");
		}
	}
	
	public FlowComponent buildFlowComponent() throws FlowComponentBuilderException {
		List<String> missingFields = this.getMissingFields();
		if (missingFields.size() > 0) {
			this.generateError(missingFields);
		}
		FlowComponent component = new FlowComponent(this.container, this.topBarImage, this.song, this.inputKeys,
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
		if (this.topBarImage == null) {
			fields.add("TopBarImage");
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
}
