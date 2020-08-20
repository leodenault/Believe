package believe.map.io;

import believe.map.data.EntityType;
import believe.map.data.GeneratedMapEntityData;
import believe.map.data.LayerData;
import believe.map.data.TileData;
import believe.map.io.InternalQualifiers.EntityTypeProperty;
import believe.map.io.InternalQualifiers.IsFrontLayerProperty;
import believe.map.io.InternalQualifiers.IsVisibleProperty;
import believe.map.tiled.Layer;
import believe.map.tiled.Tile;
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
  private final String entityTypeProperty;
  private final Set<TileParser> tileParsers;

  @Inject
  TiledMapLayerParserImpl(
      @IsFrontLayerProperty String isFrontLayerProperty,
      @IsVisibleProperty String isVisibleProperty,
      @EntityTypeProperty String entityTypeProperty,
      Set<TileParser> tileParsers) {
    this.isFrontLayerProperty = isFrontLayerProperty;
    this.isVisibleProperty = isVisibleProperty;
    this.entityTypeProperty = entityTypeProperty;
    this.tileParsers = tileParsers;
  }

  @Override
  public LayerData parseLayer(Layer layer) {
    LayerData.Builder builder =
        LayerData.newBuilder(layer)
            .setIsFrontLayer(parseBoolean(isFrontLayerProperty, layer).orElse(false))
            .setIsVisible(parseBoolean(isVisibleProperty, layer).orElse(true));

    GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
        GeneratedMapEntityData.newBuilder();
    for (Tile tile : layer.getTiles()) {
      @Nullable String entityType = tile.getProperty(entityTypeProperty);
      TileData tileData =
          TileData.create(
              tile, entityType == null ? EntityType.NONE : EntityType.forName(entityType));
      for (TileParser tileParser : tileParsers) {
        tileParser.parseTile(tileData, generatedMapEntityDataBuilder);
      }
    }

    return builder.setGeneratedMapEntityData(generatedMapEntityDataBuilder.build()).build();
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
