package musicGame.physics;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PhysicsManager {
	
	private static final float GRAVITY = 0.00125f; // Pixels per millisecond^2
	
	private List<GravityObject> gravityObjects;
	private List<Collidable> collidables;
	
	public PhysicsManager() {
		this.gravityObjects = new LinkedList<GravityObject>();
		this.collidables = new LinkedList<Collidable>();
	}
	
	public void addGravityObject(GravityObject gravityObject) {
		gravityObjects.add(gravityObject);
	}
	
	public void addGravityObjects(Collection<? extends GravityObject> gravityObjects) {
		this.gravityObjects.addAll(gravityObjects);
	}
	
	public void addCollidable(Collidable collidable) {
		collidables.add(collidable);
	}
	
	public void addCollidables(Collection<? extends Collidable> collidables) {
		this.collidables.addAll(collidables);
	}
	
	public void update(int delta) {
		applyGravity(delta);
		checkCollisions();
	}
	
	private void applyGravity(int delta) {
		for (GravityObject grav : gravityObjects) {
			float speed = grav.getVerticalSpeed();
			grav.setLocation(grav.getFloatX(), grav.getFloatY() + speed * delta);
			speed += GRAVITY * delta;
			grav.setVerticalSpeed(speed);
		}
	}
	
	// This might end up needing some spacial data structures
	// for optimization, as it's currently not terribly efficient
	protected void checkCollisions() {
		for (ListIterator<Collidable> i1 = collidables.listIterator(); i1.hasNext();) {
			Collidable c1 = i1.next();
			
			if (i1.hasNext()) {
				for (Iterator<Collidable> i2 = collidables.listIterator(i1.nextIndex()); i2.hasNext();) {
					Collidable c2 = i2.next();
					
					if (c1.getRect().intersects(c2.getRect())) {
						c1.collision(c2);
						c2.collision(c1);
					}
				}
			}
		}
	}
}
