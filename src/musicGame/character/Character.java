package musicGame.character;

import musicGame.core.AnimationSet;
import musicGame.core.SpriteSheetManager;
import musicGame.gui.ComponentBase;
import musicGame.physics.Collidable;
import musicGame.physics.DamageBoxCollidable;
import musicGame.physics.DamageHandler;
import musicGame.physics.TileCollisionHandler;
import musicGame.physics.TileCollisionHandler.TileCollidable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public abstract class Character extends ComponentBase implements TileCollidable, DamageBoxCollidable {

	public static final float MAX_FOCUS = 1.0f;
	
	private float focus;
	private TileCollisionHandler tileHandler;
	private DamageHandler damageHandler;
	
	protected AnimationSet animSet;
	protected Animation anim;

	public Character(GUIContext container, int x, int y) throws SlickException {
		super(container, x, y);
		tileHandler = new TileCollisionHandler();
		damageHandler = new DamageHandler();
		animSet = SpriteSheetManager.getInstance().getAnimationSet(getSheetName());
		anim = animSet.get("idle");
		rect = new musicGame.geometry.Rectangle(x, y - anim.getHeight(), anim.getWidth(), anim.getHeight());
		anim.stop();
		anim.setCurrentFrame(0);
		focus = MAX_FOCUS;
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
		damageHandler.handleCollision(this, other);
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
	
	
	public float getFocus() {
		return focus;
	}
	
	@Override
	public void inflictDamage(float damage) {
		focus = Math.max(0f, focus - damage);
	}
	
	public void heal(float health) {
		focus = Math.min(MAX_FOCUS, focus + health);
	}
	
	public abstract void update(int delta);
	protected abstract String getSheetName();
}