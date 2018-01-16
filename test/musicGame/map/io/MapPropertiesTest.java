package musicGame.map.io;

import static musicGame.map.io.MapProperties.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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
    when(map.getLayerProperty(anyInt(), anyString(), eq(INVALID_PROP)))
      .thenReturn(INVALID_PROP);
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
    when(map.getLayerProperty(not(eq(0)), eq(COLLISION), eq(INVALID_PROP)))
      .thenReturn(INVALID_PROP);
    when(map.getLayerProperty(not(eq(1)), eq(ENEMIES), eq(INVALID_PROP)))
      .thenReturn(INVALID_PROP);
    when(map.getLayerProperty(not(eq(2)), eq(COMMANDS), eq(INVALID_PROP)))
      .thenReturn(INVALID_PROP);
    when(map.getLayerProperty(not(eq(3)), eq(FRONT), eq(INVALID_PROP)))
      .thenReturn(INVALID_PROP);
    when(map.getLayerProperty(3, FRONT, INVALID_PROP)).thenReturn(FRONT);
    when(map.getLayerProperty(4, FRONT, INVALID_PROP)).thenReturn(INVALID_PROP);

    MapProperties.fetchLayers(map, properties);
    assertThat(properties.frontLayers, contains(3));
    assertThat(properties.rearLayers, contains(4));
  }

}
