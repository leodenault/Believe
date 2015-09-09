package musicGame.character;

import musicGame.core.DynamicCollidable;
import musicGame.gui.ComponentBase;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.GUIContext;

public abstract class Character extends ComponentBase implements DynamicCollidable {

	protected Animation anim;

	public Character(GUIContext container, int x, int y) throws SlickException {
		super(container, x, y);
		anim = new Animation(new SpriteSheet("/res/graphics/sprites/stickFigure.png", 64, 128), 100);
		rect = new musicGame.geometry.Rectangle(x, y - anim.getHeight(), anim.getWidth(), anim.getHeight());
		anim.setLooping(true);
		anim.stop();
		anim.setCurrentFrame(0);
	}

	@Override
	public float getFloatX() {
		return rect.getX();
	}

	@Override
	public float getFloatY() {
		return rect.getY();
	}

	@Override
	public void setLocation(float x, float y) {
		if (rect != null) {
			rect.setLocation(x, y);
			resetLayout();
		}
	}
	
	public abstract void update(int delta);

	@Override
	protected void resetLayout() {}
	
	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		anim.draw(rect.getX(), rect.getY());
	}
}