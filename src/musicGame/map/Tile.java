package musicGame.map;

import musicGame.core.StaticCollidable;
import musicGame.geometry.Rectangle;

public class Tile implements StaticCollidable {
	
	private Rectangle rect;
	
	public Tile(int x, int y, int tileWidth, int tileHeight) {
		rect = new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
	}
	
	@Override
	public Rectangle getRect() {
		return rect;
	}
}
