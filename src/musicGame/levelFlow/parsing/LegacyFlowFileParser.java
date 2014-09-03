package musicGame.levelFlow.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import musicGame.gui.GraphicsUtils;
import musicGame.levelFlow.Beat;
import musicGame.levelFlow.FlowComponent;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

/**
 * An object that parses LevelFlow files.
 */
public class LegacyFlowFileParser {

	private final String commentRegex = "//.*";
	private final Image defaultTopBarImage;
	private final Animation[] defaultSubImages;
	private final char[] defaultInputKeys = {'a', 's', 'k', 'l'};
	private final int[] defaultSubImageParams = {64, 64, 40};

	private Pattern comment;
	private GameContainer container;
	private Image topBarImage;
	private Animation[] subImages;
	private Music song;
	private char[] inputKeys;
	private double bpm;
	private double offset;
	private int[] subImageParams;

	private String currentLine;
	private Queue<Beat>[] beats;

	/**
	 * Creates a new FlowFileParser.
	 * 
	 * @param container		The context in which this parser is created and rendered.
	 * @param reader		The reader of the flow file to parse.
	 * @throws IOException	Thrown if an error occurs while reading the file.
	 */
	public LegacyFlowFileParser(GameContainer container, BufferedReader reader) throws IOException, SlickException {
		int[] alphaColours = {0xFFFF00FF};
		Image[] loadedSubImages = new Image[3];
		this.defaultTopBarImage = new Image("res/graphics/sprites/testScoreBar.png");
		loadedSubImages[0] = new Image("res/graphics/sprites/qNoteSheet.png");
		loadedSubImages[1] = new Image("res/graphics/sprites/eNoteSheet.png");
		loadedSubImages[2] = new Image("res/graphics/sprites/sNoteSheet.png");
		this.defaultSubImages = GraphicsUtils.makeAnimationsWithAlpha(loadedSubImages, alphaColours, defaultSubImageParams[0], defaultSubImageParams[1], defaultSubImageParams[2]); //TODO: Perhaps an xml file or something would start being useful.
		this.comment = Pattern.compile(this.commentRegex);
		this.bpm = -1;
		this.subImageParams = new int[3];
		
		for (int i = 0; i < this.defaultSubImageParams.length; i++) {
			this.subImageParams[i] = this.defaultSubImageParams[i];
		}
		
		this.container = container;
		this.readFile(reader);
	}

	/**
	 * The top bar image specified by the LevelFlow file.
	 */
	public Image getTopBarImage() {
		return this.topBarImage;
	}

	/**
	 * The song specified by the LevelFlow file.
	 */
	public Music getSong() {
		return this.song;
	}

	/**
	 * The keys to use specified by the LevelFlow file.
	 */
	public char[] getInputKeys() {
		return this.inputKeys;
	}

	/**
	 * The number of lanes specified by the LevelFlow file.
	 */
	public int getNumLanes() {
		return this.inputKeys.length;
	}

	/**
	 * The subdivision on the beats specified by the LevelFlow file.
	 */
	public int getSubdivision() {
		return (int)Math.pow(2, this.subImages.length - 1);
	}

	/**
	 * The tempo specified by the LevelFlow file.
	 */
	public double getBpm() {
		return this.bpm;
	}

	/**
	 * The offset specified by the LevelFlow file.
	 */
	public double getOffset() {
		return this.offset;
	}

	/**
	 * The beats specified by the LevelFlow file.
	 */
	public Beat[][] getBeats() {
		if (this.beats == null) {
			return null;
		}
		else {
			Beat[][] result = new Beat[this.beats.length][];
			Beat[] current;
			Iterator<Beat> it;
			int index;

			for (int i = 0; i < this.beats.length; i++) {
				current = new Beat[this.beats[i].size()];
				it = this.beats[i].iterator();
				index = 0;

				while (it.hasNext()) {
					current[index] = it.next();
					index++;
				}

				result[i] = current;
			}

			return result;
		}
	}

	/**
	 * Creates a FlowComponent based on the attributes loaded by this parser.
	 * 
	 * @param addBeats	Whether to add the beats after creating the component or not.
	 * @param x			The x position of the FlowComponent.
	 * @param y			The y position of the FlowComponent.
	 * @param width		The width of the FlowComponent.
	 * @param height	The height of the FlowComponent.
	 * @return			A new FlowComponent with the attributes loaded by this parser.
	 */
	public FlowComponent createFlowComponent(boolean addBeats, int laneWidth, int x, int y, int width, int height) {
		FlowComponent component = new FlowComponent(container, this.getTopBarImage(), this.getSong(), this.getInputKeys(), this.getNumLanes(), laneWidth, this.getSubdivision(), this.getBpm(), this.getOffset(), x, y, width, height);

		if (addBeats) {
			component.addBeats(this.getBeats());
		}

		return component;
	}

	@SuppressWarnings("unchecked")
	private void readFile(BufferedReader reader) throws IOException {
		try {
			if (reader == null) {
				throw new IllegalArgumentException("The flow file reader cannot be null.");
			}
			this.parseConfig(reader); // Parse the configurations.

			// If there is a description of the beats, then load them.
			if (this.currentLine != null && this.currentLine.trim().toUpperCase().equals("BEGIN")) {
				this.beats = new Queue[this.inputKeys.length];

				for (int i = 0; i < this.beats.length; i++) {
					this.beats[i] = new LinkedList<Beat>();
				}
				this.parseFlow(reader);
			}
			else {
				reader.close();
				return;
			}

			if (!this.currentLine.equals("END")) {
				reader.close();
				throw new IOException("Could not find end of flow.");
			}
			reader.close();
		}
		catch (Exception e) {
			if (reader != null) {
				reader.close();
			}
			throw new IOException("Error while reading FlowFile: " + e.getMessage());
		}
	}

