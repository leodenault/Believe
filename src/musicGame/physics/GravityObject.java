package musicGame.physics;

public interface GravityObject {
	float getVerticalSpeed();
	void setVerticalSpeed(float speed);
	void setLocation(float x, float y);
	float getFloatX();
	float getFloatY();
}
