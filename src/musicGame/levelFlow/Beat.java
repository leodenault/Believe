package musicGame.levelFlow;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 * An image representation of a beat that a player must tap to.
 */
public class Beat extends AbstractComponent {

	private Animation animation;
	private Animation copy;
	private int x;
	private int y;
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
		super(container);
		this.init(animation, position, 0, 0);
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
		super(container);
		this.init(animation, position, x, y);
	}
	
	private void init(Animation animation, int position, int x, int y) {
		this.animation = animation;
		this.animation.setCurrentFrame(0);
		this.animation.setLooping(false);
		this.animation.stop();
		this.x = x;
		this.y = y;
		this.tapped = false;
		this.active = true;
		this.position = position;
	}
	
	@Override
	public int getHeight() {
		return this.animation.getHeight();
	}

	@Override
	public int getWidth() {
		return this.animation.getWidth();
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (this.isPlaying()) {
			this.copy.draw(this.x, this.y);
		}
		else {
			this.animation.draw(this.x, this.y);
		}
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
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
		return new Beat(this.container, this.animation.copy(), this.position, this.x, this.y);
	}
}
