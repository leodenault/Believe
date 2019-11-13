package believe.map.io;

import believe.map.data.MapData;
import believe.map.data.proto.MapMetadataProto.MapMetadata;

import java.util.Optional;

/** Parser for extracting data out of a Tiled map. */
public interface MapMetadataParser {
  /** Parses a map based on the contents of {@code mapMetadata}. */
  Optional<MapData> parse(MapMetadata mapMetadata);
}
