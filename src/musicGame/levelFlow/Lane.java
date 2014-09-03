package musicGame.levelFlow;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import musicGame.gui.AbstractContainer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 * Container for beats. This class manages the movement of the beats
 * according to a given tempo. It also notifies its score bar when
 * beats are missed or tapped.
 */
public class Lane extends AbstractContainer {

	private static final int DEFAULT_BUFFER = 90; // Time allowed to be off the beat, in milliseconds.
	private static int DEFAULT_SPEED = 100; // Pixels per second.

	private int speed;
	private int subdivision;
	private int buffer;
	private int bufferDistance;
	private double bpm;
	private double offset;
	private PlayStatus status;
	private long startTime;
	private long endTime;
	private long elapsedTime;
	private Queue<Beat> activeBeats;
	private Queue<Beat> inactiveBeats;
	private List<Beat> animations;

	/**
	 * Creates a new Lane instance.
	 * 
	 * @param container		The context in which this lane is created and rendered.
	 * @param subdivision	The current number of subdivisions for the song (4 = 16th note, 2 = 8th note...).
	 * @param bpm			The tempo of the song to which this lane is matched.
	 * @param offset		The number of milliseconds to wait before the first beat arrives.
	 */
	public Lane(GUIContext container, int subdivision, double bpm, double offset) {
		super(container);
		this.init(subdivision, bpm, offset);
	}

	/**
	 * Creates a new Lane instance.
	 * 
	 * @param container	The context in which this lane is created and rendered.
	 * @param x			The x location of this lane.
	 * @param y			The y location of this lane.
	 * @param subdivision	The current number of subdivisions for the song (4 = 16th note, 2 = 8th note...).
	 * @param bpm		The tempo of the song to which this lane is matched.
	 * @param offset	The number of milliseconds to wait before the first beat arrives.
	 */
	public Lane(GUIContext container, int x, int y, int subdivision, double bpm, double offset) {
		super(container, x, y);
		this.init(subdivision, bpm, offset);
	}

	/**
	 * Creates a new Lane instance.
	 * 
	 * @param container	The context in which this lane is created and rendered.
	 * @param x			The x location of this lane.
	 * @param y			The y location of this lane.
	 * @param width		The width of this lane.
	 * @param height	The height of this lane.
	 * @param subdivision	The current number of subdivisions for the song (4 = 16th note, 2 = 8th note...).
	 * @param bpm		The tempo of the song to which this lane is matched.
	 * @param offset	The number of milliseconds to wait before the first beat arrives.
	 */
	public Lane(GUIContext container, int x, int y, int width, int height, int subdivision, double bpm, double offset) {
		super(container, x, y, width, height);
		this.init(subdivision, bpm, offset);
	}

	private void init(int subdivision, double bpm, double offset) {
		this.speed = DEFAULT_SPEED;
		this.subdivision = subdivision;
		this.setBuffer(DEFAULT_BUFFER);
		this.bpm = bpm;
		this.offset = offset;
		this.elapsedTime = 0;
		this.activeBeats = new LinkedList<Beat>();
		this.inactiveBeats = new LinkedList<Beat>();
		this.animations = new LinkedList<Beat>();
		this.status = PlayStatus.STOPPED;
	}

	/**
	 * The multiplier for the speed at which the beats scroll onscreen.
	 */
	public double getSpeedMultiplier() {
		return this.speed / DEFAULT_SPEED;
	}

	/**
	 * The multiplier for the speed at which the beats scroll onscreen.
	 */
	public void setSpeedMultiplier(double value) {
		this.speed = (int)Math.round(value * DEFAULT_SPEED);
		this.setBuffer(this.buffer);
		for (Beat current : this.activeBeats) {
			current.setLocation(this.x + ((this.getWidth() - current.getWidth()) / 2), this.calculatePosition(current));
		}
	}

	/**
	 * The current number of subdivisions for the lane (4 = 16th note, 2 = 8th note...).
	 */
	public int getSubdivision() {
		return this.subdivision;
	}

	/**
	 * The current number of subdivisions for the lane (4 = 16th note, 2 = 8th note...).
	 */
	public void setSubdivision(int value) {
		this.subdivision = value;
	}

	/**
	 * The current tempo of the lane in beats per minute.
	 */
	public double getBpm() {
		return this.bpm;
	}

	/**
	 * The current tempo of the lane in beats per minute.
	 */
	public void setBpm(double value) {
		this.bpm = value;
	}

	/**
	 * The time in milliseconds allotted for missing a beat.
	 */
	public int getBuffer() {
		return (this.bufferDistance / this.speed) * 1000;
	}

	/**
	 * The time in milliseconds allotted for missing a beat.
	 */
	public void setBuffer(int value) {
		this.buffer = value;
		this.bufferDistance = (int)((value / 1000.0) * this.speed);
	}

	@Override
	public void addChild(AbstractComponent child) {
		this.children.add(child);
	}

