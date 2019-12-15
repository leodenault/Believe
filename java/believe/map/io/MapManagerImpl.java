package believe.map.io;

import believe.datamodel.protodata.BinaryProtoFile;
import believe.datamodel.protodata.BinaryProtoFile.BinaryProtoFileFactory;
import believe.map.data.MapData;
import believe.map.data.proto.MapMetadataProto.MapMetadata;
import believe.map.io.InternalQualifiers.MapDefinitionsDirectory;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.newdawn.slick.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Default implementation of {@link MapManager}. */
@Singleton
public class MapManagerImpl implements MapManager {
  private final MapMetadataParser mapMetadataParser;
  private final BinaryProtoFileFactory binaryProtoFileFactory;
  private final String mapDefinitionsDirectory;
  private final Map<String, MapData> nameToMapDataMap;

  @Inject
  MapManagerImpl(
      MapMetadataParser mapMetadataParser,
      BinaryProtoFileFactory binaryProtoFileFactory,
      @MapDefinitionsDirectory String mapDefinitionsDirectory) {
    this.mapMetadataParser = mapMetadataParser;
    this.binaryProtoFileFactory = binaryProtoFileFactory;
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
    BinaryProtoFile<MapMetadata> mapMetadataFile =
        binaryProtoFileFactory.create(metadataLocation, MapMetadata.parser());
    MapMetadata mapMetadata = mapMetadataFile.load();

    if (mapMetadata == null) {
      Log.error("Failed to read map metadata at '" + metadataLocation + "'.");
      return Optional.empty();
    }

    Optional<MapData> parsedMapData = mapMetadataParser.parse(mapMetadata);
    if (!parsedMapData.isPresent()) {
      Log.error(
          "Failed to parse map metadata contents at '"
              + metadataLocation
              + "' into valid MapData instance.");
      return Optional.empty();
    }

    nameToMapDataMap.put(name, parsedMapData.get());
    return parsedMapData;
  }
}
