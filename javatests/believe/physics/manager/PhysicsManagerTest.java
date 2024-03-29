package believe.physics.manager;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandlerExecutor;
import believe.physics.gravity.GravityObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

public class PhysicsManagerTest {

  @Mock private GravityObject gravity;
  @Mock private Collidable coll;
  @Mock private CollisionHandlerExecutor collisionHandlerExecutor;

  private PhysicsManager manager;
  private Set<GravityObject> gravityObjects;
  private Set<Collidable<?>> collidables;
  private Set<Collidable<?>> staticCollidables;

  @Before
  public void setUp() {
    initMocks(this);
    gravityObjects = new HashSet<>();
    collidables = new HashSet<>();
    staticCollidables = new HashSet<>();
    manager =
        new PhysicsManager(
            collisionHandlerExecutor, gravityObjects, collidables, staticCollidables);
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
