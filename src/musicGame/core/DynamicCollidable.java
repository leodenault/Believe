package musicGame.core;


public interface DynamicCollidable extends StaticCollidable {
	void setLocation(float x, float y);
	float getFloatX();
	float getFloatY();
}
