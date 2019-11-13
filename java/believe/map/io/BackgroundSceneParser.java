package believe.map.io;

import believe.map.data.BackgroundSceneData;
import believe.map.data.proto.MapMetadataProto.MapBackground;

import java.util.Optional;

/** Loads {@link believe.map.data.BackgroundSceneData} from disk. */
public interface BackgroundSceneParser {
  /** Attempts to load a {@link BackgroundSceneData} from {@code mapBackground}. */
  Optional<BackgroundSceneData> parse(MapBackground mapBackground);
}
