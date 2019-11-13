package believe.map.io;

import believe.gui.ImageSupplier;
import believe.map.data.BackgroundSceneData;
import believe.map.data.proto.MapMetadataProto.MapBackground;
import javax.inject.Inject;

import java.util.Optional;

final class BackgroundSceneParserImpl implements BackgroundSceneParser {
  private final ImageSupplier imageSupplier;

  @Inject
  BackgroundSceneParserImpl(ImageSupplier imageSupplier) {
    this.imageSupplier = imageSupplier;
  }

  @Override
  public Optional<BackgroundSceneData> parse(MapBackground mapBackground) {
    return imageSupplier
        .get(mapBackground.getFileLocation())
        .map(image -> BackgroundSceneData.create(image, mapBackground));
  }
}
