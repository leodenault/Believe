package musicGame.levelFlow;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.Timer;
import musicGame.gui.AbstractContainer;

/**
 * Container for beats. This class manages the movement of the beats
 * according to a given tempo. It also notifies its score bar when
 * beats are missed or tapped.
 */
public class Lane extends AbstractContainer {

	private static final int DEFAULT_BUFFER = 300; // Time allowed to be off the beat, in milliseconds.
	private static final double BANNER_CONSTANT = 0.219298246; // Constant for scaling banner size depending on speed

	// Visible for testing
	protected static final int DEFAULT_SPEED = 100; // Pixels per second.
	protected static final int MIN_BANNER = 45; // Minimum banner height

	private int speed;
	private int subdivision;
	private int buffer;
	private int bufferDistance;
	private double bpm;
	private double offset;
	private PlayStatus status;
	private Queue<Beat> activeBeats;
	private Queue<Beat> inactiveBeats;
	private List<Beat> animations;
	private Timer timer;

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
		this.activeBeats = new LinkedList<Beat>();
		this.inactiveBeats = new LinkedList<Beat>();
		this.animations = new LinkedList<Beat>();
		this.status = PlayStatus.STOPPED;
		this.timer = new Timer();
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
			current.setLocation(this.rect.getX() + ((this.getWidth() - current.getWidth()) / 2), this.calculatePosition(current));
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
		return (int)((double)this.bufferDistance / this.speed) * 1000;
	}

	/**
	 * The time in milliseconds allotted for missing a beat.
	 */
	public void setBuffer(int value) {
		this.buffer = value;
		double seconds = value / 1000.0;
		this.bufferDistance = (int)(MIN_BANNER + (speed - DEFAULT_SPEED) * seconds * BANNER_CONSTANT);
	}
	
	public int getBufferStart() {
		return (int)(this.rect.getY());
	}
	
	public int getBufferEnd() {
		return (int)(this.rect.getY() + this.bufferDistance);
	}

	/**
	 * Adds a beat to this lane.
	 * 
	 * @param beat	The beat to be added.
	 */
	private void addBeat(Beat beat) {
		this.addChild(beat);
		this.activeBeats.add(beat);
		beat.setLocation(this.rect.getX() + ((this.getWidth() - beat.getWidth()) / 2), this.calculatePosition(beat));
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
	protected void renderComponent(GUIContext context, Graphics g) {
		// Don't call super. We only want to render the active beats.
		for (Beat beat : this.activeBeats) {
			if (beat.getY() > this.rect.getHeight()) {
				break;
			}
			beat.renderComponent(container, g);
		}

		try {
			for (Beat beat : this.animations) {
				if (beat.isPlaying()) {
					beat.renderComponent(container, g);
				}
				else {
					this.animations.remove(beat);
				}
			}
		}
		catch (ConcurrentModificationException e) {}

/*		g.setColor(new Color(0xFF00FF00));
		g.drawLine(this.getX(), this.getY(), this.getX(), this.getY() + this.getHeight());
		g.drawLine(this.getX() + this.getWidth(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight());

		for (int i = 0; i < this.getHeight(); i += ((this.speed * 60) / this.bpm)) {
			g.drawLine(this.getX(), this.getY() + i, this.getX() + this.getWidth(), this.getY() + i);
		}*/
		
		/*g.setColor(Color.orange);
		g.drawLine(this.getX(), this.bufferDistance + this.getY(), this.getX() + this.getWidth(), this.bufferDistance + this.getY());
		g.drawLine(this.getX(), -this.bufferDistance + this.getY(), this.getX() + this.getWidth(), -this.bufferDistance + this.getY());
		g.drawLine(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY());*/
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
	public void updateBeats(int delta) {
		if (this.status == PlayStatus.PLAYING && this.activeBeats.size() > 0) {
			timer.update(delta);
			double distance = (timer.getElapsedTime() / 1000.0) * this.speed;
			Iterator<Beat> beatIter = this.activeBeats.iterator();

			while (beatIter.hasNext()) {
				Beat beat = beatIter.next();
				beat.setLocation(beat.getX(), this.calculatePosition(beat) - distance);
			}

			Beat first = this.activeBeats.element(); 
			if (first.getY() + first.getHeight() < this.rect.getY()) {
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
			timer.play();
		}
	}

	/**
	 * Pauses counting the passing of time for this lane.
	 */
	public void pause() {
		if (this.status == PlayStatus.PLAYING) {
			timer.pause();
			this.status = PlayStatus.PAUSED;
		}
	}

	/**
	 * Stops counting the passing of time and resets this lane.
	 */
	public void stop() {
		timer.stop();
		this.status = PlayStatus.STOPPED;
		this.reset();
	}

	/**
	 * Consumes the nearest beat if it is within range.
	 * 
	 * @return	True if a beat was succesfully consumed, false if none were in range. 
	 */
	public boolean consumeBeat() {
		if (this.activeBeats.size() > 0) {
			Beat first = this.activeBeats.element();
			int firstCenterY = first.getY() + first.getHeight() / 2;
			if (firstCenterY >= this.rect.getY()
					&& firstCenterY <= this.rect.getY() + bufferDistance) {
				Beat temp = this.activeBeats.element();
				temp.consume();
				this.animations.add(temp);
				this.discardBeat();
				return true;
			}
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
			beat.setLocation(this.rect.getX() + ((this.getWidth() - beat.getWidth()) / 2), this.calculatePosition(beat));
			beat.reset();
		}
	}

	private void discardBeat() {
		if (this.activeBeats.size() > 0) {
			this.inactiveBeats.add(this.activeBeats.remove());
		}
	}

	private double calculatePosition(Beat beat) {
		return beat.getPosition() * ((this.speed * 60) / (this.bpm * this.subdivision))
				+ this.rect.getY()
				+ (this.offset / 1000) * this.speed
				- beat.getHeight() / 2
				+ this.bufferDistance / 2;
	}

	@Override
	protected void resetLayout() {}
}
