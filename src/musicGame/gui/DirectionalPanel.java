package musicGame.gui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class DirectionalPanel<T extends AbstractComponent> extends AbstractComponent implements Iterable<T> {

	protected LinkedList<T> items;
	protected int spacing;
	
	private int x;
	private int y;
	
	public DirectionalPanel(GUIContext context) {
		super(context);
		this.spacing = 0;
		this.x = 0;
		this.y = 0;
		this.items = new LinkedList<T>();
	}
	
	public void add(T item) {
		this.items.add(item);
		this.resetItemLocations();
	}
	
	public void addAll(Collection<? extends T> items) {
		this.items.addAll(items);
	}
	
	@Override
	public int getHeight() {
		if (this.items == null) {
			return 0;
		}
		
		int itemHeight = 0;
		for (AbstractComponent item : this.items) {
			itemHeight += item.getHeight();
		}
		return itemHeight + ((this.items.size() - 1) * this.spacing);
	}

	@Override
	public int getWidth() {
		if (this.items == null) {
			return 0;
		}
		
		int maxWidth = 0;
		for (AbstractComponent item : this.items) {
			int itemWidth = item.getWidth();
			maxWidth = (itemWidth > maxWidth) ? itemWidth : maxWidth;
		}
		return maxWidth;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}
	
	public void setSpacing(int spacing) {
		this.spacing = spacing;
		this.resetItemLocations();
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		for (AbstractComponent item : this.items) {
			item.render(container, g);
		}
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		this.resetItemLocations();
	}

	@Override
	public Iterator<T> iterator() {
		return this.items.iterator();
	}
	
	public int size() {
		return this.items.size();
	}
	
	public T get(int index) {
		return this.items.get(index);
	}
	
	public int indexOf(T item) {
		return this.items.indexOf(item);
	}
	
	protected void resetItemLocations() {
		if (this.items != null) {
			int width = this.getWidth();
			int currentHeight = 0;
			for (AbstractComponent item : this.items) {
				int x = this.x + (width - item.getWidth()) / 2;
				int y = this.y + currentHeight;
				currentHeight += item.getHeight() + this.spacing;
				item.setLocation(x, y);
			}
		}
	}
}
