package musicGame.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import musicGame.geometry.Rectangle;

import org.newdawn.slick.geom.Vector2f;

public class PhysicsManager {
	private static final float GRAVITY = 0.000625f; // Pixels per millisecond^2
	private static final float NO_FALLING = Float.MIN_VALUE;
	
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
		applyGravity(delta);
		checkCollisions();
		updatePositions();
	}
	
	private void applyGravity(int delta) {
		for (DynamicCollidable child : dynamics) {
			float speed = child.getVerticalSpeed();
			
			if (speed != NO_FALLING) {
				speed += GRAVITY * delta;
				child.setLocation(child.getFloatX(), child.getFloatY() + speed);
				child.setVerticalSpeed(speed);
			}
		}
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
					c1.setVerticalSpeed(NO_FALLING);
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
