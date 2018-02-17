package believe.physics.collision;

import believe.geometry.Rectangle;
import believe.map.collidable.Tile;
import believe.physics.gravity.GravityObject;
import believe.physics.collision.Collidable.CollidableType;
import believe.physics.collision.TileCollisionHandler.TileCollidable;

public class TileCollisionHandler implements CollisionHandler<TileCollidable> {

  public interface TileCollidable extends Collidable, GravityObject {
    void landed();
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
            caller.landed();
          }
        }
      }
    }
  }
}
