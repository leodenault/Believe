package musicGame.core;

import org.newdawn.slick.Input;

public enum MovementDirection {
	NONE(-1, 0), LEFT(Input.KEY_LEFT, -1), RIGHT(Input.KEY_RIGHT, 1);
	
	private int key;
	private int value;
	
	private MovementDirection(int key, int value) {
		this.key = key;
		this.value = value;
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
	
	public int getValue() {
		return value;
	}
}
