package musicGame.character;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Character {

	private Rectangle rect;
	private Animation anim;
	
	public Character(int x, int y) throws SlickException {
		anim = new Animation(new SpriteSheet("/res/graphics/sprites/stickFigure.png", 64, 128), 100);
		rect = new Rectangle(x, y - anim.getHeight(), anim.getWidth(), anim.getHeight());
		anim.setLooping(true);
		anim.stop();
		anim.setCurrentFrame(0);
	}
	
	public void render() {
		anim.draw(rect.getX(), rect.getY());
	}
	
	public void move() {
		anim.start();
	}
	
	public void stop() {
		anim.stop();
		anim.setCurrentFrame(0);
	}
	
	public void setLocation(int x, int y) {
		rect.setLocation(x, y);
	}
}
