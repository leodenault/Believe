package musicGame.levelFlow;

import java.awt.Font;

import musicGame.gui.AbstractContainer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

/**
 * Contains lanes with beats. The main component for creating musical games
 * where the user must tap beats to play.
 */
public class FlowComponent extends AbstractContainer implements ComponentListener, MusicListener {

	private final TrueTypeFont KEY_FONT;
	
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
	public FlowComponent(GUIContext container, Music song, char[] inputKeys, int numLanes, int width, int subdivision, double bpm, double offset) {
		this(container, song, inputKeys, numLanes, width, subdivision, bpm, offset, 0, 0);
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
	public FlowComponent(GUIContext container, Music song, char[] inputKeys, int numLanes, int width, int subdivision, double bpm, double offset, int x, int y) {
		this(container, song, inputKeys, numLanes, width, subdivision, bpm, offset, x, y, 0);
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
	public FlowComponent(GUIContext container, Music song, char[] inputKeys, int numLanes, int width, int subdivision, double bpm, double offset, int x, int y, int height) {
		this(container, song, inputKeys, numLanes, width, subdivision, bpm, offset, x, y, height, new TrueTypeFont(new Font("Verdana", Font.PLAIN, 50), true));
	}
	
	/**
	 * Used for testing
	 */
	protected FlowComponent(GUIContext container, Music song, char[] inputKeys, int numLanes, int width,
			int subdivision, double bpm, double offset, int x, int y, int height, TrueTypeFont keyFont) {
		super(container, 0, 0, 0, 0);

		if (inputKeys.length != numLanes) {
			throw new RuntimeException("Number of keys must be equal to number of lanes.");
		}
		this.inputKeys = inputKeys;
		this.status = PlayStatus.STOPPED;
		this.song = song;
		this.song.addListener(this);
		this.rect.setWidth(0);
		this.createLanes(numLanes, width / numLanes, subdivision, bpm, offset);
		KEY_FONT = keyFont;
	}

	private void createLanes(int numLanes, int laneWidth, int subdivision, double bpm, double offset) {
		if (numLanes <= 0) {
			throw new RuntimeException("FlowComponent must contain at least one lane.");
		}
			this.lanes = new Lane[numLanes];
		for (int i = 0; i < numLanes; i++) {
			Lane lane = new Lane(this.container, (int)this.rect.getX() + (i * laneWidth), (int)this.rect.getY(), laneWidth, (int)this.rect.getHeight(), subdivision, bpm, offset);
			this.addChild(lane);
			this.lanes[i] = lane;
			lane.addListener(this);
		}
		
		this.setWidth(laneWidth * this.lanes.length);
	}
	
	private void notifyLanes(char key) {
		for (int i = 0; i < this.inputKeys.length; i++) {
			if (key == this.inputKeys[i]) {
				if (this.lanes[i].consumeBeat()) {
					for (Object listener : this.listeners) {
						if (listener instanceof FlowComponentListener) {
							FlowComponentListener child = (FlowComponentListener)listener;
							child.beatSuccess(i);
						}
					}
				}
				else {
					for (Object listener : this.listeners) {
						if (listener instanceof FlowComponentListener) {
							FlowComponentListener child = (FlowComponentListener)listener;
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
	protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
		// Render the bar at the top
		g.setColor(new Color(0x6e00b8));
		g.setLineWidth(2.0f);
		int start = lanes[0].getBufferStart();
		int end = lanes[0].getBufferEnd();
		int middle = (end + start) / 2;
		for (int i = start; i <= end; i += (end - start) / 5) {
			g.drawLine(this.getX(), i, this.getX() + this.getWidth(), i);
		}
		
		// Render the middle line for help with precision
		g.setColor(Color.blue);
		g.drawLine(this.getX(), middle, this.getX() + this.getWidth(), middle);
		
		// Render the letters
		g.setColor(Color.white);
		g.setFont(KEY_FONT);
		
		int bufferY = (lanes[0].getBufferEnd() + lanes[0].getBufferStart()) / 2;

		for (int i = 0; i < inputKeys.length; i++) {
			String key = String.valueOf(Character.toUpperCase(inputKeys[i]));
			int width = g.getFont().getWidth(key);
			int height = g.getFont().getHeight(key);
			
			g.drawString(key, lanes[i].getX() + (lanes[i].getWidth() - width) / 2, bufferY - height / 2);
		}
		
		super.renderComponent(container, g);
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
			if (listener instanceof FlowComponentListener) {
				FlowComponentListener child = (FlowComponentListener)listener;
				child.beatMissed();
			}
		}
	}

	/**
	 * Updates the lanes in this FlowComponent.
	 */
	public void update(int delta) {
		for (Lane lane : this.lanes) {
			lane.updateBeats(delta);
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
	
	public void reset() {
		for (Lane lane : this.lanes) {
			lane.stop();
		}
		this.song.pause();
		this.song.setPosition(0.0f);
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
	 * Whether this FlowComponent is playing or not.
	 * 
	 * @return	Whether this FlowComponent is playing or not.
	 */
	public boolean isPlaying() {
		return this.status == PlayStatus.PLAYING;
	}
	
	/**
	 * Whether this FlowComponent is paused or not.
	 * 
	 * @return	Whether this FlowComponent is paused or not.
	 */
	public boolean isPaused() {
		return this.status == PlayStatus.PAUSED;
	}
	
	/**
	 * Whether this FlowComponent is stopped or not.
	 * 
	 * @return	Whether this FlowComponent is stopped or not.
	 */
	public boolean isStopped() {
		return this.status == PlayStatus.STOPPED;
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

	@Override
	protected void resetLayout() {}

	@Override
	public void musicEnded(Music music) {
		for (Object listener : this.listeners) {
			if (listener instanceof FlowComponentListener) {
				((FlowComponentListener)listener).songEnded();
			}
		}
	}

	@Override
	public void musicSwapped(Music music, Music newMusic) {
		// This shouldn't be happening.
	}
}
