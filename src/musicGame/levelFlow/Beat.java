package musicGame.levelFlow;

import musicGame.gui.ComponentBase;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

/**
 * An image representation of a beat that a player must tap to.
 */
public class Beat extends ComponentBase {

	private Animation animation;
	private Animation copy;
	private int position;
	private boolean tapped;
	private boolean active;
	
	/**
	 * Creates a new BeatComponent.
	 * 
	 * @param container	The container displaying this component.
	 * @param sheet		The sprite sheet containing the beat animation.
	 * @param position	The index of this beats position in the song.
	 */
	public Beat(GUIContext container, Animation animation, int position) {
		this(container, animation, position, 0, 0);
	}
	
	/**
	 * Creates a new BeatComponent.
	 * 
	 * @param container	The container displaying this component.
	 * @param sheet		The sprite sheet containing the beat animation.
	 * @param position	The index of this beats position in the song.
	 * @param x			The x location of the component.
	 * @param y			The y location of the component.
	 */
	public Beat(GUIContext container, Animation animation, int position, int x, int y) {
		super(container, x, y, animation.getWidth(), animation.getHeight());
		this.animation = animation;
		this.animation.setCurrentFrame(0);
		this.animation.setLooping(false);
		this.animation.stop();
		this.tapped = false;
		this.active = true;
		this.position = position;
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (this.isPlaying()) {
			this.copy.draw(rect.getX(), rect.getY());
		}
		else {
			this.animation.draw(rect.getX(), rect.getY());
		}
	}

	/**
	 * Moves the component.
	 * 
	 * @param x	X coordinate.
	 * @param y	Y coordinate.
	 */
	public void setLocation(double x, double y) {
		this.setLocation((int)Math.round(x), (int)Math.round(y));
	}
	
	/**
	 * True if this beat is active.
	 */
	public boolean getActive() {
		return this.active;
	}
	
	/**
	 * Sets whether this beat is active or not.
	 */
	public void setActive(boolean value) {
		this.active = value;
	}
	
	/**
	 * True if this beat was succesfully tapped on.
	 */
	public boolean getTapped() {
		return this.tapped;
	}
	
	/**
	 * Sets whether this beat has been tapped on yet.
	 */
	public void setTapped(boolean value) {
		this.tapped = value;
	}
	
	/**
	 * The index of this beat's position.
	 */
	public int getPosition() {
		return this.position;
	}
	
	public boolean isPlaying() {
		if (this.copy == null) {
			return false;
		}
		return !this.copy.isStopped();
	}
	
	public void consume() {
		this.copy = this.animation.copy();	// Not so pretty, but the clone method doesn't work in events...
											// It causes the event to be fired a second time because of the association
											// of the container.
		this.copy.start();
		this.tapped = true;
	}
	
	public void reset() {
		if (this.copy != null) {
			this.copy.stop();
		}
		this.copy = null;
		this.tapped = false;
	}

	protected Beat clone() {
		return new Beat(this.container, this.animation.copy(), this.position, (int)rect.getX(), (int)rect.getY());
	}

	@Override
	protected void resetLayout() {}
}
