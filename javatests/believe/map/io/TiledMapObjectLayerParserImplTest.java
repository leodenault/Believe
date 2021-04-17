package believe.map.io;

import static believe.map.tiled.testing.TiledFakes.fakeTiledObject;
import static believe.map.tiled.testing.TiledFakes.fakeTiledObjectGroup;
import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import believe.map.data.EntityType;
import believe.map.tiled.TiledObject;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.Arrays;
import java.util.Collections;

/** Unit tests for {@link TiledMapObjectLayerParserImpl}. */
@InstantiateMocksIn
final class TiledMapObjectLayerParserImplTest {
  private TiledMapObjectLayerParserImpl parser;

  @Mock private ObjectParser objectParser;

  @BeforeEach
  void setUp() {
    parser =
        new TiledMapObjectLayerParserImpl(
            hashMapOf(entry(EntityType.COLLIDABLE_TILE, objectParser)));
  }

  @Test
  void parseObjectLayer_returnsValidObjectLayerData() {
    TiledObject tiledObject =
        fakeTiledObject(
            EntityType.COLLIDABLE_TILE.name(),
            "",
            /* x= */ 1,
            /* y= */ 1,
            /* width= */ 3,
            /* height= */ 4);

    parser.parseObjectGroup(
        fakeTiledObjectGroup("some group", Arrays.asList(tiledObject, tiledObject)));

    verify(objectParser, times(2)).parseObject(eq(tiledObject));
  }

  @Test
  void parseObjectLayer_objectEntityHasNoParser_doesNotParse() {
    TiledObject tiledObject =
        fakeTiledObject(
            EntityType.NONE.name(), "", /* x= */ 0, /* y= */ 0, /* width= */ 0, /* height= */ 0);

    parser.parseObjectGroup(
        fakeTiledObjectGroup("some name", Collections.singletonList(tiledObject)));

    verify(objectParser, never()).parseObject(eq(tiledObject));
  }
}
