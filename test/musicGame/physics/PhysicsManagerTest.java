package musicGame.physics;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashSet;
import java.util.Set;

import musicGame.physics.collision.Collidable;
import musicGame.physics.gravity.GravityObject;
import musicGame.physics.manager.PhysicsManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class PhysicsManagerTest {

	@Mock private GravityObject gravity;
	@Mock private Collidable coll;
	
	private PhysicsManager manager;
	private Set<GravityObject> gravityObjects;
	private Set<Collidable> collidables;
	private Set<Collidable> staticCollidables;
	
	@Before
	public void setUp() {
		initMocks(this);
		gravityObjects = new HashSet<>();
		collidables = new HashSet<>();
		staticCollidables = new HashSet<>();
		manager = new PhysicsManager(gravityObjects, collidables, staticCollidables);
	}
	
	@Test
	public void resetClearsAllSetsOfObjects() {
		manager.addGravityObject(gravity);
		manager.addCollidable(coll);
		manager.addStaticCollidable(coll);
		
		assertThat(gravityObjects, contains(gravity));
		assertThat(collidables, contains(coll));
		assertThat(staticCollidables, contains(coll));
		
		manager.reset();
		assertThat(gravityObjects, is(empty()));
		assertThat(collidables, is(empty()));
		assertThat(staticCollidables, is(empty()));
	}
}
