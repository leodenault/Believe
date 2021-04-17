package believe.map.io;

import static believe.map.tiled.testing.TiledFakes.fakeLayer;
import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static com.google.common.truth.Truth.assertThat;

import believe.map.data.LayerData;
import believe.map.data.TileData;
import believe.map.tiled.Layer;
import believe.physics.manager.PhysicsManageable;
import believe.physics.manager.PhysicsManager;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

/** Unit tests for {@link TiledMapLayerParserImpl}. */
final class TiledMapLayerParserImplTest {
  private static final class FakePhysicsManageable implements PhysicsManageable {
    final List<TileData> tiles = new ArrayList<>();

    @Override
    public void addToPhysicsManager(PhysicsManager physicsManager) {}
  }

  private static final String IS_FRONT_PROPERTY = "isFront";
  private static final String IS_VISIBLE_PROPERTY = "isVisible";

  private TiledMapLayerParserImpl parser =
      new TiledMapLayerParserImpl(IS_FRONT_PROPERTY, IS_VISIBLE_PROPERTY);

  @Test
  void parseLayer_propertiesDoNotExist_returnsLayerWithDefaults() {
    Layer layer = fakeLayer();
    assertThat(parser.parseLayer(layer))
        .isEqualTo(
            LayerData.newBuilder(layer)
                .setIsFrontLayer(/* isFrontLayer= */ false)
                .setIsVisible(/* isVisible= */ true)
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
                .build());

    assertThat(parser.parseLayer(layer2))
        .isEqualTo(
            LayerData.newBuilder(layer2)
                .setIsFrontLayer(/* isFrontLayer= */ false)
                .setIsVisible(/* isVisible= */ false)
                .build());
  }
}
