package musicGame.physics;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import musicGame.geometry.Rectangle;
import musicGame.map.Tile;
import musicGame.physics.Collidable.CollidableType;
import musicGame.physics.TileCollisionHandler.TileCollidable;

public class TileCollisionHandlerTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
			setThreadingPolicy(new Synchroniser());
	}};
	
	private TileCollisionHandler handler;
	
	@Mock private TileCollidable first;
	@Mock private Collidable collidable;
	
	@Before
	public void setUp() {
		handler = new TileCollisionHandler();
	}
	
	@Test
	public void handleCollisionShouldNotRunWhenNotGivenATile() {
		mockery.checking(new Expectations() {{
			oneOf(collidable).getType(); will(returnValue(CollidableType.CHARACTER));
		}});
		handler.handleCollision(first, collidable);
	}
	
	@Test
	public void handleCollisionShouldMoveObjectsByShallowAxis() {
		final float x1 = 0;
		final float y1 = 0;
		final float x2 = 28;
		final float y2 = 38;
		
		mockery.checking(new Expectations() {{
			oneOf(first).getRect(); will(returnValue(new Rectangle(x1, y1, 22, 40)));
			oneOf(first).getFloatX(); will(returnValue(x1));
			oneOf(first).getFloatY(); will(returnValue(y1));
			oneOf(first).setLocation(-2, y1);
			
			oneOf(first).getRect(); will(returnValue(new Rectangle(x2, y2, 80, 50)));
			oneOf(first).getVerticalSpeed(); will(returnValue(-2f));
			oneOf(first).getFloatX(); will(returnValue(x2));
			oneOf(first).getFloatY(); will(returnValue(y2));
			oneOf(first).setLocation(x2, 40);
			oneOf(first).setVerticalSpeed(0f);
		}});

		Tile tile = new Tile(1, 1, 20, 20);
		handler.handleCollision(first, tile);
		handler.handleCollision(first, tile);
	}
	
	@Test
	public void handleCollisionIgnoresInternalVertices() {
		final float x = 8;
		final float y = 10;
		
		mockery.checking(new Expectations() {{
			oneOf(first).getRect(); will(returnValue(new Rectangle(x, y, 45, 11)));
			oneOf(first).getVerticalSpeed(); will(returnValue(1.3f));
		}});
		
		Tile tile = new Tile(0, 2, 10, 10);
		tile.setTopNeighbour(true);
		handler.handleCollision(first, tile);
	}
}
