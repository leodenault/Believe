package musicGame.character;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.AnimationSet;
import musicGame.core.SpriteSheetManager;
import musicGame.gui.ComponentBase;
import musicGame.physics.Collidable;
import musicGame.physics.TileCollisionHandler;
import musicGame.physics.TileCollisionHandler.TileCollidable;

public abstract class Character extends ComponentBase implements TileCollidable {

	private TileCollisionHandler tileHandler;
	
	protected AnimationSet animSet;
	protected Animation anim;

	public Character(GUIContext container, int x, int y) throws SlickException {
		super(container, x, y);
		tileHandler = new TileCollisionHandler();
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
	
	@Override
	public void collision(Collidable other) {
		tileHandler.handleCollision(this, other);
	}
	
	@Override
	public CollidableType getType() {
		return CollidableType.CHARACTER;
	}

	@Override
	protected void resetLayout() {}
	
	@Override
	protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
		anim.draw(rect.getX(), rect.getY());
	}
	
	public abstract void update(int delta);
	protected abstract String getSheetName();
}