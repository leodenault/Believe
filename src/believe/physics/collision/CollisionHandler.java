package believe.physics.collision;

public interface CollisionHandler<T extends Collidable> {
  void handleCollision(T caller, Collidable other);
}
