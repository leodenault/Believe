package believe.map.io;

import static believe.map.tiled.testing.TiledFakes.fakeLayer;
import static believe.map.tiled.testing.TiledFakes.fakeTile;
import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

import believe.map.data.EntityType;
import believe.map.data.GeneratedMapEntityData;
import believe.map.data.LayerData;
import believe.map.data.TileData;
import believe.map.tiled.Layer;
import believe.physics.manager.PhysicsManageable;
import believe.physics.manager.PhysicsManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/** Unit tests for {@link TiledMapLayerParserImpl}. */
final class TiledMapLayerParserImplTest {
  private static final class FakePhysicsManageable implements PhysicsManageable {
    final List<TileData> tiles = new ArrayList<>();

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

  @Test
  void parseLayer_propertiesDoNotExist_returnsLayerWithDefaults() {
    Layer layer = fakeLayer();
    assertThat(parser.parseLayer(layer))
        .isEqualTo(
            LayerData.newBuilder(layer)
                .setIsFrontLayer(/* isFrontLayer= */ false)
                .setIsVisible(/* isVisible= */ true)
                .setGeneratedMapEntityData(GeneratedMapEntityData.newBuilder().build())
                .build());
  }

  @Test
  void parseLayer_propertiesHaveErrors_returnsLayerWithDefaults() {
    Layer layer =
        fakeLayer(hashMapOf(entry(IS_FRONT_PROPERTY, "987"), entry(IS_VISIBLE_PROPERTY, "nope")));
    assertThat(parser.parseLayer(layer))
        .isEqualTo(
            LayerData.newBuilder(layer)
                .setIsFrontLayer(/* isFrontLayer= */ false)
                .setIsVisible(/* isVisible= */ true)
                .setGeneratedMapEntityData(GeneratedMapEntityData.newBuilder().build())
                .build());
  }

  @Test
  void parseLayer_propertiesExist_returnsLayerWithValues() {
    Layer layer1 =
        fakeLayer(hashMapOf(entry(IS_FRONT_PROPERTY, "true"), entry(IS_VISIBLE_PROPERTY, "true")));
    Layer layer2 =
        fakeLayer(
            hashMapOf(entry(IS_FRONT_PROPERTY, "false"), entry(IS_VISIBLE_PROPERTY, "false")));

    assertThat(parser.parseLayer(layer1))
        .isEqualTo(
            LayerData.newBuilder(layer1)
                .setIsFrontLayer(/* isFrontLayer= */ true)
                .setIsVisible(/* isVisible= */ true)
                .setGeneratedMapEntityData(GeneratedMapEntityData.newBuilder().build())
                .build());

    assertThat(parser.parseLayer(layer2))
        .isEqualTo(
            LayerData.newBuilder(layer2)
                .setIsFrontLayer(/* isFrontLayer= */ false)
                .setIsVisible(/* isVisible= */ false)
                .setGeneratedMapEntityData(GeneratedMapEntityData.newBuilder().build())
                .build());
  }
}
