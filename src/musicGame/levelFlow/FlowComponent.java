package musicGame.levelFlow;

import musicGame.GUI.AbstractContainer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

/**
 * Contains lanes with beats. The main component for creating musical games
 * where the user must tap beats to play.
 */
public class FlowComponent extends AbstractContainer implements ComponentListener {

	private Image image;
	private Lane[] lanes;
	private Music song;
	private char[] inputKeys;
	private PlayStatus status;

	/**
	 * Creates a new FlowComponent.
	 * 
	 * @param container	The context in which this FlowComponent is created and rendered.
	 * @param image		The image for the top bar of this FlowComponent.
	 * @param song		The song to play when activating this component.
	 * @param inputKeys	The keys in that each lane will respond to, in their respective order. There must be the same amount of lanes
	 * as there are keys. The int codes are used from the Input class constants.
	 * @param numLanes	The number of lanes for this component to contain.
	 * @param laneWidth	The width of each lane.
	 * @param subdivision	The current number of subdivisions for the song (4 = 16th note, 2 = 8th note...).
	 * @param bpm		The tempo of the song in beats per minute.
	 * @param offset	The offset of the song in milliseconds.
	 * 
	 * @see org.newdawn.slick.Input
	 */
	public FlowComponent(GUIContext container, Image image, Music song, char[] inputKeys, int numLanes, int laneWidth, int subdivision, double bpm, double offset) {
		super(container);
		this.init(image, song, inputKeys, numLanes, laneWidth, subdivision, bpm, offset);
	}

	/**
	 * Creates a new FlowComponent.
	 * 
	 * @param container	The context in which this FlowComponent is created and rendered.
	 * @param image		The image for the top bar of this FlowComponent.
	 * @param song		The song to play when activating this component.
	 * @param inputKeys	The keys in that each lane will respond to, in their respective order. There must be the same amount of lanes
	 * as there are keys. The int codes are used from the Input class constants.
	 * @param numLanes	The number of lanes for this component to contain.
	 * @param laneWidth	The width of each lane.
	 * @param subdivision	The current number of subdivisions for the song (4 = 16th note, 2 = 8th note...).
	 * @param bpm		The tempo of the song in beats per minute.
	 * @param offset	The offset of the song in milliseconds.
	 * @param x			The x position of this FlowComponent.
	 * @param y			The y position of this FlowComponent.
	 * 
	 * @see org.newdawn.slick.Input
	 */
	public FlowComponent(GUIContext container, Image image, Music song, char[] inputKeys, int numLanes, int laneWidth, int subdivision, double bpm, double offset, int x, int y) {
		super(container, x, y);
		this.init(image, song, inputKeys, numLanes, laneWidth, subdivision, bpm, offset);
	}

	/**
	 * Creates a new FlowComponent.
	 * 
	 * @param container	The context in which this FlowComponent is created and rendered.
	 * @param image		The image for the top bar of this FlowComponent.
	 * @param song		The song to play when activating this component.
	 * @param inputKeys	The keys in that each lane will respond to, in their respective order. There must be the same amount of lanes
	 * as there are keys. The int codes are used from the Input class constants.
	 * @param numLanes	The number of lanes for this component to contain.
	 * @param laneWidth	The width of each lane.
	 * @param subdivision	The current number of subdivisions for the song (4 = 16th note, 2 = 8th note...).
	 * @param bpm		The tempo of the song in beats per minute.
	 * @param offset	The offset of the song in milliseconds.
	 * @param x			The x position of this FlowComponent.
	 * @param y			The y position of this FlowComponent.
	 * @param width		The width of this FlowComponent.
	 * @param height	The height of this FlowComponent.
	 * 
	 * @see org.newdawn.slick.Input
	 */
	public FlowComponent(GUIContext container, Image image, Music song, char[] inputKeys, int numLanes, int laneWidth, int subdivision, double bpm, double offset, int x, int y, int width, int height) {
		super(container, x, y, width, height);
		this.init(image, song, inputKeys, numLanes, laneWidth, subdivision, bpm, offset);
	}

	private void init(Image image, Music song, char[] inputKeys, int numLanes, int laneWidth, int subdivision, double bpm, double offset) {
		if (inputKeys.length != numLanes) {
			throw new RuntimeException("Number of keys must be equal to number of lanes.");
		}
		this.image = image;
		this.inputKeys = inputKeys;
		this.status = PlayStatus.STOPPED;
		this.song = song;
		this.createLanes(numLanes, laneWidth, subdivision, bpm, offset);
	}

	private void createLanes(int numLanes, int laneWidth, int subdivision, double bpm, double offset) {
		if (numLanes <= 0) {
			throw new RuntimeException("FlowComponent must contain at least one lane.");
		}
			this.lanes = new Lane[numLanes];
		for (int i = 0; i < numLanes; i++) {
			Lane lane = new Lane(this.container, this.x + (i * laneWidth), this.y + this.image.getHeight() / 2, laneWidth, this.height, subdivision, bpm, offset);
			this.addChild(lane);
			this.lanes[i] = lane;
			lane.addListener(this);
		}
	}
	
