package musicGame.physics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PhysicsManager {
	
	private static PhysicsManager INSTANCE;
	
	public static final float GRAVITY = 0.00125f; // Pixels per millisecond^2
	
	private Set<GravityObject> gravityObjects;
	private Set<Collidable> collidables;
	private Set<Collidable> staticCollidables;
	private Set<Collidable> removed;
	
	private PhysicsManager() {
		this.gravityObjects = new HashSet<GravityObject>();
		this.collidables = new HashSet<Collidable>();
		this.staticCollidables = new HashSet<Collidable>();
		this.removed = new HashSet<Collidable>();
	}
	
	public static PhysicsManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PhysicsManager();
		}
		
		return INSTANCE;
	}
	
	public void reset() {
		this.gravityObjects.clear();
		this.collidables.clear();
	}
	
	public void addGravityObject(GravityObject gravityObject) {
		gravityObjects.add(gravityObject);
	}
	
	public void addGravityObjects(Collection<? extends GravityObject> gravityObjects) {
		this.gravityObjects.addAll(gravityObjects);
	}
	
	/**
	 * All collidables added here will interact with all other collidables
	 */
	public void addCollidable(Collidable collidable) {
		collidables.add(collidable);
	}
	
	/**
	 * All collidables added here will interact with all other collidables
	 */
	public void addCollidables(Collection<? extends Collidable> collidables) {
		this.collidables.addAll(collidables);
	}
	
	/**
	 * All collidables added here do not interact with each other.
	 */
	public void addStaticCollidable(Collidable collidable) {
		this.staticCollidables.add(collidable);
	}
	
	/**
	 * All collidables added to this list do not interact with each other.
	 */
	public void addStaticCollidables(Collection<? extends Collidable> collidables) {
		this.staticCollidables.addAll(collidables);
	}
	
	public void removeCollidable(Collidable collidable) {
		if (!this.removed.contains(collidable)) {
			this.removed.add(collidable);
		}
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
		for (Collidable collidable : removed) {
			collidables.remove(collidable);
		}

		for (Collidable staticCollidable : removed) {
			staticCollidables.remove(staticCollidable);
		}
		
		removed.clear();

		Collidable[] coll = new Collidable[collidables.size()];
		Collidable[] statColl = new Collidable[staticCollidables.size()];
		collidables.toArray(coll);
		staticCollidables.toArray(statColl);

		for (int i = 0; i < coll.length; i++) {
			Collidable c1 = coll[i];

			// Interact with all static collidables first
			for (int j = 0; j < statColl.length; j++) {
				Collidable sc2 = statColl[j];
				engageCollision(c1, sc2);
			}
			
			// Then interact with the normal collidables
			for (int k = i + 1; k < coll.length; k++) {
				Collidable c2 = coll[k];
				engageCollision(c1, c2);
			}
		}
	}

	private void engageCollision(Collidable c1, Collidable c2) {
		if (c1.getRect().intersects(c2.getRect())) {
			c1.collision(c2);
			c2.collision(c1);
		}
	}
}
