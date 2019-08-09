package believe.map.io;

import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import believe.map.data.GeneratedMapEntityData;
import believe.map.data.LayerData;
import believe.map.tiled.EntityType;
import believe.map.tiled.Tile;
import believe.map.tiled.TiledMap;
import believe.physics.manager.PhysicsManageable;
import believe.physics.manager.PhysicsManager;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/** Unit tests for {@link TiledMapLayerParserImpl}. */
@InstantiateMocksIn
final class TiledMapLayerParserImplTest {
  private static final class FakePhysicsManageable implements PhysicsManageable {
    final List<Tile> tiles = new ArrayList<>();

    @Override
    public void addToPhysicsManager(PhysicsManager physicsManager) {}
  }

  private static final String IS_FRONT_PROPERTY = "isFront";
  private static final String IS_VISIBLE_PROPERTY = "isVisible";
  private static final String ENTITY_TYPE_PROPERTY = "entityType";

  private final Set<TileParser> tileParsers = hashSetOf();

  private TiledMapLayerParserImpl parser =
      new TiledMapLayerParserImpl(
          IS_FRONT_PROPERTY, IS_VISIBLE_PROPERTY, ENTITY_TYPE_PROPERTY, tileParsers);

  @Mock private TiledMap tiledMap;

  @Test
  void parseLayers_propertiesDoNotExist_returnsLayerWithDefaults() {
    when(tiledMap.getLayerProperty(/* layerIndex= */ 0, IS_VISIBLE_PROPERTY))
        .thenReturn(Optional.empty());
    when(tiledMap.getLayerProperty(/* layerIndex= */ 0, IS_FRONT_PROPERTY))
        .thenReturn(Optional.empty());

    assertThat(parser.parseLayer(tiledMap, /* layerId= */ 0))
        .isEqualTo(
            LayerData.newBuilder(tiledMap, /* layerId= */ 0)
                .setIsFrontLayer(/* isFrontLayer= */ false)
                .setIsVisible(/* isVisible= */ true)
                .setGeneratedMapEntityData(GeneratedMapEntityData.newBuilder().build())
                .build());
  }

  @Test
  void parseLayers_propertiesHaveErrors_returnsLayerWithDefaults() {
    when(tiledMap.getLayerProperty(/* layerIndex= */ 0, IS_FRONT_PROPERTY))
        .thenReturn(Optional.of("987"));
    when(tiledMap.getLayerProperty(/* layerIndex= */ 0, IS_VISIBLE_PROPERTY))
        .thenReturn(Optional.of("nope"));

    assertThat(parser.parseLayer(tiledMap, /* layerId= */ 0))
        .isEqualTo(
            LayerData.newBuilder(tiledMap, /* layerId= */ 0)
                .setIsFrontLayer(/* isFrontLayer= */ false)
                .setIsVisible(/* isVisible= */ true)
                .setGeneratedMapEntityData(GeneratedMapEntityData.newBuilder().build())
                .build());
  }

  @Test
  void parseLayers_propertiesExist_returnsLayerWithValues() {
    when(tiledMap.getLayerProperty(/* layerIndex= */ 0, IS_FRONT_PROPERTY))
        .thenReturn(Optional.of("true"));
    when(tiledMap.getLayerProperty(/* layerIndex= */ 0, IS_VISIBLE_PROPERTY))
        .thenReturn(Optional.of("true"));
    when(tiledMap.getLayerProperty(/* layerIndex= */ 1, IS_FRONT_PROPERTY))
        .thenReturn(Optional.of("false"));
    when(tiledMap.getLayerProperty(/* layerIndex= */ 1, IS_VISIBLE_PROPERTY))
        .thenReturn(Optional.of("false"));

    assertThat(parser.parseLayer(tiledMap, /* layerId= */ 0))
        .isEqualTo(
            LayerData.newBuilder(tiledMap, /* layerId= */ 0)
                .setIsFrontLayer(/* isFrontLayer= */ true)
                .setIsVisible(/* isVisible= */ true)
                .setGeneratedMapEntityData(GeneratedMapEntityData.newBuilder().build())
                .build());

    assertThat(parser.parseLayer(tiledMap, /* layerId= */ 1))
        .isEqualTo(
            LayerData.newBuilder(tiledMap, /* layerId= */ 1)
                .setIsFrontLayer(/* isFrontLayer= */ false)
                .setIsVisible(/* isVisible= */ false)
                .setGeneratedMapEntityData(GeneratedMapEntityData.newBuilder().build())
                .build());
  }

