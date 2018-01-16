package musicGame.physics.collision;

import musicGame.geometry.Rectangle;

public interface Collidable {
  public enum CollidableType {
    TILE, CHARACTER, DAMAGE_BOX, COMMAND
  }

  /**
   * Formal implementations of this method should be fulfilled by entities that define a
   * consequence for themselves. For example, a character colliding with a tile will readjust its
   * position. A breakable box with a missile would break itself, and the missile would also
   * detect the box and make itself explode.
   *
   * @param other The other {@link Collidable} that this {@link Collidable} will use to determine
   * how to react.
   */
  void collision(Collidable other);
  CollidableType getType();
  Rectangle getRect();
}
