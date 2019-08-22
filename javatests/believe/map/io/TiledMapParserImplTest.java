package believe.map.io;

import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import believe.map.data.BackgroundSceneData;
import believe.map.data.LayerData;
import believe.map.data.MapData;
import believe.map.data.proto.MapMetadataProto.MapBackground;
import believe.map.tiled.TiledMap;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.newdawn.slick.Image;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/** Unit tests for {@link TiledMapParserImpl}. */
@InstantiateMocksIn
final class TiledMapParserImplTest {
  private static final String PLAYER_START_X_PROPERTY = "x";
  private static final String PLAYER_START_Y_PROPERTY = "y";

  private TiledMapParserImpl parser;
  private List<BackgroundSceneData> backgroundScenes;

  @Mock private TiledMap tiledMap;
  @Mock private TiledMapLayerParser layerParser;
  @Mock private Image backgroundImage;
  @Mock private LayerData firstLayer;
  @Mock private LayerData secondLayer;

  @BeforeEach
  void setUp() {
    parser = new TiledMapParserImpl(PLAYER_START_X_PROPERTY, PLAYER_START_Y_PROPERTY, layerParser);
    backgroundScenes =
        Collections.singletonList(
            BackgroundSceneData.create(
                backgroundImage,
                MapBackground.newBuilder()
                    .setTopYPosition(0)
                    .setBottomYPosition(12)
                    .setHorizontalSpeedMultiplier(0.5f)
                    .build()));
    firstLayer = LayerData.newBuilder(tiledMap, 0).build();
    secondLayer = LayerData.newBuilder(tiledMap, 1).build();
  }

  @Test
  void parseMap_returnsMapDataWithPlayerLocation() {
    when(tiledMap.getMapProperty(PLAYER_START_X_PROPERTY)).thenReturn(Optional.of("123"));
    when(tiledMap.getMapProperty(PLAYER_START_Y_PROPERTY)).thenReturn(Optional.of("321"));
    when(tiledMap.getTileWidth()).thenReturn(10);
    when(tiledMap.getTileHeight()).thenReturn(100);

    MapData mapData = parser.parseMap(tiledMap, Collections.emptyList());

    assertThat(mapData.playerStartX()).isEqualTo(1230);
    assertThat(mapData.playerStartY()).isEqualTo(32100);
  }

  @Test
  void parseMap_returnsMapDataWithDimensions() {
    when(tiledMap.getWidth()).thenReturn(10);
    when(tiledMap.getHeight()).thenReturn(100);
    when(tiledMap.getTileWidth()).thenReturn(28);
    when(tiledMap.getTileHeight()).thenReturn(2);

    MapData mapData = parser.parseMap(tiledMap, Collections.emptyList());

    assertThat(mapData.width()).isEqualTo(280);
    assertThat(mapData.height()).isEqualTo(200);
  }

  @Test
  void parseMap_returnsMapDataWithLayerData() {
    when(tiledMap.getLayerCount()).thenReturn(2);
    when(layerParser.parseLayer(tiledMap, 0)).thenReturn(firstLayer);
    when(layerParser.parseLayer(tiledMap, 1)).thenReturn(secondLayer);

    MapData mapData = parser.parseMap(tiledMap, Collections.emptyList());

    assertThat(mapData.layers()).containsExactly(firstLayer, secondLayer);
  }

  @Test
  void parseMap_returnsMapDataWithBackgroundData() {
    MapData mapData = parser.parseMap(tiledMap, backgroundScenes);

    assertThat(mapData.backgroundScenes()).containsAllIn(backgroundScenes);
  }
}
