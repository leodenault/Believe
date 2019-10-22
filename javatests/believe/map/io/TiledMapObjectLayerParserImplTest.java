package believe.map.io;

import static believe.util.Util.hashSetOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import believe.map.tiled.EntityType;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledMapObjectPropertyProvider;
import believe.map.tiled.TiledObject;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

/** Unit tests for {@link TiledMapObjectLayerParserImpl}. */
@InstantiateMocksIn
final class TiledMapObjectLayerParserImplTest {
  private static final String ENTITY_TYPE_PROPERTY = "entity type";

  private TiledMapObjectLayerParserImpl parser;

  @Mock private ObjectParser objectParser;
  @Mock private TiledMap tiledMap;

  @BeforeEach
  void setUp() {
    parser = new TiledMapObjectLayerParserImpl(ENTITY_TYPE_PROPERTY, hashSetOf(objectParser));
  }

  // Due to the Tiled issue found at https://github.com/bjorn/tiled/issues/91 we have to adjust the
  // y position of objects by subtracting the height. The reason for this is that, unlike tiles, the
  // y position for objects is defined at the bottom of the object rather than the top.
  @Test
  void parseObjectLayer_returnsValidObjectLayerData() {
    when(tiledMap.getObjectCount(/* groupID= */ 0)).thenReturn(2);
    when(tiledMap.getObjectProperty(
            /* groupID= */ eq(0), /* objectID= */ anyInt(), eq(ENTITY_TYPE_PROPERTY)))
        .thenReturn(Optional.of(EntityType.COLLIDABLE_TILE.name()));
    when(tiledMap.getObjectX(/* groupID= */ eq(0), anyInt())).thenReturn(1);
    when(tiledMap.getObjectY(/* groupID= */ eq(0), anyInt())).thenReturn(5);
    when(tiledMap.getObjectWidth(/* groupID= */ eq(0), anyInt())).thenReturn(3);
    when(tiledMap.getObjectHeight(/* groupID= */ eq(0), anyInt())).thenReturn(4);

    parser.parseObjectLayer(tiledMap, /* layerId= */ 0);

    verify(objectParser)
        .parseObject(
            eq(
                TiledObject.create(
                    EntityType.COLLIDABLE_TILE,
                    TiledMapObjectPropertyProvider.create(
                        tiledMap, /* layerId= */ 0, /* objectId= */ 0),
                    /* x= */ 1,
                    /* y= */ 1,
                    /* width= */ 3,
                    /* height= */ 4)),
            any());
    verify(objectParser)
        .parseObject(
            eq(
                TiledObject.create(
                    EntityType.COLLIDABLE_TILE,
                    TiledMapObjectPropertyProvider.create(
                        tiledMap, /* layerId= */ 0, /* objectId= */ 1),
                    /* x= */ 1,
                    /* y= */ 1,
                    /* width= */ 3,
                    /* height= */ 4)),
            any());
  }

  @Test
  void parseObjectLayer_objectHasNoEntityType_createsObjectWithNoneType() {
    when(tiledMap.getObjectCount(/* groupID= */ 1)).thenReturn(1);
    when(tiledMap.getObjectProperty(/* groupID= */ 1, /* objectID= */ 0, ENTITY_TYPE_PROPERTY))
        .thenReturn(Optional.empty());

    parser.parseObjectLayer(tiledMap, /* layerId= */ 1);

    verify(objectParser)
        .parseObject(
            eq(
                TiledObject.create(
                    EntityType.NONE,
                    TiledMapObjectPropertyProvider.create(
                        tiledMap, /* layerId= */ 1, /* objectId= */ 0),
                    /* x= */ 0,
                    /* y= */ 0,
                    /* width= */ 0,
                    /* height= */ 0)),
            any());
  }
}
