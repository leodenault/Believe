package musicGame.physics;


import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import musicGame.geometry.Rectangle;
import musicGame.map.Tile;
import musicGame.physics.Collidable.CollidableType;
import musicGame.physics.TileCollisionHandler.TileCollidable;

public class TileCollisionHandlerTest {
	private TileCollisionHandler handler;
	
	@Mock private TileCollidable first;
	@Mock private Collidable collidable;
	
	@Before
	public void setUp() {
		initMocks(this);
		handler = new TileCollisionHandler();
	}
	
	@Test
	public void handleCollisionShouldNotRunWhenNotGivenATile() {
		mockery.checking(new Expectations() {{
			when(collidable.getType()).thenReturn(CollidableType.CHARACTER);
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
			when(first.getRect()).thenReturn(new Rectangle(x1, y1, 22, 40));
			when(first.getFloatX()).thenReturn(x1);
			when(first.getFloatY()).thenReturn(y1);

			
			when(first.getRect()).thenReturn(new Rectangle(x2, y2, 80, 50));
			when(first.getVerticalSpeed()).thenReturn(-2f);
			when(first.getFloatX()).thenReturn(x2);
			when(first.getFloatY()).thenReturn(y2);


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
			when(first.getRect()).thenReturn(new Rectangle(x, y, 45, 11));
			when(first.getVerticalSpeed()).thenReturn(1.3f);
		}});
		
		Tile tile = new Tile(0, 2, 10, 10);
		tile.setTopNeighbour(true);
		handler.handleCollision(first, tile);
	}
}
