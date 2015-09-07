package musicGame.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import musicGame.geometry.Rectangle;

import org.newdawn.slick.geom.Vector2f;

public class PhysicsManager {
	
	private class CollisionData {
		public DynamicCollidable dynColl;
		public Rectangle dynHitBox;
		public Rectangle statHitBox;
		public float interWidth;
		public float interHeight;
		
		public CollisionData(DynamicCollidable dynColl, StaticCollidable statColl) {
			this.dynColl = dynColl;
			dynHitBox = dynColl.getRect();
			statHitBox = statColl.getRect();
			
			Rectangle intersection = dynHitBox.intersection(statHitBox);
			interWidth = intersection.getWidth();
			interHeight = intersection.getHeight();
		}
	}
	
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
				child.setLocation(child.getFloatX(), child.getFloatY() + speed * delta);
				child.setVerticalSpeed(speed);
			}
		}
	}
	
	// This might end up needing some spacial data structures
	// for optimization, as it's currently not terribly efficient
	protected void checkCollisions() {
		// Separate collisions into vertical and horizontal resolutions
		List<CollisionData> hCol = new LinkedList<CollisionData>();
		List<CollisionData> vCol = new LinkedList<CollisionData>();

		// Find all collisions and separate by horizontal and vertical
		for (DynamicCollidable c1 : dynamics) {
			for (StaticCollidable c2 : statics) {
				CollisionData collision = new CollisionData(c1, c2);

				if (collision.dynHitBox.intersects(collision.statHitBox)) {
					if (collision.interWidth > collision.interHeight) {
						vCol.add(collision);
					} else {
						hCol.add(collision);
					}
				}
			}
		}

		// Resolve horizontal collisions first
		for (CollisionData collision : hCol) {
			if (collision.dynHitBox.intersects(collision.statHitBox)) {
				float distance = collision.statHitBox
						.horizontalCollisionDirection(collision.dynHitBox) ?
								collision.interWidth : -collision.interWidth;
				
				DynamicCollidable dyn = collision.dynColl;
				dyn.setLocation(dyn.getFloatX() + distance, dyn.getFloatY());
			}
		}
		
		// Resolve vertical collisions second
		for (CollisionData collision : vCol) {
			if (collision.dynHitBox.intersects(collision.statHitBox)) {
				DynamicCollidable dyn = collision.dynColl;
				boolean down = collision.statHitBox.verticalCollisionDirection(collision.dynHitBox);
				float speed = dyn.getVerticalSpeed();
				
				if (down && speed < 0 || !down && speed > 0) {
					float distance = down ? collision.interHeight : -collision.interHeight;
					dyn.setLocation(dyn.getFloatX(), dyn.getFloatY() + distance);
					dyn.setVerticalSpeed(0);
					
					if (!down) {
						dyn.setCanJump(true);
					}
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