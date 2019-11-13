package believe.map.io;

import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.map.data.MapData;
import believe.map.data.TiledMapData;
import believe.map.data.proto.MapMetadataProto.MapMetadata;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

/** Unit tests for {@link MapManagerImpl}. */
@InstantiateMocksIn
final class MapManagerImplTest {
  private static final String METADATA_DIRECTORY = "/javatests/believe/map/io/data";
  private static final MapMetadata MAP_METADATA =
      MapMetadata.newBuilder().setMapLocation("some location").build();
  private static final MapData EXPECTED_MAP_DATA =
      MapData.newBuilder(
              TiledMapData.newBuilder(
                      /* playerStartX= */ 1,
                      /* playerStartY= */ 1,
                      /* width= */ 320,
                      /* height= */ 640)
                  .build())
          .build();

  private MapManagerImpl mapManager;

  @Mock private MapMetadataParser mapMetadataParser;

  @BeforeEach
  void setUp() {
    mapManager = new MapManagerImpl(mapMetadataParser, METADATA_DIRECTORY);
  }

  @Test
  @VerifiesLoggingCalls
  void getMap_metadataCannotBeRead_logsErrorAndReturnsEmpty(VerifiableLogSystem logSystem) {
    when(mapMetadataParser.parse(MAP_METADATA)).thenReturn(Optional.empty());

    assertThat(mapManager.getMap("map_does_not_exist")).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasSeverity(LogSeverity.ERROR)
        .hasPattern(
            "Failed to read map metadata at '" + METADATA_DIRECTORY + "/map_does_not_exist.pb'.");
  }

  @Test
  void getMap_mapExistsAndHasNotYetBeenLoaded_loadsMapAndReturnsMapData() {
    when(mapMetadataParser.parse(MAP_METADATA)).thenReturn(Optional.of(EXPECTED_MAP_DATA));

    assertThat(mapManager.getMap("test_map")).hasValue(EXPECTED_MAP_DATA);
  }

  @Test
  void getMap_mapExistsAndHasBeenLoaded_doesNotReparseMap() {
    when(mapMetadataParser.parse(MAP_METADATA)).thenReturn(Optional.of(EXPECTED_MAP_DATA));

    mapManager.getMap("test_map");
    mapManager.getMap("test_map");

    verify(mapMetadataParser, times(1)).parse(MAP_METADATA);
  }
}
