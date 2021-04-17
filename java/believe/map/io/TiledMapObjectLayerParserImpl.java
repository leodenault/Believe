package believe.map.io;

import believe.map.data.ObjectLayerData;
import believe.map.data.EntityType;
import believe.map.tiled.TiledObject;
import believe.map.tiled.TiledObjectGroup;
import dagger.Reusable;
import javax.inject.Inject;

import java.util.Map;
import java.util.Set;

@Reusable
final class TiledMapObjectLayerParserImpl implements TiledMapObjectLayerParser {
  private final Map<EntityType, ObjectParser> objectParsers;

  @Inject
  TiledMapObjectLayerParserImpl(Map<EntityType, ObjectParser> objectParsers) {
    this.objectParsers = objectParsers;
  }

  @Override
  public ObjectLayerData parseObjectGroup(TiledObjectGroup tiledObjectGroup) {
    ObjectLayerData.Builder objectLayerDataBuilder = ObjectLayerData.newBuilder();

    for (TiledObject tiledObject : tiledObjectGroup.getObjects()) {
      EntityType entityType =
          tiledObject.getType() == null
              ? EntityType.NONE
              : EntityType.forName(tiledObject.getType());
      ObjectParser parser = objectParsers.get(entityType);
      if (parser != null) {
        objectLayerDataBuilder.addObjectFactory(parser.parseObject(tiledObject));
      }
    }
    return objectLayerDataBuilder.build();
  }
}
