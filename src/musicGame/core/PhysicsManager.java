package musicGame.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import musicGame.geometry.Rectangle;

import org.newdawn.slick.geom.Vector2f;

public class PhysicsManager {
	private List<StaticCollidable> statics;
	private List<DynamicCollidable> dynamics;
	private HashMap<DynamicCollidable, Vector2f> coords;
	
	public PhysicsManager() {
		statics = new LinkedList<StaticCollidable>();
		dynamics = new LinkedList<DynamicCollidable>();
		coords = new HashMap<DynamicCollidable, Vector2f>();
	}
	
	public void addStaticCollidable(StaticCollidable collidable) {
		statics.add(collidable);
	}
	
	public void addDynamicCollidable(DynamicCollidable collidable) {
		dynamics.add(collidable);
		coords.put(collidable, new Vector2f(collidable.getFloatX(), collidable.getFloatY()));
	}
	
	public void addStaticCollidables(Collection<? extends StaticCollidable> collidables) {
		statics.addAll(collidables);
	}
	
	public void addDynamicCollidables(Collection<? extends DynamicCollidable> collidables) {
		dynamics.addAll(collidables);
		
		for (DynamicCollidable collidable : collidables) {
			coords.put(collidable, new Vector2f(collidable.getFloatX(), collidable.getFloatY()));
		}
	}
	
	public void update(int delta) {
		checkCollisions();
		updatePositions();
	}
	
	// This might end up needing some spacial data structures
	// for optimization, as it's currently not terribly efficient
	protected void checkCollisions() {
		for (DynamicCollidable c1 : dynamics) {
			for (StaticCollidable c2 : statics) {
				Rectangle r1 = c1.getRect();
				Rectangle r2 = c2.getRect();
				
				if (r1.intersects(r2)) {
					Vector2f c1Coords = coords.get(c1);
					c1.setLocation(c1Coords.getX(), c1Coords.getY());
				}
			}
		}
	}
	
	private void updatePositions() {
		for (DynamicCollidable child : dynamics) {
			coords.put(child, new Vector2f(child.getFloatX(), child.getFloatY()));
		}
	}
}
