package musicGame.levelFlow.parsing;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.GUIContext;

import musicGame.levelFlow.component.Beat;
import musicGame.levelFlow.component.FlowComponent;
import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

public class FlowComponentBuilder {

  private static final int INVALID_BPM = -1;
  private static final int DEFAULT_OFFSET = 0;
  private static final int DEFAULT_FRAME_WIDTH = 64;
  private static final int DEFAULT_FRAME_HEIGHT = 64;
  private static final int DEFAULT_FRAME_DURATION = 40;

  private GUIContext container;
  private String song;
  private List<BeatData>[] beats;
  private List<String> subdivisionImageFiles;
  private HashMap<Integer, Integer> indexTable;
  private char[] inputKeys;
  private int subdivisions;
  private int laneWidth;
  private int bpm;
  private int offset;

  public class BeatData {
    private int imageIndex;
    private int position;

    public BeatData(int imageIndex, int position) {
      this.imageIndex = imageIndex;
      this.position = position;
    }

    public int getPosition() {
      return position;
    }

    public Beat convertToBeat(Animation[] beatAnimations) {
      return new Beat(container, beatAnimations[imageIndex], position);
    }
  }

  public FlowComponentBuilder(GUIContext container, int laneWidth) {
    this.container = container;
    this.laneWidth = laneWidth;
    this.bpm = INVALID_BPM;
    this.offset = DEFAULT_OFFSET;
  }

  public FlowComponentBuilder subdivisionImages(List<String> values)
      throws FlowComponentBuilderException {
    if (values.isEmpty()) {
      throw new FlowComponentBuilderException("Expected at least one value for subdivision images, got none");
    }
    this.subdivisions = (int)Math.pow(2, values.size() - 1);
    this.subdivisionImageFiles = values;
    return this;
  }

  public FlowComponentBuilder song(List<String> values) throws FlowComponentBuilderException {
    if (values.size() != 1) {
      throw new FlowComponentBuilderException(
          String.format("Expected one value for song, got %d", values.size()));
    }
    this.song = values.get(0);
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
      this.beats[i] = new LinkedList<BeatData>();
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
        this.beats[counter].add(new BeatData(imageIndex, position));
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

  public FlowComponent buildFlowComponent() throws FlowComponentBuilderException, SlickException {
    List<String> missingFields = this.getMissingFields();
    if (missingFields.size() > 0) {
      this.generateError(missingFields);
    }
    FlowComponent component = new FlowComponent(this.container, new Music(this.song), this.inputKeys,
        this.inputKeys.length, this.laneWidth, this.subdivisions, this.bpm, this.offset);
    component.addBeats(this.convertBeats());
    return component;
  }

  public int getBpm() {
    return bpm;
  }

  public int getOffset() {
    return offset;
  }

  public int getSubdivisions() {
    return subdivisions;
  }

  public char[] getInputKeys() {
    return inputKeys;
  }

  public List<BeatData>[] getBeats() {
    return beats;
  }

  private Beat[][] convertBeats() throws SlickException {
    Animation[] beatAnimations = fetchBeatAnimations();
    Beat[][] converted = new Beat[beats.length][];
    for (int i = 0; i < converted.length; i++) {
      converted[i] = new Beat[beats[i].size()];
      for (int j = 0; j < converted[i].length; j++) {
        converted[i][j] = beats[i].get(j).convertToBeat(beatAnimations);
      }
    }
    return converted;
  }

  private List<String> getMissingFields() {
    List<String> fields = new LinkedList<String>();
    if (this.subdivisionImageFiles == null) {
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

  private Animation[] fetchBeatAnimations() throws SlickException {
    Animation[] beatAnimations = new Animation[subdivisionImageFiles.size()];
    for (int i = 0; i < beatAnimations.length; i++) {
      SpriteSheet sheet = new SpriteSheet(subdivisionImageFiles.get(i), DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
      beatAnimations[i] = new Animation(sheet, DEFAULT_FRAME_DURATION);
    }
    return beatAnimations;
  }

  protected int getImageIndex(int position) throws FlowComponentBuilderException {
    if (this.subdivisionImageFiles == null) {
      throw new FlowComponentBuilderException("Tried to add beats, but the subdivision images haven't been defined");
    }

    int index = position % this.subdivisions;
    if (index == 0) {
      return 0;
    }
    else if (index % 2 == 1) {
      return this.subdivisionImageFiles.size() - 1;
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
