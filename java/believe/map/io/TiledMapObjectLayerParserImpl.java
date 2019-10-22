package believe.map.io;

import believe.map.data.GeneratedMapEntityData;
import believe.map.data.ObjectLayerData;
import believe.map.io.InternalQualifiers.EntityTypeProperty;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledMapObjectPropertyProvider;
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
        int objectHeight = tiledMap.getObjectHeight(layerId, objectId);
        int finalObjectId = objectId;
        objectParser.parseObject(
            TiledObject.create(
                entityType,
                TiledMapObjectPropertyProvider.create(tiledMap, layerId, objectId),
                tiledMap.getObjectX(layerId, objectId),
                tiledMap.getObjectY(layerId, objectId) - objectHeight,
                tiledMap.getObjectWidth(layerId, objectId),
                objectHeight),
            generatedMapEntityDataBuilder);
      }
    }
    return ObjectLayerData.create(generatedMapEntityDataBuilder.build());
  }
}
