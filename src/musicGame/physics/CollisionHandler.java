package musicGame.physics;

public interface CollisionHandler<T extends Collidable> {
	void handleCollision(T caller, Collidable other);
}
