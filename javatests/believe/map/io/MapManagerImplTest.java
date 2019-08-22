package believe.map.io;

import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import believe.gui.testing.FakeImage;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.map.data.BackgroundSceneData;
import believe.map.data.MapData;
import believe.map.data.proto.MapMetadataProto.MapBackground;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.newdawn.slick.SlickException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/** Unit tests for {@link MapManagerImpl}. */
@InstantiateMocksIn
final class MapManagerImplTest {
  private static final FakeImage BACKGROUND_IMAGE = new FakeImage(/* width= */ 0, /* height= */ 0);
  private static final List<BackgroundSceneData> BACKGROUND_SCENE_DATA =
      Arrays.asList(
          BackgroundSceneData.create(
              BACKGROUND_IMAGE,
              MapBackground.newBuilder()
                  .setFileLocation("/javatests/believe/map/io/data/fake_image.png")
                  .setTopYPosition(0.3f)
                  .setBottomYPosition(1f)
                  .setHorizontalSpeedMultiplier(0.78f)
                  .build()),
          BackgroundSceneData.create(
              BACKGROUND_IMAGE,
              MapBackground.newBuilder()
                  .setFileLocation("/javatests/believe/map/io/data/fake_image.png")
                  .setTopYPosition(0.1f)
                  .setBottomYPosition(0.9f)
                  .setHorizontalSpeedMultiplier(1f)
                  .build()));
  private static final MapData EXPECTED_MAP_DATA =
      MapData.newBuilder(
              /* playerStartX= */ 1,
              /* playerStartY= */ 1,
              /* width= */ 320,
              /* height= */ 640,
              BACKGROUND_SCENE_DATA)
          .build();

  private MapManagerImpl mapManager;

  @Mock private TiledMapParser tiledMapParser;

  @BeforeEach
  void setUp() {
    mapManager =
        new MapManagerImpl(
            tiledMapParser,
            "/javatests/believe/map/io/data",
            fileLocation -> Optional.of(BACKGROUND_IMAGE));
  }

  @Test
  @VerifiesLoggingCalls
  void getMap_mapDoesNotExist_logsErrorAndReturnsEmpty(VerifiableLogSystem logSystem)
      throws SlickException {
    assertThat(mapManager.getMap("map_does_not_exist")).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasSeverity(LogSeverity.ERROR)
        .hasPattern("Could not find map.*");
  }

  @Test
  void getMap_mapExistsAndHasNotYetBeenLoaded_loadsMapAndReturnsMapData() throws SlickException {
    when(tiledMapParser.parseMap(any(), eq(BACKGROUND_SCENE_DATA))).thenReturn(EXPECTED_MAP_DATA);

    assertThat(mapManager.getMap("test_map")).hasValue(EXPECTED_MAP_DATA);
    verify(tiledMapParser).parseMap(any(), eq(BACKGROUND_SCENE_DATA));
  }

  @Test
  void getMap_mapExistsAndHasBeenLoaded_doesNotReparseMap() throws SlickException {
    when(tiledMapParser.parseMap(any(), eq(BACKGROUND_SCENE_DATA))).thenReturn(EXPECTED_MAP_DATA);

    mapManager.getMap("test_map");
    mapManager.getMap("test_map");

    verify(tiledMapParser, times(1)).parseMap(any(), eq(BACKGROUND_SCENE_DATA));
  }
}
