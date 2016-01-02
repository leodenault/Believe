package musicGame.physics;

import musicGame.geometry.Rectangle;
import musicGame.map.Tile;
import musicGame.physics.Collidable.CollidableType;
import musicGame.physics.TileCollisionHandler.TileCollidable;

public class TileCollisionHandler implements CollisionHandler<TileCollidable> {
	
	public interface TileCollidable extends Collidable, GravityObject {
		void setCanJump(boolean canJump);
	}
	
	@Override
	public void handleCollision(TileCollidable caller, Collidable other) {
		if (other.getType() == CollidableType.TILE) {
			Tile tile = (Tile)other;
			Rectangle callerRect = caller.getRect();
			Rectangle otherRect = other.getRect();
			Rectangle intersection = callerRect.intersection(otherRect);
			float interWidth = intersection.getWidth();
			float interHeight = intersection.getHeight();
			
			if (interWidth < interHeight) {
				boolean right = otherRect.horizontalCollisionDirection(callerRect);
				
				if ((right && !tile.hasRightNeighbour()) || (!right && !tile.hasLeftNeighbour())) {
					float distance = right ? interWidth : -interWidth;
					caller.setLocation(caller.getFloatX() + distance, caller.getFloatY());
				}
			} else {
				boolean down = otherRect.verticalCollisionDirection(callerRect);
				float speed = caller.getVerticalSpeed();

				if ((down && speed < 0 && !tile.hasBottomNeighbour()) || (!down && speed > 0 && !tile.hasTopNeighbour())) {
					float distance = down ? interHeight : -interHeight;
					caller.setLocation(caller.getFloatX(), caller.getFloatY() + distance);
					caller.setVerticalSpeed(0);

					if (!down) {
						caller.setCanJump(true);
					}
				}
			}
		}
	}
}
