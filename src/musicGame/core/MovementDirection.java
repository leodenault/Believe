package musicGame.core;

import org.newdawn.slick.Input;

public enum MovementDirection {
	NONE(-1), LEFT(Input.KEY_LEFT), RIGHT(Input.KEY_RIGHT);
	
	private int key;
	
	private MovementDirection(int key) {
		this.key = key;
	}
	
	public static MovementDirection directionForKey(int key) {
		switch (key) {
			case Input.KEY_LEFT:
				return LEFT;
			case Input.KEY_RIGHT:
				return RIGHT;
			default:
				return NONE;
		}
	}
	
	public int getKey() {
		return key;
	}
}
