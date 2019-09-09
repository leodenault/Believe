package believe.map.io;

import believe.map.data.GeneratedMapEntityData;
import believe.map.data.ObjectLayerData;
import believe.map.io.InternalQualifiers.EntityTypeProperty;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledObject;
import dagger.Reusable;
import javax.inject.Inject;

import java.util.Optional;
import java.util.Set;

@Reusable
final class TiledMapObjectLayerParserImpl implements TiledMapObjectLayerParser {
  private final String entityTypeProperty;
  private final Set<ObjectParser> objectParsers;

  @Inject
  TiledMapObjectLayerParserImpl(
      @EntityTypeProperty String entityTypeProperty, Set<ObjectParser> objectParsers) {
    this.entityTypeProperty = entityTypeProperty;
    this.objectParsers = objectParsers;
  }

  @Override
  public ObjectLayerData parseObjectLayer(TiledMap tiledMap, int layerId) {
    GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
        GeneratedMapEntityData.newBuilder();

    for (int objectId = 0; objectId < tiledMap.getObjectCount(layerId); objectId++) {
      Optional<String> entityTypeName =
          tiledMap.getObjectProperty(layerId, objectId, entityTypeProperty);
      EntityType entityType = entityTypeName.map(EntityType::forName).orElse(EntityType.NONE);
      for (ObjectParser objectParser : objectParsers) {
        objectParser.parseObject(
            TiledObject.create(
                tiledMap,
                entityType,
                tiledMap.getObjectX(layerId, objectId),
                tiledMap.getObjectY(layerId, objectId),
                tiledMap.getObjectWidth(layerId, objectId),
                tiledMap.getObjectHeight(layerId, objectId),
                layerId,
                objectId),
            generatedMapEntityDataBuilder);
      }
    }
    return ObjectLayerData.create(generatedMapEntityDataBuilder.build());
  }
}