	private void notifyLanes(char key) {
		for (int i = 0; i < this.inputKeys.length; i++) {
			if (key == this.inputKeys[i]) {
				if (this.lanes[i].consumeBeat()) {
					for (Object listener : this.listeners) {
						if (listener instanceof IFlowComponentListener) {
							IFlowComponentListener child = (IFlowComponentListener)listener;
							child.beatSuccess(i);
						}
					}
				}
				else {
					for (Object listener : this.listeners) {
						if (listener instanceof IFlowComponentListener) {
							IFlowComponentListener child = (IFlowComponentListener)listener;
							child.beatFailed();
						}
					}
				}
				break;
			}
		}
	}
	
	/**
	 * The multiplier for the speed at which the beats scroll onscreen.
	 */
	public double getSpeedMultiplier() {
		return this.lanes[0].getSpeedMultiplier();
	}
	
	/**
	 * The multiplier for the speed at which the beats scroll onscreen.
	 */
	public void setSpeedMultiplier(double value) {
		for (Lane lane : this.lanes) {
			lane.setSpeedMultiplier(value);
		}
	}
	
	/**
	 * Gets the current number of subdivisions for the song (4 = 16th note, 2 = 8th note...).
	 */
	public int getSubdivision() {
		return this.lanes[0].getSubdivision();
	}
	
	/**
	 * Sets the current number of subdivisions for the song (4 = 16th note, 2 = 8th note...).
	 */
	public void setSubdivision(int value) {
		for (Lane lane : this.lanes) {
			lane.setSubdivision(value);
		}
	}
	
	/**
	 * Gets the current tempo of the song in beats per minute.
	 */
	public double getBpm() {
		return this.lanes[0].getBpm();
	}
	
	/**
	 * Sets the current tempo of the song in beats per minute.
	 */
	public void setBpm(double value) {
		for (Lane lane : this.lanes) {
			lane.setBpm(value);
		}
	}
	
	/**
	 * The time in milliseconds allotted for missing a beat.
	 */
	public int getBuffer() {
		return this.lanes[0].getBuffer();
	}
	
	/**
	 * The time in milliseconds allotted for missing a beat.
	 */
	public void setBuffer(int value) {
		for (Lane lane : this.lanes) {
			lane.setBuffer(value);
		}
	}
	
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		for (Lane lane : this.lanes) {
			lane.setHeight(height);
		}
	}

	@Override
	public void addChild(AbstractComponent child) {
		this.children.add(child);
	}

	@Override
	public void removeChild(AbstractComponent child) {
		this.children.remove(child);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		g.drawImage(this.image, this.x, this.y);
		super.render(container, g);
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		if (this.status == PlayStatus.PLAYING) {
			this.notifyLanes(Character.toLowerCase(c)); // TODO: Do we really want this? Having to switch using CapsLock could be an interesting feature... Or annoyance.
		}
	}
	
	@Override
	public void componentActivated(AbstractComponent source) {
		for (Object listener : this.listeners) {
			if (listener instanceof IFlowComponentListener) {
				IFlowComponentListener child = (IFlowComponentListener)listener;
				child.beatMissed();
			}
		}
	}

	/**
	 * Updates the lanes in this FlowComponent.
	 */
	public void update() {
		for (Lane lane : this.lanes) {
			lane.updateBeats();
		}
	}

	/**
	 * Begins playing the music and this FlowComponent's lanes.
	 */
	public void play() {
		if (this.status != PlayStatus.PLAYING) {
			for (Lane lane : this.lanes) {
				lane.play();
			}
			if (this.status == PlayStatus.PAUSED) {
				this.song.resume();
			}
			else {
				this.song.play();
			}
			this.status = PlayStatus.PLAYING;
		}
	}

	/**
	 * Pauses playing the music and this FlowComponent's lanes.
	 */
	public void pause() {
		if (this.status == PlayStatus.PLAYING) {
			for (Lane lane : this.lanes) {
				lane.pause();
			}
			this.song.pause();
			this.status = PlayStatus.PAUSED;
		}
	}

	/**
	 * Stops playing the music and resets this FlowComponent's lanes.
	 */
	public void stop() {
		for (Lane lane : this.lanes) {
			lane.stop();
		}
		this.song.stop();
		this.status = PlayStatus.STOPPED;
	}

	/**
	 * The number of lanes this FlowComponent contains.
	 * 
	 * @return	The number of lanes this FlowComponent contains.
	 */
	public int numLanes() {
		return this.lanes.length;
	}


	/**
	 * Whether this FLowComponent is playing or not.
	 * 
	 * @return	Whether this FlowComponent is playing or not.
	 */
	public boolean isPlaying() {
		return this.status == PlayStatus.PLAYING;
	}

	/**
	 * Adds beats to the specified lanes and positions. This can done at any time.
	 * 
	 * @param beats	A 2D array of beats. The first dimension represents the lane to put the beat in,
	 * 					and the second dimension is for specifying the beats to be put in.
	 */
	public void addBeats(Beat[][] beats) {

		if (beats == null) {
			return;
		}
		else if (beats.length != this.lanes.length) {
			throw new RuntimeException("Length of beat types and positions must be the same as number of lanes.");
		}

		for (int i = 0; i < beats.length; i++) {
			this.lanes[i].addBeats(beats[i]);
		}
	}
}
