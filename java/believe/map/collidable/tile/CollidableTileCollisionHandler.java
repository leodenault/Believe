package believe.map.collidable.tile;

import believe.geometry.Rectangle;
import believe.map.collidable.tile.CollidableTileCollisionHandler.TileCollidable;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.gravity.GravityObject;
import dagger.Reusable;
import javax.inject.Inject;

@Reusable
public class CollidableTileCollisionHandler
    implements CollisionHandler<CollidableTile, TileCollidable<?>> {

  public interface TileCollidable<T extends TileCollidable<T>>
      extends Collidable<T>, GravityObject {
    void landed();
  }

  @Inject
  CollidableTileCollisionHandler() {}

  @Override
  public void handleCollision(CollidableTile collidableTile, TileCollidable<?> subject) {
    Rectangle subjectRect = subject.rect();
    Rectangle collidableTileRect = collidableTile.rect();
    Rectangle intersection = subjectRect.intersection(collidableTileRect);
    float interWidth = intersection.getWidth();
    float interHeight = intersection.getHeight();

    if (interWidth < interHeight) {
      if (interWidth <= 0) {
        return;
      }

      boolean subjectIsToTheRight = collidableTileRect.horizontalCollisionDirection(subjectRect);

      float distance = subjectIsToTheRight ? interWidth : -interWidth;
      subject.setLocation(subject.getFloatX() + distance, subject.getFloatY());
    } else if (interHeight > 0) {
      boolean subjectIsBelow = collidableTileRect.verticalCollisionDirection(subjectRect);
      float speed = subject.getVerticalSpeed();

      if (speed == 0 || (subjectIsBelow && speed < 0) || (!subjectIsBelow && speed > 0)) {
        float distance = subjectIsBelow ? interHeight : -interHeight;
        subject.setLocation(subject.getFloatX(), subject.getFloatY() + distance);
        subject.setVerticalSpeed(0);

        if (!subjectIsBelow) {
          subject.landed();
        }
      }
    }
  }
}
