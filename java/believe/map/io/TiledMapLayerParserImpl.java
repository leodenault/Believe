package believe.map.io;

import believe.map.data.LayerData;
import believe.map.io.InternalQualifiers.EntityTypeProperty;
import believe.map.io.InternalQualifiers.IsFrontLayerProperty;
import believe.map.io.InternalQualifiers.IsVisibleProperty;
import believe.map.tiled.Layer;
import dagger.Reusable;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.Optional;
import java.util.Set;

/**
 * Parser for interpreting a {@link believe.map.tiled.Layer} into {@link LayerData} usable in a
 * Believe application.
 */
@Reusable
final class TiledMapLayerParserImpl implements TiledMapLayerParser {
  private final String isFrontLayerProperty;
  private final String isVisibleProperty;

  @Inject
  TiledMapLayerParserImpl(
      @IsFrontLayerProperty String isFrontLayerProperty,
      @IsVisibleProperty String isVisibleProperty) {
    this.isFrontLayerProperty = isFrontLayerProperty;
    this.isVisibleProperty = isVisibleProperty;
  }

  @Override
  public LayerData parseLayer(Layer layer) {
    return LayerData.newBuilder(layer)
        .setIsFrontLayer(parseBoolean(isFrontLayerProperty, layer).orElse(false))
        .setIsVisible(parseBoolean(isVisibleProperty, layer).orElse(true))
        .build();
  }

  private static Optional<Boolean> parseBoolean(String property, Layer layer) {
    @Nullable String propertyValue = layer.getProperty(property);
    if (propertyValue != null) {
      switch (propertyValue.toLowerCase()) {
        case "true":
          return Optional.of(true);
        case "false":
          return Optional.of(false);
      }
    }
    return Optional.empty();
  }
}
