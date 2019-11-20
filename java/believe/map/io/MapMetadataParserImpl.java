package believe.map.io;

import believe.map.data.MapData;
import believe.map.data.proto.MapMetadataProto.MapBackground;
import believe.map.data.proto.MapMetadataProto.MapMetadata;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledMapFactory;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import java.util.Optional;

@Reusable
public final class MapMetadataParserImpl implements MapMetadataParser {
  interface TiledMapProvider {
    TiledMap provideTiledMap(String mapLocation, String tileSetsLocation);
  }

  private final TiledMapProvider tiledMapProvider;
  private final TiledMapParser tiledMapParser;
  private final BackgroundSceneParser backgroundSceneParser;

  @Inject
  MapMetadataParserImpl(
      TiledMapFactory tiledMapFactory,
      TiledMapParser tiledMapParser,
      BackgroundSceneParser backgroundSceneParser) {
    this(tiledMapFactory::create, tiledMapParser, backgroundSceneParser);
  }

  MapMetadataParserImpl(
      TiledMapProvider tiledMapProvider,
      TiledMapParser tiledMapParser,
      BackgroundSceneParser backgroundSceneParser) {
    this.tiledMapProvider = tiledMapProvider;
    this.tiledMapParser = tiledMapParser;
    this.backgroundSceneParser = backgroundSceneParser;
  }

  @Override
  public Optional<MapData> parse(MapMetadata mapMetadata) {
    TiledMap tiledMap =
        tiledMapProvider.provideTiledMap(
            mapMetadata.getMapLocation(), mapMetadata.getTileSetsLocation());

    try {
      tiledMap.load();
    } catch (SlickException e) {
      Log.error("Failed to load Tiled map.", e);
      return Optional.empty();
    }

    MapData.Builder mapData = MapData.newBuilder(tiledMapParser.parse(tiledMap));
    for (MapBackground mapBackground : mapMetadata.getMapBackgroundsList()) {
      backgroundSceneParser.parse(mapBackground).ifPresent(mapData::addBackgroundScene);
    }

    return Optional.of(mapData.build());
  }
}