	/**
	 * Adds a beat to this lane.
	 * 
	 * @param beat	The beat to be added.
	 */
	private void addBeat(Beat beat) {
		this.addChild(beat);
		this.activeBeats.add(beat);
		beat.setLocation(this.x + ((this.getWidth() - beat.getWidth()) / 2), this.calculatePosition(beat));
	}

	/**
	 * Adds a beat at each position specified.
	 * 
	 * @param beats	The beats to add.
	 */
	public void addBeats(Beat[] beats) {
		if (beats == null) {
			return;
		}

		for (Beat beat : beats) {
			this.addBeat(beat);
		}
	}

	@Override
	public void removeChild(AbstractComponent child) {
		this.children.remove(child);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		// Don't call super. We only want to render the active beats.
		for (Beat beat : this.activeBeats) {
			if (beat.getY() > this.height) {
				break;
			}
			beat.render(container, g);
		}

		try {
			for (Beat beat : this.animations) {
				if (beat.isPlaying()) {
					beat.render(container, g);
				}
				else {
					this.animations.remove(beat);
				}
			}
		}
		catch (ConcurrentModificationException e) {}

		/*g.setColor(new Color(0xFF00FF00));
		g.drawLine(this.x, this.y, this.x, this.y + this.height);
		g.drawLine(this.x + this.width, this.y, this.x + this.width, this.y + this.height);

		for (int i = 0; i < this.height; i += ((this.speed * 60) / this.bpm)) {
			g.drawLine(this.x, this.y + i, this.x + this.width, this.y + i);
		}*/
	}

	/**
	 * Whether this lane is currently playing or not.
	 * 
	 * @return	Whether this lane is currently playing or not.
	 */
	public boolean isPlaying() {
		return this.status == PlayStatus.PLAYING;
	}

	/**
	 * Updates the positions of the beats in this lane, if it is playing.
	 */
	public void updateBeats() {
		if (this.status == PlayStatus.PLAYING && this.activeBeats.size() > 0) {
			this.endTime = System.currentTimeMillis();
			double delta = ((this.endTime - this.startTime) / 1000.0) * this.speed;
			Iterator<Beat> beatIter = this.activeBeats.iterator();

			while (beatIter.hasNext()) {
				Beat beat = beatIter.next();
				beat.setLocation(beat.getX(), this.calculatePosition(beat) - delta);
			}

			if (this.activeBeats.element().getY() < this.y - bufferDistance - (this.activeBeats.element().getHeight() / 2)) {
				this.discardBeat();
				this.notifyListeners();
			}
		}
	}

	/**
	 * Starts counting the passing of time for this lane.
	 */
	public void play() {
		if (this.status != PlayStatus.PLAYING) {
			this.status = PlayStatus.PLAYING;
			this.startTime = System.currentTimeMillis() - this.elapsedTime;
			this.elapsedTime = 0;
		}
	}

	/**
	 * Pauses counting the passing of time for this lane.
	 */
	public void pause() {
		if (this.status == PlayStatus.PLAYING) {
			this.status = PlayStatus.PAUSED;
			this.elapsedTime = this.endTime - this.startTime;
			this.startTime = 0;
			this.endTime = 0;
		}
	}

	/**
	 * Stops counting the passing of time and resets this lane.
	 */
	public void stop() {
		this.status = PlayStatus.STOPPED;
		this.startTime = 0;
		this.endTime = 0;
		this.elapsedTime = 0;
		this.reset();
	}

	/**
	 * Consumes the nearest beat if it is within range.
	 * 
	 * @return	True if a beat was succesfully consumed, false if none were in range. 
	 */
	public boolean consumeBeat() {
		if (this.activeBeats.size() > 0 && (this.activeBeats.element().getY() + (this.activeBeats.element().getHeight() / 2)) >= this.y - bufferDistance && (this.activeBeats.element().getY() + (this.activeBeats.element().getHeight() / 2)) <= this.y + bufferDistance) {
			Beat temp = this.activeBeats.element();
			temp.consume();
			this.animations.add(temp);
			this.discardBeat();
			return true;
		}
		return false;
	}

	private void reset() {
		while (this.activeBeats.size() != 0) {
			this.inactiveBeats.add(this.activeBeats.remove());
		}

		while (this.inactiveBeats.size() != 0) {
			this.activeBeats.add(this.inactiveBeats.remove());
		}

		for (Beat beat : this.activeBeats) {
			beat.setLocation(this.x + ((this.getWidth() - beat.getWidth()) / 2), this.calculatePosition(beat));
			beat.reset();
		}
	}

	private void discardBeat() {
		if (this.activeBeats.size() > 0) {
			this.inactiveBeats.add(this.activeBeats.remove());
		}
	}

	private double calculatePosition(Beat beat) {
		return beat.getPosition() * ((this.speed * 60) / (this.bpm * this.subdivision)) + this.y + (this.offset / 1000) * this.speed;
	}
}
