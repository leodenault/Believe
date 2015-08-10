package musicGame.gui;

import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import musicGame.core.SynchedComboPattern;
import musicGame.core.SynchedComboPattern.TimeKeyPair;
import musicGame.levelFlow.FlowComponentListener;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.gui.GUIContext;

public class ComboSyncher extends ComponentBase {
	protected static final float BUFFER_TIME = 2.5f; // In seconds
	
	private static final int HEIGHT = 40;
	private static final int PIXELS_PER_SECOND = 100;
	private static final float BUFFER_LENGTH = BUFFER_TIME * PIXELS_PER_SECOND;
	private static final float ERROR_MARGIN = 0.15f; // In seconds
	private static final float ERROR_DISTANCE = ERROR_MARGIN * PIXELS_PER_SECOND;
	private static final Color NOT_ACTIVATED = new Color(0xffffff);
	private static final Color SUCCESS = new Color(0x00ff00);
	private static final Color MISSED = new Color(0xff0000);
	
	private final TrueTypeFont font;
	
	private int bpm;
	private float max;
	private float start;
	private float length;
	private float actionSectionLength;
	private float startBuffer;
	private SynchedComboPattern pattern;
	private Line tracker;
	private Music music;
	private List<TimeKeyPair> actions;
	private Queue<TimeKeyPair> actionsLeft;
	private HashMap<TimeKeyPair, Color> actionColors;
	
	public ComboSyncher(GUIContext container, SynchedComboPattern pattern, int bpm, int x, int y) {
		this(container, pattern,new TrueTypeFont(new Font("Verdana", Font.PLAIN, 20), true),
				bpm, x, y);
	}
	
	/**
	 * Used for testing
	 */
	protected ComboSyncher(GUIContext container, SynchedComboPattern pattern, TrueTypeFont font, int bpm, int x, int y) {
		super(container, x, y, 0, HEIGHT);
		this.bpm = bpm;
		this.pattern = pattern;
		this.tracker = new Line(x, y, x, y + HEIGHT);
		this.font = font;
		this.actionColors = new HashMap<SynchedComboPattern.TimeKeyPair, Color>();
		setUpPattern();
	}
	
	private void setUpPattern() {
		actions = pattern.getActions();
		actionsLeft = new LinkedList<TimeKeyPair>(actions);
		max = actions.get(actions.size() - 1).time;
		actionSectionLength = PIXELS_PER_SECOND * max * 60.0f / bpm;
		
		for (TimeKeyPair action : actions) {
			actionColors.put(action, NOT_ACTIVATED);
		}
	}
	
	private float getActionLocation(TimeKeyPair action) {
		return (actionSectionLength * action.time / max) + startBuffer + rect.getX();
	}
	
	private void checkForMissed() {
		if (!actionsLeft.isEmpty()) {
			TimeKeyPair current = actionsLeft.peek();
			if (tracker.getX() - getActionLocation(current) > ERROR_DISTANCE) {
				actionColors.put(current, MISSED);
				actionsLeft.remove();
				for (Object listener : listeners) {
					if (listener instanceof FlowComponentListener) {
						((FlowComponentListener)listener).beatMissed();
					}
				}
			}
		}
	}

	@Override
	protected void resetLayout() {}
	
	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		if (music != null) {
			g.setLineWidth(2f);
			g.setColor(new Color(0x00aaaa));
			g.fill(rect);
			g.setColor(new Color(0xff0000));
			g.draw(tracker);
			g.setColor(new Color(0xffffff));
			g.drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	
			g.setFont(font);
			for (TimeKeyPair action : actions) {
				g.setColor(actionColors.get(action));
				String key = String.valueOf(Character.toUpperCase(action.key));
				int centerX = font.getWidth(String.valueOf(action.key)) / 2;
				int centerY = g.getFont().getHeight(key) / 2;
				g.drawString(key, getActionLocation(action) - centerX,
						rect.getCenterY() - centerY);
			}
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		if (!actionsLeft.isEmpty() && music != null) {
			TimeKeyPair current = actionsLeft.peek();
				int index = actions.indexOf(current);
	
				if (current.key == c &&
						Math.abs(tracker.getX() - getActionLocation(current)) < ERROR_DISTANCE) {
					actionsLeft.remove();
					actionColors.put(current, SUCCESS);
					
					for (Object listener : listeners) {
						if (listener instanceof FlowComponentListener) {
							((FlowComponentListener)listener).beatSuccess(index);
						}
					}
					return;
				}
			
			for (Object listener : listeners) {
				if (listener instanceof FlowComponentListener) {
					((FlowComponentListener)listener).beatFailed();
				}
			}
		}
	}

	public void start(Music music) {
		this.music = music;
		float secondsInBeat = 60.0f / bpm;
		start = music.getPosition();
		float startIn = -(start + BUFFER_TIME) % (secondsInBeat) + BUFFER_TIME;
		startBuffer = PIXELS_PER_SECOND * startIn;
		rect.setWidth(actionSectionLength + startBuffer + BUFFER_LENGTH);
		length = startIn + (max * secondsInBeat) + BUFFER_TIME;
	}
	
	public void update() {
		if (music != null) {
			float progress = (music.getPosition() - start) / length;
			
			if (progress < 1.0f) {
				float x = progress * rect.getWidth() + rect.getX();
				tracker.set(x, tracker.getY1(), x, tracker.getY2());
				checkForMissed();
			} else {
				music = null;
				for (Object listener : listeners) {
					if (listener instanceof FlowComponentListener) {
						((FlowComponentListener)listener).songEnded();
					}
				}
			}
			
		}
	}
}
