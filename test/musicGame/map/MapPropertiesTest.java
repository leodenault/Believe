package musicGame.map;

import static musicGame.map.MapProperties.COLLISION;
import static musicGame.map.MapProperties.COMMANDS;
import static musicGame.map.MapProperties.ENEMIES;
import static musicGame.map.MapProperties.FRONT;
import static musicGame.map.MapProperties.INVALID_PROP;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.contains;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.tiled.TiledMap;

public class MapPropertiesTest {
	private MapProperties properties;
	
	@Mock private TiledMap map;
	
	@Before
	public void setUp() {
		initMocks(this);
		properties = new MapProperties();
	}

	@Test
	public void fetchLayersShouldPlaceLayersWithFrontPropertyInFront() {
		mockery.checking(new Expectations() {{
			exactly(7).of(map).getLayerCount(); will(returnValue(6));
			exactly(18).of(map).getLayerProperty(
					with(any(Integer.class)),
					with(anyOf(equalTo(COLLISION), equalTo(ENEMIES), equalTo(COMMANDS))),
					with(equalTo(INVALID_PROP)));
			will(returnValue(INVALID_PROP));

			oneOf(map).getLayerProperty(0, FRONT, INVALID_PROP); will(returnValue(INVALID_PROP));
			oneOf(map).getLayerProperty(1, FRONT, INVALID_PROP); will(returnValue(FRONT));
			oneOf(map).getLayerProperty(2, FRONT, INVALID_PROP); will(returnValue(FRONT));
			oneOf(map).getLayerProperty(3, FRONT, INVALID_PROP); will(returnValue(INVALID_PROP));
			oneOf(map).getLayerProperty(4, FRONT, INVALID_PROP); will(returnValue(INVALID_PROP));
			oneOf(map).getLayerProperty(5, FRONT, INVALID_PROP); will(returnValue(FRONT));
		}});
		
		MapProperties.fetchLayers(map, properties);
		assertThat(properties.frontLayers, contains(1, 2, 5));
		assertThat(properties.rearLayers, contains(0, 3, 4));
	}

	@Test
	public void fetchLayersShouldIgnoreInvisibleLayers() {
		mockery.checking(new Expectations() {{
			exactly(6).of(map).getLayerCount(); will(returnValue(5));
			oneOf(map).getLayerProperty(0, COLLISION, INVALID_PROP); will(returnValue(COLLISION));
			oneOf(map).getLayerProperty(1, ENEMIES, INVALID_PROP); will(returnValue(ENEMIES));
			oneOf(map).getLayerProperty(2, COMMANDS, INVALID_PROP); will(returnValue(COMMANDS));

			exactly(4).of(map).getLayerProperty(
					with(any(Integer.class)),
					with(equalTo(COLLISION)),
					with(equalTo(INVALID_PROP)));
			will(returnValue(INVALID_PROP));
			exactly(3).of(map).getLayerProperty(
					with(any(Integer.class)),
					with(equalTo(ENEMIES)),
					with(equalTo(INVALID_PROP)));
			will(returnValue(INVALID_PROP));
			exactly(2).of(map).getLayerProperty(
					with(any(Integer.class)),
					with(equalTo(COMMANDS)),
					with(equalTo(INVALID_PROP)));
			will(returnValue(INVALID_PROP));

			oneOf(map).getLayerProperty(3, FRONT, INVALID_PROP); will(returnValue(FRONT));
			oneOf(map).getLayerProperty(4, FRONT, INVALID_PROP); will(returnValue(INVALID_PROP));
		}});
		
		MapProperties.fetchLayers(map, properties);
		assertThat(properties.frontLayers, contains(3));
		assertThat(properties.rearLayers, contains(4));
	}

}
