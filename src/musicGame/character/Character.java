package musicGame.character;

import musicGame.core.AnimationSet;
import musicGame.core.DynamicCollidable;
import musicGame.core.SpriteSheetManager;
import musicGame.gui.ComponentBase;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public abstract class Character extends ComponentBase implements DynamicCollidable {

	protected AnimationSet animSet;
	protected Animation anim;

	public Character(GUIContext container, int x, int y) throws SlickException {
		super(container, x, y);
		animSet = SpriteSheetManager.getInstance().getAnimationSet(getSheetName());
		anim = animSet.get("idle");
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
	protected abstract String getSheetName();

	@Override
	protected void resetLayout() {}
	
	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		anim.draw(rect.getX(), rect.getY());
	}
}