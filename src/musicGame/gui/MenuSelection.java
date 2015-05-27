package musicGame.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

public class MenuSelection extends ComponentBase {
	
	private static final class ColorSet {
		public int color;
		public int borderColor;

		public ColorSet(int color, int borderColor) {
			this.color = color;
			this.borderColor = borderColor;
		}
	}
	
	private static final int PADDING = 5;
	private static final ColorSet INACTIVE = new ColorSet(0x0cffb2, 0xcf0498);
	private static final ColorSet ACTIVE = new ColorSet(0xff0000, 0xffffff);
	private static final String DEFAULT_SELECTION_SOUND = "res/sfx/selection.ogg";
	private static final String DEFAULT_ACTIVATION_SOUND = "res/sfx/selection_picked.ogg";
	
	private Rectangle innerRect;
	private String text;
	private ColorSet colorSet;
	private Sound selectionSound;
	private Sound activationSound;
	
	public MenuSelection(GUIContext container, String text) throws SlickException {
		this(container, 0, 0, 0, 0, text);
	}
	
	public MenuSelection(GUIContext container, int x, int y, int width, int height, String text)
		throws SlickException {
		super(container, x, y, width, height);
		this.text = text;
		this.colorSet = INACTIVE;
		this.selectionSound = new Sound(DEFAULT_SELECTION_SOUND);
		this.activationSound = new Sound(DEFAULT_ACTIVATION_SOUND);
		setupInnerRect();
	}
	
	public void toggleSelect() {
		toggleSelect(false);
	}
	
	public void toggleSelect(boolean silent) {
		if (colorSet.equals(INACTIVE)) {
			colorSet = ACTIVE;
			
			if (!silent) {
				this.selectionSound.play();
			}
		} else {
			colorSet = INACTIVE;
		}
	}
	
	public void activate() {
		activationSound.play();
		for (Object listener : listeners) {
			((ComponentListener)listener).componentActivated(this);
		}
	}
	
	public boolean isSelected() {
		return colorSet.equals(INACTIVE);
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
		g.setColor(new Color(colorSet.borderColor));
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
