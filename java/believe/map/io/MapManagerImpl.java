package believe.map.io;

import believe.datamodel.protodata.MutableProtoDataCommitter;
import believe.map.data.MapData;
import believe.map.data.proto.MapMetadataProto.MapMetadata;
import believe.map.io.InternalQualifiers.MapDefinitionsDirectory;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Default implementation of {@link MapManager}. */
@Singleton
public class MapManagerImpl implements MapManager {
  private final MapMetadataParser mapMetadataParser;
  private final String mapDefinitionsDirectory;
  private final Map<String, MapData> nameToMapDataMap;

  @Inject
  MapManagerImpl(
      MapMetadataParser mapMetadataParser,
      @MapDefinitionsDirectory String mapDefinitionsDirectory) {
    this.mapMetadataParser = mapMetadataParser;
    this.mapDefinitionsDirectory = mapDefinitionsDirectory;
    this.nameToMapDataMap = new HashMap<>();
  }

  @Override
  public Optional<MapData> getMap(String name) {
    MapData mapData = nameToMapDataMap.get(name);
    if (mapData != null) {
      return Optional.of(mapData);
    }

    String metadataLocation = mapDefinitionsDirectory + "/" + name + ".pb";
    MutableProtoDataCommitter<MapMetadata> metadataLoader =
        new MutableProtoDataCommitter<>(
            metadataLocation, MapMetadata.parser(), MapMetadata.getDefaultInstance());
    metadataLoader.load();

    Optional<MapData> parsedMapData = mapMetadataParser.parse(metadataLoader.get());
    if (!parsedMapData.isPresent()) {
      Log.error("Failed to read map metadata at '" + metadataLocation + "'.");
      return Optional.empty();
    }

    nameToMapDataMap.put(name, parsedMapData.get());
    return parsedMapData;
  }
}
