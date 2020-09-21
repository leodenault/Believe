package believe.map.io;

import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.Mockito.doThrow;

import believe.gui.testing.FakeImage;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.map.data.BackgroundSceneData;
import believe.map.data.MapData;
import believe.map.data.TiledMapData;
import believe.map.data.proto.MapMetadataProto.MapBackground;
import believe.map.data.proto.MapMetadataProto.MapMetadata;
import believe.map.tiled.TiledMap;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.newdawn.slick.SlickException;

import java.util.Optional;

/** Unit tests for {@link MapMetadataParserImpl}. */
@InstantiateMocksIn
final class MapMetadataParserImplTest {
  private static final String MAP_LOCATION = "map location";
  private static final String TILE_SETS_LOCATION = "tile sets location";
  private static final MapBackground MAP_BACKGROUND =
      MapBackground.newBuilder()
          .setFileLocation("background image file location")
          .setTopYPosition(0f)
          .setBottomYPosition(1f)
          .setHorizontalSpeedMultiplier(12)
          .build();
  private static final MapMetadata MAP_METADATA =
      MapMetadata.newBuilder()
          .setMapLocation(MAP_LOCATION)
          .setTileSetsLocation(TILE_SETS_LOCATION)
          .addMapBackgrounds(MAP_BACKGROUND)
          .build();
  private static final TiledMapData TILED_MAP_DATA =
      TiledMapData.newBuilder(
              /* playerStartX= */ 3, /* playerStartY= */ 10, /* width= */ 20, /* height= */ 100)
          .build();
  private static final BackgroundSceneData BACKGROUND_SCENE_DATA =
      BackgroundSceneData.create(new FakeImage(), MAP_BACKGROUND);

  private MapMetadataParserImpl parser;

  @Mock private TiledMap tiledMap;

  @BeforeEach
  void setUp() {
    parser =
        new MapMetadataParserImpl(
            (mapLocation) -> tiledMap,
            tiledMap -> TILED_MAP_DATA,
            mapBackground -> Optional.of(BACKGROUND_SCENE_DATA));
  }

  @Test
  void parse_returnsValidData() {
    assertThat(parser.parse(MAP_METADATA))
        .hasValue(
            MapData.newBuilder(TILED_MAP_DATA).addBackgroundScene(BACKGROUND_SCENE_DATA).build());
  }

  @Test
  @VerifiesLoggingCalls
  void parse_tiledMapFailsToLoad_returnsEmptyAndLogsError(VerifiableLogSystem logSystem) {
    parser =
        new MapMetadataParserImpl(
            (mapLocation) -> null,
            tiledMap -> TILED_MAP_DATA,
            mapBackground -> Optional.of(BACKGROUND_SCENE_DATA));

    assertThat(parser.parse(MAP_METADATA)).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasSeverity(LogSeverity.ERROR)
        .containsExactly("Failed to load Tiled map.");
  }
}
