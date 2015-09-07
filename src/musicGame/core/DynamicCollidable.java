package musicGame.core;


public interface DynamicCollidable extends StaticCollidable {
	void setCanJump(boolean canJump);
	float getVerticalSpeed();
	void setVerticalSpeed(float speed);
	void setLocation(float x, float y);
	float getFloatX();
	float getFloatY();
}
