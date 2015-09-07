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
		final float dcX = 26;
		final float dcY = -30;
		
		mockery.checking(new Expectations() {{
			oneOf(sC).getRect(); will(returnValue(new Rectangle(0, 0, 50, 50)));
			oneOf(dC).getRect(); will(returnValue(new Rectangle(dcX, dcY, 50, 50)));
			oneOf(dC).getVerticalSpeed(); will(returnValue(2f));
			exactly(2).of(dC).getFloatX(); will(returnValue(dcX));
			exactly(2).of(dC).getFloatY(); will(returnValue(dcY));
			oneOf(dC).setLocation(dcX, -50);
			oneOf(dC).setVerticalSpeed(0);
			oneOf(dC).setCanJump(true);
			
			oneOf(sC).getRect(); will(returnValue(new Rectangle(0, 0, 50, 50)));
			oneOf(dC).getRect(); will(returnValue(new Rectangle(0, 26, 50, 50)));
			oneOf(dC).getVerticalSpeed(); will(returnValue(-2f));
			oneOf(dC).getFloatX(); will(returnValue(0f));
			oneOf(dC).getFloatY(); will(returnValue(26f));
			oneOf(dC).setLocation(0, 50);
			oneOf(dC).setVerticalSpeed(0);
			
			oneOf(sC).getRect(); will(returnValue(new Rectangle(0, 0, 50, 50)));
			oneOf(dC).getRect(); will(returnValue(new Rectangle(0, 50, 50, 50)));
		}});
		
		manager.addStaticCollidable(sC);
		manager.addDynamicCollidable(dC);

		manager.checkCollisions();
		manager.checkCollisions();
		manager.checkCollisions();
	}
	
	@Test
	public void checkCollisionsShouldRunHorizontalCollisionsFirst() {
		mockery.checking(new Expectations() {{
			oneOf(sC).getRect(); will(returnValue(new Rectangle(-49, 0, 50, 50)));
			oneOf(sC).getRect(); will(returnValue(new Rectangle(0, 49, 50, 50)));
			exactly(2).of(dC).getRect(); will(returnValue(new Rectangle(0, 0, 50, 50)));
			oneOf(dC).getVerticalSpeed(); will(returnValue(2f));
			exactly(2).of(dC).getFloatX(); will(returnValue(0f));
			oneOf(dC).getFloatX(); will(returnValue(1f));
			exactly(3).of(dC).getFloatY(); will(returnValue(0f));
			oneOf(dC).setLocation(1, 0);
			oneOf(dC).setLocation(1, -1);
			oneOf(dC).setVerticalSpeed(0);
			oneOf(dC).setCanJump(true);
		}});

		manager.addStaticCollidable(sC);
		manager.addStaticCollidable(sC);
		manager.addDynamicCollidable(dC);
		
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
