package musicGame.map;

import static musicGame.map.io.MapProperties.COLLISION;
import static musicGame.map.io.MapProperties.COMMANDS;
import static musicGame.map.io.MapProperties.ENEMIES;
import static musicGame.map.io.MapProperties.FRONT;
import static musicGame.map.io.MapProperties.INVALID_PROP;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import musicGame.map.io.MapProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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
		when(map.getLayerCount()).thenReturn(6);
		when(map.getLayerProperty(anyInt(), anyString(),INVALID_PROP)).thenReturn(INVALID_PROP);
		when(map.getLayerProperty(0, FRONT, INVALID_PROP)).thenReturn(INVALID_PROP);
		when(map.getLayerProperty(1, FRONT, INVALID_PROP)).thenReturn(FRONT);
		when(map.getLayerProperty(2, FRONT, INVALID_PROP)).thenReturn(FRONT);
		when(map.getLayerProperty(3, FRONT, INVALID_PROP)).thenReturn(INVALID_PROP);
		when(map.getLayerProperty(4, FRONT, INVALID_PROP)).thenReturn(INVALID_PROP);
		when(map.getLayerProperty(5, FRONT, INVALID_PROP)).thenReturn(FRONT);
		
		MapProperties.fetchLayers(map, properties);
		assertThat(properties.frontLayers, contains(1, 2, 5));
		assertThat(properties.rearLayers, contains(0, 3, 4));
	}

	@Test
	public void fetchLayersShouldIgnoreInvisibleLayers() {
		when(map.getLayerCount()).thenReturn(5);
		when(map.getLayerProperty(0, COLLISION, INVALID_PROP)).thenReturn(COLLISION);
		when(map.getLayerProperty(1, ENEMIES, INVALID_PROP)).thenReturn(ENEMIES);
		when(map.getLayerProperty(2, COMMANDS, INVALID_PROP)).thenReturn(COMMANDS);
		when(map.getLayerProperty(anyInt(), COLLISION, INVALID_PROP)).thenReturn(INVALID_PROP);
		when(map.getLayerProperty(anyInt(), ENEMIES, INVALID_PROP)).thenReturn(INVALID_PROP);
		when(map.getLayerProperty(anyInt(), COMMANDS, INVALID_PROP)).thenReturn(INVALID_PROP);
		when(map.getLayerProperty(3, FRONT, INVALID_PROP)).thenReturn(FRONT);
		when(map.getLayerProperty(4, FRONT, INVALID_PROP)).thenReturn(INVALID_PROP);
		
		MapProperties.fetchLayers(map, properties);
		assertThat(properties.frontLayers, contains(3));
		assertThat(properties.rearLayers, contains(4));
	}

}