	private void parseConfig(BufferedReader br) throws IOException, SlickException {
		String[] args;
		int[] transparentColours = new int[1];
		transparentColours[0] = 0xFFFF00FF;

		// Keep reading until either the end of the file is found or all fields are filled.
		while ((this.currentLine = br.readLine()) != null && !this.currentLine.trim().toUpperCase().equals("BEGIN")) {
			args = this.currentLine.split("=");

			if (args.length == 2 && !this.comment.matcher(this.currentLine).matches()) {
				args[0] = args[0].trim().toUpperCase();
				args[1] = args[1].trim();

				if (args[0].equals("TOPBARIMAGE")) {
					this.topBarImage = GraphicsUtils.makeImageWithAlpha(new Image(args[1]), transparentColours);
				}
				else if (args[0].equals("SONG")) {
					this.song = new Music(args[1]);
				}
				else if (args[0].equals("KEYS")) {
					String[] keys = args[1].split(",");
					this.inputKeys = new char[keys.length];
					for (int i = 0; i < keys.length; i++) {
						if (keys[i].length() > 1) {
							throw new IOException("Only one character may be specified for a key.");
						}
						if (keys.length == 0) {
							throw new IOException("A specification of a key is missing.");
						}
						// TODO: Do we really want this? Having to switch using CapsLock could be an interesting feature... Or annoyance.
						this.inputKeys[i] = Character.toLowerCase(keys[i].charAt(0)); // Get each key.
					}
				}
				else if (args[0].equals("TEMPO")) {
					this.bpm = Double.parseDouble(args[1]);
				}
				else if (args[0].equals("OFFSET")) {
					this.offset = Double.parseDouble(args[1]);
				}
				else if (args[0].equals("SUBDIVISIONIMAGES")) {
					String[] params = args[1].split(",");
					int numParams = 0;
					
					// Check the parameters that should be at the beginning. As long as they're numbers,
					// they don't pass the length of the number of possible parameters, and they don't 
					// pass the number of arguments detected, they're parameters.
					while (numParams < this.subImageParams.length && numParams < params.length) {
						if (!Pattern.matches("[0-9]+", params[numParams].trim())) {
							break;
						}
						this.subImageParams[numParams] = Integer.parseInt(params[numParams].trim());
						numParams++;
					}
					
					this.subImages = new Animation[params.length - numParams];
					for (int i = 0; i < this.subImages.length; i++) {
						this.subImages[i] = GraphicsUtils.makeAnimationWithAlpha(new Image(params[i + numParams].trim()), transparentColours, this.subImageParams[0], this.subImageParams[1], this.subImageParams[2]); // Get each image.
					}
				}
			}
		}

		this.setDefaults(false);

		if (!this.fieldsFilled()) {
			br.close();
			throw new IOException("Not all necessary configurations were declared.");
		}

		this.validateFields();
	}

	private void parseFlow(BufferedReader br) throws IOException {
		int lineNumber = 0;
		Pattern lineDefinition = Pattern.compile("[-x]+");
		int subdivisions = (int)Math.pow(2, this.subImages.length - 1);

		while ((this.currentLine = br.readLine()) != null && !this.currentLine.trim().toUpperCase().equals("END")) {
			this.currentLine = this.currentLine.trim().toLowerCase();

			if (lineDefinition.matcher(this.currentLine).matches() && !this.comment.matcher(this.currentLine).matches()) {
				// Check each character of the line and if it is marked as a note, then add it.
				for (int i = 0; i < this.currentLine.length() && i < this.inputKeys.length; i++) {
					if (this.currentLine.charAt(i) == 'x') {
						this.beats[i].add(new Beat(this.container, this.subImages[this.findImageIndex(lineNumber % subdivisions, subdivisions)], lineNumber));
					}
				}
				lineNumber++;
			}
		}
	}

	private int findImageIndex(int subdivisionIndex, int subdivisions) {
		if (subdivisionIndex == 0) {
			return 0;
		}
		else if (subdivisionIndex % 2 == 1) {
			return this.subImages.length - 1;
		}
		else {
			return this.recursiveFindImageIndex(subdivisionIndex, subdivisions, 1);
		}
	}

	private int recursiveFindImageIndex(int subdivisionIndex, int subdivisions, int currentImageIndex) {
		if (subdivisionIndex == subdivisions / 2) {
			return currentImageIndex;
		}
		else if (subdivisionIndex > subdivisions / 2) {
			return this.recursiveFindImageIndex(subdivisionIndex, subdivisions + (subdivisions / 2), currentImageIndex + 1);
		}
		else {
			return this.recursiveFindImageIndex(subdivisionIndex, subdivisions / 2, currentImageIndex + 1);
		}
	}

	private void setDefaults(boolean force) {
		if (this.topBarImage == null || force) {
			this.topBarImage = this.defaultTopBarImage;
		}
		if (this.inputKeys == null || force) {
			this.inputKeys = this.defaultInputKeys;
		}
		if (this.subImages == null || force) {
			this.subImages = this.defaultSubImages;
		}
	}

	private boolean fieldsFilled() {
		return this.song != null && this.bpm > -1;
	}

	private void validateFields() throws IOException {
		// Validate the information that was given.
		if (this.bpm <= 0) {
			throw new IOException("The tempo must be positive and non-null.");
		}
	}
}