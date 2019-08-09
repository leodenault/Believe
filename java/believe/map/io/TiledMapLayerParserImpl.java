package believe.map.io;

import believe.map.data.GeneratedMapEntityData;
import believe.map.data.LayerData;
import believe.map.io.InternalQualifiers.EntityTypeProperty;
import believe.map.io.InternalQualifiers.IsFrontLayerProperty;
import believe.map.io.InternalQualifiers.IsVisibleProperty;
import believe.map.tiled.EntityType;
import believe.map.tiled.Tile;
import believe.map.tiled.TiledMap;
import dagger.Reusable;
import javax.inject.Inject;

import java.util.Optional;
import java.util.Set;

/**
 * Parser for interpreting a {@link believe.map.tiled.Layer} into {@link LayerData} usable in a
 * Believe application.
 */
@Reusable
final class TiledMapLayerParserImpl implements TiledMapLayerParser {
  private static final int EMPTY_TILE = 0;

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
  public LayerData parseLayer(TiledMap tiledMap, int layerId) {
    LayerData.Builder builder =
        LayerData.newBuilder(tiledMap, layerId)
            .setIsFrontLayer(parseBoolean(tiledMap, isFrontLayerProperty, layerId).orElse(false))
            .setIsVisible(parseBoolean(tiledMap, isVisibleProperty, layerId).orElse(true));

    GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
        GeneratedMapEntityData.newBuilder();
    int mapWidth = tiledMap.getWidth();
    int mapHeight = tiledMap.getHeight();
    int tileWidth = tiledMap.getTileWidth();
    int tileHeight = tiledMap.getTileHeight();
    for (int layer = 0; layer < tiledMap.getLayerCount(); layer++) {
      for (int x = 0; x < mapWidth; x++) {
        for (int y = 0; y < mapHeight; y++) {
          int tileId = tiledMap.getTileId(x, y, layer);
          if (tileId != EMPTY_TILE) {
            Optional<String> entityType = tiledMap.getTileProperty(tileId, entityTypeProperty);
            Tile tile =
                Tile.create(
                    tiledMap,
                    entityType.isPresent() ? EntityType.forName(entityType.get()) : EntityType.NONE,
                    tileId,
                    x,
                    y,
                    tileWidth,
                    tileHeight,
                    layer);
            for (TileParser tileParser : tileParsers) {
              tileParser.parseTile(tiledMap, tile, generatedMapEntityDataBuilder);
            }
          }
        }
      }
    }

    return builder.setGeneratedMapEntityData(generatedMapEntityDataBuilder.build()).build();
  }

  private static Optional<Boolean> parseBoolean(TiledMap map, String property, int layer) {
    Optional<String> propertyValue = map.getLayerProperty(layer, property);
    if (propertyValue.isPresent()) {
      switch (propertyValue.get().toLowerCase()) {
        case "true":
          return Optional.of(true);
        case "false":
          return Optional.of(false);
      }
    }
    return Optional.empty();
  }
}
