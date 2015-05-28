package musicGame.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

public class TextComponent extends ComponentBase {

	protected static final class ColorSet {
		public int color;
		public int borderColor;
		public int textColor;

		public ColorSet(int color, int borderColor, int textColor) {
			this.color = color;
			this.borderColor = borderColor;
			this.textColor = textColor;
		}
	}
	
	private static final int PADDING = 5;
	private static final ColorSet DEFAULT = new ColorSet(0x000000, 0x898989, 0xffffff);
	
	private Rectangle innerRect;
	private String text;

	protected ColorSet colorSet;
	
	public TextComponent(GUIContext container, String text) {
		this(container, 0, 0, 0, 0, text);
	}
	
	public TextComponent(GUIContext container, int x, int y, int width, int height, String text) {
		super(container, x, y, width, height);
		this.text = text;
		this.colorSet = DEFAULT;
		setupInnerRect();
	}
	
	public void setColorSet(ColorSet set) {
		this.colorSet = set;
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		Rectangle oldClip = g.getClip();
		g.setClip(rect);
		
		// Colour the border
		g.setColor(new Color(colorSet.borderColor));
		g.fill(rect);
		
		// Colour the button
		g.setColor(new Color(colorSet.color));
		g.fill(innerRect);
		
		// Draw the text
		g.setColor(new Color(colorSet.textColor));
		int textWidth = g.getFont().getWidth(text);
		int textHeight = g.getFont().getHeight(text);
		g.translate(-textWidth/2, -textHeight/2);
		g.drawString(text, rect.getCenterX(), rect.getCenterY());
		g.translate(textWidth/2, textHeight/2);
		
		g.setClip(oldClip);
	}

	@Override
	protected void resetLayout() {
		setupInnerRect();
	}
	
	private void setupInnerRect() {
		this.innerRect = new Rectangle(rect.getX() + PADDING,
				rect.getY() + PADDING,
				rect.getWidth() - (PADDING * 2),
				rect.getHeight() - (PADDING * 2));
	}
}
