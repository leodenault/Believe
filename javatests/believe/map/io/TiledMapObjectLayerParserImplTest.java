package believe.map.io;

import static believe.map.tiled.testing.TiledFakes.fakeTiledObject;
import static believe.map.tiled.testing.TiledFakes.fakeTiledObjectGroup;
import static believe.util.Util.hashSetOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    parser = new TiledMapObjectLayerParserImpl(hashSetOf(objectParser));
  }

  @Test
  void parseObjectLayer_returnsValidObjectLayerData() {
    TiledObject tiledObject =
        fakeTiledObject(
            EntityType.COLLIDABLE_TILE.name(),
            /* x= */ 1,
            /* y= */ 1,
            /* width= */ 3,
            /* height= */ 4);

    parser.parseObjectGroup(
        fakeTiledObjectGroup("some group", Arrays.asList(tiledObject, tiledObject)));

    verify(objectParser, times(2))
        .parseObject(eq(EntityType.COLLIDABLE_TILE), eq(tiledObject), any());
  }

  @Test
  void parseObjectLayer_objectHasNoEntityType_parsesWithNoneType() {
    TiledObject tiledObject =
        fakeTiledObject(
            EntityType.NONE.name(), /* x= */ 0, /* y= */ 0, /* width= */ 0, /* height= */ 0);

    parser.parseObjectGroup(
        fakeTiledObjectGroup("some name", Collections.singletonList(tiledObject)));

    verify(objectParser).parseObject(eq(EntityType.NONE), eq(tiledObject), any());
  }
}
