package believe.map.io;

import static believe.map.tiled.testing.TiledFakes.fakeLayer;
import static believe.map.tiled.testing.TiledFakes.fakeTiledObjectGroup;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import believe.map.data.LayerData;
import believe.map.data.ObjectLayerData;
import believe.map.data.TiledMapData;
import believe.map.tiled.Layer;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledObjectGroup;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

@InstantiateMocksIn
final class TiledMapParserTest {
  private static final String PLAYER_START_X_PROPERTY = "x";
  private static final String PLAYER_START_Y_PROPERTY = "y";

  private TiledMapParser parser;

  @Mock private TiledMap tiledMap;
  @Mock private TiledMapLayerParser layerParser;
  @Mock private TiledMapObjectLayerParser objectLayerParser;
  @Mock private LayerData firstLayer;
  @Mock private LayerData secondLayer;
  @Mock private ObjectLayerData firstObjectLayerData;
  @Mock private ObjectLayerData secondObjectLayerData;

  @BeforeEach
  void setUp() {
    parser =
        TiledMapParser.create(
            PLAYER_START_X_PROPERTY, PLAYER_START_Y_PROPERTY, layerParser, objectLayerParser);
    firstLayer = LayerData.newBuilder(fakeLayer()).build();
    secondLayer = LayerData.newBuilder(fakeLayer()).build();
  }

  @Test
  void parseMap_returnsDataWithPlayerLocation() {
    when(tiledMap.getMapProperty(PLAYER_START_X_PROPERTY)).thenReturn(Optional.of("123"));
    when(tiledMap.getMapProperty(PLAYER_START_Y_PROPERTY)).thenReturn(Optional.of("321"));
    when(tiledMap.getTileWidth()).thenReturn(10);
    when(tiledMap.getTileHeight()).thenReturn(100);

    TiledMapData mapData = parser.parse(tiledMap);

    assertThat(mapData.playerStartX()).isEqualTo(1230);
    assertThat(mapData.playerStartY()).isEqualTo(32100);
  }

  @Test
  void parseMap_returnsMapDataWithDimensions() {
    when(tiledMap.getWidth()).thenReturn(10);
    when(tiledMap.getHeight()).thenReturn(100);
    when(tiledMap.getTileWidth()).thenReturn(28);
    when(tiledMap.getTileHeight()).thenReturn(2);

    TiledMapData mapData = parser.parse(tiledMap);

    assertThat(mapData.width()).isEqualTo(280);
    assertThat(mapData.height()).isEqualTo(200);
  }

  @Test
  void parseMap_returnsMapDataWithLayerData() {
    Layer layer1 = fakeLayer();
    Layer layer2 = fakeLayer();
    when(tiledMap.getLayers()).thenReturn(Arrays.asList(layer1, layer2));
    when(layerParser.parseLayer(layer1)).thenReturn(firstLayer);
    when(layerParser.parseLayer(layer2)).thenReturn(secondLayer);

    TiledMapData mapData = parser.parse(tiledMap);

    assertThat(mapData.layers()).containsExactly(firstLayer, secondLayer);
  }

  @Test
  void parseMap_returnsMapDataWithObjectLayerData() {
    TiledObjectGroup tiledObjectGroup1 = fakeTiledObjectGroup();
    TiledObjectGroup tiledObjectGroup2 = fakeTiledObjectGroup();
    when(tiledMap.getObjectGroups())
        .thenReturn(Arrays.asList(tiledObjectGroup1, tiledObjectGroup2));
    when(objectLayerParser.parseObjectGroup(tiledObjectGroup1)).thenReturn(firstObjectLayerData);
    when(objectLayerParser.parseObjectGroup(tiledObjectGroup2)).thenReturn(secondObjectLayerData);

    TiledMapData mapData = parser.parse(tiledMap);

    assertThat(mapData.objectLayers()).containsExactly(firstObjectLayerData, secondObjectLayerData);
  }
}
