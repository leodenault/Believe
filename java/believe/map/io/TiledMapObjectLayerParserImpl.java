package believe.map.io;

import believe.map.data.GeneratedMapEntityData;
import believe.map.data.ObjectLayerData;
import believe.map.data.EntityType;
import believe.map.tiled.TiledObject;
import believe.map.tiled.TiledObjectGroup;
import dagger.Reusable;
import javax.inject.Inject;

import java.util.Set;

@Reusable
final class TiledMapObjectLayerParserImpl implements TiledMapObjectLayerParser {
  private final Set<ObjectParser> objectParsers;

  @Inject
  TiledMapObjectLayerParserImpl(Set<ObjectParser> objectParsers) {
    this.objectParsers = objectParsers;
  }

  @Override
  public ObjectLayerData parseObjectGroup(TiledObjectGroup tiledObjectGroup) {
    GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
        GeneratedMapEntityData.newBuilder();

    for (TiledObject tiledObject : tiledObjectGroup.getObjects()) {
      EntityType entityType =
          tiledObject.getType() == null
              ? EntityType.NONE
              : EntityType.forName(tiledObject.getType());
      for (ObjectParser objectParser : objectParsers) {
        objectParser.parseObject(entityType, tiledObject, generatedMapEntityDataBuilder);
      }
    }
    return ObjectLayerData.create(generatedMapEntityDataBuilder.build());
  }
}
