package believe.map.io;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import believe.map.data.LayerData;
import believe.map.data.ObjectLayerData;
import believe.map.data.TiledMapData;
import believe.map.tiled.TiledMap;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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
    firstLayer = LayerData.newBuilder(tiledMap, 0).build();
    secondLayer = LayerData.newBuilder(tiledMap, 1).build();
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
    when(tiledMap.getLayerCount()).thenReturn(2);
    when(layerParser.parseLayer(tiledMap, 0)).thenReturn(firstLayer);
    when(layerParser.parseLayer(tiledMap, 1)).thenReturn(secondLayer);

    TiledMapData mapData = parser.parse(tiledMap);

    assertThat(mapData.layers()).containsExactly(firstLayer, secondLayer);
  }

  @Test
  void parseMap_returnsMapDataWithObjectLayerData() {
    when(tiledMap.getObjectGroupCount()).thenReturn(2);
    when(objectLayerParser.parseObjectLayer(tiledMap, 0)).thenReturn(firstObjectLayerData);
    when(objectLayerParser.parseObjectLayer(tiledMap, 1)).thenReturn(secondObjectLayerData);

    TiledMapData mapData = parser.parse(tiledMap);

    assertThat(mapData.objectLayers()).containsExactly(firstObjectLayerData, secondObjectLayerData);
  }
}
