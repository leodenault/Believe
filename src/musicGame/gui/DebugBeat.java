package musicGame.gui;

import java.util.HashMap;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class DebugBeat extends ComponentBase {
	private static final int RADIUS = 5;
	private static final int COLOR = 0xFF0000;
	
	private boolean display;
	private HashMap<Character, Boolean> detectionKeys;
	
	public DebugBeat(GUIContext container, Set<Character> detectionKeys) {
		this(container, detectionKeys, 0, 0);
	}

	public DebugBeat(GUIContext container, Set<Character> detectionKeys, int x, int y) {
		super(container, x, y, RADIUS, RADIUS);
		this.display = false;
		this.detectionKeys = generateDetectionKeys(detectionKeys);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (detectionKeys.containsKey(c)) {
			display = true;
			detectionKeys.put(c, true);
		}
	}
	
	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		if (detectionKeys.containsKey(c)) {
			detectionKeys.put(c, false);
			display = detectionKeys.values().contains(true);
		}
	}
	
	private HashMap<Character, Boolean> generateDetectionKeys(Set<Character> detectionKeys) {
		HashMap<Character, Boolean> map = new HashMap<>();
		for (Character key : detectionKeys) {
			map.put(key, false);
		}
		return map;
	}
	
	@Override
	protected void resetLayout() {}

	@Override
	protected void renderComponent(GUIContext context, Graphics g)
			throws SlickException {
		if (display) {
			g.setColor(new Color(COLOR));
			g.fillOval(getX() - RADIUS, getY() - RADIUS, RADIUS * 2, RADIUS * 2);
		}
	}
}
