package musicGame.core;

import musicGame.geometry.Rectangle;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PhysicsManagerTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
			setThreadingPolicy(new Synchroniser());
	}};
	
	private PhysicsManager manager;
	
	@Mock private StaticCollidable sC;
	@Mock private DynamicCollidable dC;
	
	@Before
	public void setUp() {
		manager = new PhysicsManager();
	}
	
	@Test
	public void checkCollisionsShouldMoveObjectsToAppropriateLocation() {
		mockery.checking(new Expectations() {{
			oneOf(dC).getFloatX(); will(returnValue(-1000f));
			oneOf(dC).getFloatY(); will(returnValue(0f));
			
			oneOf(sC).getRect(); will(returnValue(new Rectangle(0, 0, 50, 50)));
			oneOf(dC).getRect(); will(returnValue(new Rectangle(26, -30, 50, 50)));
			
			oneOf(sC).getRect(); will(returnValue(new Rectangle(0, 0, 50, 50)));
			oneOf(dC).getRect(); will(returnValue(new Rectangle(0, 26, 50, 50)));
			
			oneOf(sC).getRect(); will(returnValue(new Rectangle(0, 0, 50, 50)));
			oneOf(dC).getRect(); will(returnValue(new Rectangle(0, 50, 50, 50)));

			exactly(2).of(dC).setLocation(-1000f, 0f);
			exactly(2).of(dC).setVerticalSpeed(Float.MIN_VALUE);
		}});
		
		manager.addStaticCollidable(sC);
		manager.addDynamicCollidable(dC);

		manager.checkCollisions();
		manager.checkCollisions();
		manager.checkCollisions();
	}
	
	@Test
	public void checkCollisionsShouldNotCollideDynamicWithDynamic() {
		mockery.checking(new Expectations() {{
			exactly(2).of(dC).getFloatX(); will(returnValue(0f));
			exactly(2).of(dC).getFloatY(); will(returnValue(0f));
		}});
		manager.addDynamicCollidable(dC);
		manager.addDynamicCollidable(dC);
		manager.checkCollisions();
	}
	
	@Test
	public void checkCollisionsShouldNotCollideStaticWithStatic() {
		manager.addStaticCollidable(sC);
		manager.addStaticCollidable(sC);
		manager.checkCollisions();
	}
}