  @Test
  void parseLayers_tilesAreParsed_returnsLayerWithTiles() {
    when(tiledMap.getLayerCount()).thenReturn(1);
    when(tiledMap.getWidth()).thenReturn(2);
    when(tiledMap.getHeight()).thenReturn(2);
    when(tiledMap.getTileWidth()).thenReturn(10);
    when(tiledMap.getTileHeight()).thenReturn(100);
    when(tiledMap.getTileId(anyInt(), anyInt(), anyInt())).thenReturn(1);
    when(tiledMap.getTileProperty(anyInt(), eq(ENTITY_TYPE_PROPERTY)))
        .thenReturn(Optional.of(EntityType.ENEMY.name()));
    FakePhysicsManageable physicsManageable = new FakePhysicsManageable();
    tileParsers.add(createTileParser(physicsManageable));

    LayerData layerData = parser.parseLayer(tiledMap, /* layerId= */ 0);

    assertThat(layerData.generatedMapEntityData().physicsManageables())
        .containsExactly(physicsManageable);
    assertThat(physicsManageable.tiles.get(0).entityType()).isEqualTo(EntityType.ENEMY);
    assertThat(physicsManageable.tiles.get(0).tileX()).isEqualTo(0);
    assertThat(physicsManageable.tiles.get(0).pixelX()).isEqualTo(0);
    assertThat(physicsManageable.tiles.get(0).tileY()).isEqualTo(0);
    assertThat(physicsManageable.tiles.get(0).pixelY()).isEqualTo(0);
    assertThat(physicsManageable.tiles.get(0).width()).isEqualTo(10);
    assertThat(physicsManageable.tiles.get(0).height()).isEqualTo(100);
    assertThat(physicsManageable.tiles.get(0).layerId()).isEqualTo(0);
    assertThat(physicsManageable.tiles.get(3).entityType()).isEqualTo(EntityType.ENEMY);
    assertThat(physicsManageable.tiles.get(3).tileX()).isEqualTo(1);
    assertThat(physicsManageable.tiles.get(3).pixelX()).isEqualTo(10);
    assertThat(physicsManageable.tiles.get(3).tileY()).isEqualTo(1);
    assertThat(physicsManageable.tiles.get(3).pixelY()).isEqualTo(100);
    assertThat(physicsManageable.tiles.get(3).width()).isEqualTo(10);
    assertThat(physicsManageable.tiles.get(3).height()).isEqualTo(100);
    assertThat(physicsManageable.tiles.get(3).layerId()).isEqualTo(0);
  }

  @Test
  void parseLayers_tileHasNoEntityType_createsTileDataWithNoneType() {
    when(tiledMap.getLayerCount()).thenReturn(1);
    when(tiledMap.getWidth()).thenReturn(1);
    when(tiledMap.getHeight()).thenReturn(1);
    when(tiledMap.getTileId(anyInt(), anyInt(), anyInt())).thenReturn(1);
    when(tiledMap.getTileProperty(anyInt(), eq(ENTITY_TYPE_PROPERTY))).thenReturn(Optional.empty());
    FakePhysicsManageable physicsManageable = new FakePhysicsManageable();
    tileParsers.add(createTileParser(physicsManageable));

    parser.parseLayer(tiledMap, /* layerId= */ 0);

    assertThat(physicsManageable.tiles.get(0).entityType()).isEqualTo(EntityType.NONE);
  }

  private static TileParser createTileParser(FakePhysicsManageable fakePhysicsManageable) {
    return (map, tile, generatedMapEntityDataBuilder) -> {
      fakePhysicsManageable.tiles.add(tile);
      generatedMapEntityDataBuilder.addPhysicsManageable(fakePhysicsManageable);
    };
  }
}
