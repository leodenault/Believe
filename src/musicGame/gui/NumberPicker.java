package musicGame.gui;

import musicGame.core.Util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.gui.GUIContext;


public class NumberPicker extends MenuSelection {
	
	@SuppressWarnings("serial")
	private static final Polygon ARROW = new Polygon() {{
		addPoint(-0.5f, 0);
		addPoint(0.5f, -1);
		addPoint(0.5f, 1);
	}};
	
	private boolean activated;
	private int value;
	private int min;
	private int max;

	public NumberPicker(GUIContext container, String text, int value) throws SlickException {
		this(container, text, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public NumberPicker(GUIContext container, String text, int value, int min, int max) throws SlickException {
		this(container, 0, 0, 0, 0, text, value, min, max);
	}
	
	public NumberPicker(GUIContext container, int x, int y, int width,
			int height, String text, int value) throws SlickException {
		this(container, x, y, width, height, text, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public NumberPicker(GUIContext container, int x, int y, int width,
			int height, String text, int value, int min, int max) throws SlickException {
		super(container, x, y, width, height, text);
		this.value = value;
		this.activated = false;
		this.min = min;
		this.max = max;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public void activate() {
		super.activate();
		activated = !activated;
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		if (activated) {
			if (key == Input.KEY_LEFT && value > min) {
				value--;
			} else if (key == Input.KEY_RIGHT && value < max) {
				value++;
			}
		}
	}

	@Override
	protected void drawText(GUIContext context, Graphics g) {
			Rectangle oldClip = Util.changeClipContext(g, rect);
			
			// Draw the text
			g.setColor(new Color(colorSet.textColor));
			int textWidth = g.getFont().getWidth(text);
			int textHeight = g.getFont().getHeight(text);

			g.pushTransform();
			g.translate(-textWidth/2, -textHeight);
			g.drawString(text, rect.getCenterX(), rect.getCenterY());
			g.popTransform();
			
			// Draw the value
			String stringValue = String.valueOf(value);
			int valueWidth = g.getFont().getWidth(stringValue);
			int valueHeight = g.getFont().getHeight(stringValue);

			g.pushTransform();
			g.translate(-valueWidth/2, valueHeight);
			g.drawString(stringValue, rect.getCenterX(), rect.getCenterY());
			g.popTransform();
			
			// Draw the plus and minus signs if the component is active
			if (activated) {
				Shape left = ARROW.transform(Transform.createScaleTransform(rect.getWidth() / 20, rect.getHeight() * 0.75f / ARROW.getHeight()))
						.transform(Transform.createTranslateTransform(rect.getX() + rect.getWidth() / 10, rect.getCenterY()));
				Shape right = ARROW.transform(Transform.createScaleTransform(-rect.getWidth() / 20, rect.getHeight() * 0.75f / ARROW.getHeight()))
						.transform(Transform.createTranslateTransform(rect.getMaxX() - rect.getWidth() / 10, rect.getCenterY()));
				
				g.fill(left);
				g.fill(right);
			}
			
			Util.resetClipContext(g, oldClip);
	}
}
