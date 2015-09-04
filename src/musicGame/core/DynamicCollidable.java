package musicGame.core;


public interface DynamicCollidable extends StaticCollidable {
	float getVerticalSpeed();
	void setVerticalSpeed(float speed);
	void setLocation(float x, float y);
	float getFloatX();
	float getFloatY();
}
