package musicGame.physics;

import musicGame.geometry.Rectangle;

public interface Collidable {
	public enum CollidableType {
		TILE, CHARACTER, DAMAGE_BOX
	}
	
	void collision(Collidable other);
	CollidableType getType();
	Rectangle getRect();
}
