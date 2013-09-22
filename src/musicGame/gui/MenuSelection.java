package musicGame.gui;

import musicGame.menu.action.MenuAction;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class MenuSelection extends AbstractComponent {

	private static final int DEFAULT_WIDTH = 100;
	private static final int DEFAULT_HEIGHT = 40;
	private static final int DEFAULT_LINE_WIDTH = 5;
	private static final Color DEFAULT_TEXT_COLOR = new Color(0xCF0498);
	private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0x0CFFB2);
	private static final String DEFAULT_SELECTION_SOUND = "res/sfx/selection.ogg";
		
	private String text;
	private Rectangle rect;
	
	private Color currentTextColor;
	private Color currentBackgroundColor;
	private Color inactiveTextColor;
	private Color inactiveBackgroundColor;
	private Color activeTextColor;
	private Color activeBackgroundColor;
	
	private Sound selectionSound;
	private MenuAction action;
	
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean playSound;
	
	public MenuSelection(GUIContext container, int x, int y) throws SlickException {
		this(container, "", x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public MenuSelection(GUIContext container, String text, int x, int y, int width, int height) throws SlickException {
		super(container);
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.playSound = false;
		this.inactiveTextColor = DEFAULT_TEXT_COLOR;
		this.inactiveBackgroundColor = DEFAULT_BACKGROUND_COLOR;
		this.activeTextColor = DEFAULT_TEXT_COLOR;
		this.activeBackgroundColor = DEFAULT_BACKGROUND_COLOR;
		this.currentTextColor = this.inactiveTextColor;
		this.currentBackgroundColor = this.inactiveBackgroundColor;
		this.selectionSound = new Sound(DEFAULT_SELECTION_SOUND);
		/*
		 * This needs to be called for two reasons. First because the super constructor
		 * calls it before any values can be set. Second because the location simply
		 * needs to be set.
		 */
		this.setLocation(x, y); 
		this.rect = new Rectangle(x, y, width, height);
	}

	public void select() {
		if (this.playSound) {
			this.selectionSound.play();
		}
		this.currentBackgroundColor = this.activeBackgroundColor;
		this.currentTextColor = this.activeTextColor;
	}
	
	public void deselect() {
		this.currentBackgroundColor = this.inactiveBackgroundColor;
		this.currentTextColor = this.inactiveTextColor;
	}
	
	public void activate() {
		if (this.action != null) {
			this.action.performAction();
		}
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}
	
	public void setPlaySound(boolean playSound) {
		this.playSound = playSound;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void setMenuAction(MenuAction action) {
		this.action = action;
	}
	
	public Color getInactiveTextColor() {
		return inactiveTextColor;
	}

	public void setInactiveTextColor(Color textColor) {
		this.inactiveTextColor = textColor;
	}

	public Color getInactiveBackgroundColor() {
		return inactiveBackgroundColor;
	}

	public void setInactiveBackgroundColor(Color backgroundColor) {
		this.inactiveBackgroundColor = backgroundColor;
	}
	
	public Color getActiveTextColor() {
		return activeTextColor;
	}

	public void setActiveTextColor(Color activeTextColor) {
		this.activeTextColor = activeTextColor;
	}

	public Color getActiveBackgroundColor() {
		return activeBackgroundColor;
	}

	public void setActiveBackgroundColor(Color activeBackgroundColor) {
		this.activeBackgroundColor = activeBackgroundColor;
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		g.setColor(this.currentBackgroundColor);
		g.fill(this.rect);
		g.setColor(this.currentTextColor);
		g.setLineWidth(DEFAULT_LINE_WIDTH);
		g.draw(this.rect);
		Font currentFont = g.getFont();
		int textWidth = currentFont.getWidth(this.text);
		int textHeight = currentFont.getHeight(this.text);
		g.drawString(this.text, this.x + ((this.width - textWidth) / 2), this.y + ((this.height - textHeight) / 2));
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		if (this.rect != null) {
			this.rect.setLocation(x, y);
		}
	}

}
