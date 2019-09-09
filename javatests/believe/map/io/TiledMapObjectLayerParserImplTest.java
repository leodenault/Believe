package believe.map.io;

import static believe.util.Util.hashSetOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import believe.map.tiled.EntityType;
import believe.map.tiled.TiledMap;
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

  @Test
  void parseObjectLayer_returnsValidObjectLayerData() {
    when(tiledMap.getObjectCount(/* groupID= */ 0)).thenReturn(2);
    when(tiledMap.getObjectProperty(
            /* groupID= */ eq(0), /* objectID= */ anyInt(), eq(ENTITY_TYPE_PROPERTY)))
        .thenReturn(Optional.of(EntityType.COLLIDABLE_TILE.name()));
    when(tiledMap.getObjectX(/* groupID= */ eq(0), anyInt())).thenReturn(1);
    when(tiledMap.getObjectY(/* groupID= */ eq(0), anyInt())).thenReturn(2);
    when(tiledMap.getObjectWidth(/* groupID= */ eq(0), anyInt())).thenReturn(3);
    when(tiledMap.getObjectHeight(/* groupID= */ eq(0), anyInt())).thenReturn(4);

    parser.parseObjectLayer(tiledMap, /* layerId= */ 0);

    verify(objectParser)
        .parseObject(
            eq(
                TiledObject.create(
                    tiledMap,
                    EntityType.COLLIDABLE_TILE,
                    /* x= */ 1,
                    /* y= */ 2,
                    /* width= */ 3,
                    /* height= */ 4,
                    /* layerId= */ 0,
                    /* objectId= */ 0)),
            any());
    verify(objectParser)
        .parseObject(
            eq(
                TiledObject.create(
                    tiledMap,
                    EntityType.COLLIDABLE_TILE,
                    /* x= */ 1,
                    /* y= */ 2,
                    /* width= */ 3,
                    /* height= */ 4,
                    /* layerId= */ 0,
                    /* objectId= */ 1)),
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
                    tiledMap,
                    EntityType.NONE,
                    /* x= */ 0,
                    /* y= */ 0,
                    /* width= */ 0,
                    /* height= */ 0,
                    /* layerId= */ 1,
                    /* objectId= */ 0)),
            any());
  }
}
