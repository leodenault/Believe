package believe.map.io;

import believe.datamodel.protodata.MutableProtoDataCommitter;
import believe.gui.ImageSupplier;
import believe.map.data.BackgroundSceneData;
import believe.map.data.MapData;
import believe.map.data.proto.MapMetadataProto.MapBackground;
import believe.map.data.proto.MapMetadataProto.MapMetadata;
import believe.map.io.InternalQualifiers.MapDefinitionsDirectory;
import believe.map.tiled.TiledMap;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** Default implementation of {@link MapManager}. */
@Singleton
public class MapManagerImpl implements MapManager {
  private static final class MapBackgroundAndImage {
    final MapBackground mapBackground;
    @Nullable final Image image;

    MapBackgroundAndImage(MapBackground mapBackground, @Nullable Image image) {
      this.mapBackground = mapBackground;
      this.image = image;
    }
  }

  private final TiledMapParser tiledMapParser;
  private final String mapDefinitionsDirectory;
  private final ImageSupplier imageSupplier;
  private final Map<String, MapData> nameToMapDataMap;

  @Inject
  MapManagerImpl(
      TiledMapParser tiledMapParser, @MapDefinitionsDirectory String mapDefinitionsDirectory) {
    this(
        tiledMapParser,
        mapDefinitionsDirectory,
        ref -> {
          try {
            return Optional.of(new Image(ref));
          } catch (SlickException e) {
            return Optional.empty();
          }
        });
  }

  MapManagerImpl(
      TiledMapParser tiledMapParser, String mapDefinitionsDirectory, ImageSupplier imageSupplier) {
    this.tiledMapParser = tiledMapParser;
    this.mapDefinitionsDirectory = mapDefinitionsDirectory;
    this.imageSupplier = imageSupplier;
    this.nameToMapDataMap = new HashMap<>();
  }

  @Override
  public Optional<MapData> getMap(String name) throws SlickException {
    MapData mapData = nameToMapDataMap.get(name);
    if (mapData != null) {
      return Optional.of(mapData);
    }

    String mapPrefix = mapDefinitionsDirectory + "/" + name;
    String tileMapLocation = mapPrefix + ".tmx";
    String metadataLocation = mapPrefix + ".pb";
    String tileSetsLocation = mapDefinitionsDirectory + "/tilesets";

    if (!ResourceLoader.resourceExists(tileMapLocation)) {
      Log.error("Could not find map '" + tileMapLocation + "'.");
      return Optional.empty();
    }

    TiledMap tiledMap = new TiledMap(tileMapLocation, tileSetsLocation);
    MutableProtoDataCommitter<MapMetadata> metadataLoader =
        new MutableProtoDataCommitter<>(
            metadataLocation, MapMetadata.parser(), MapMetadata.getDefaultInstance());

    tiledMap.load();
    metadataLoader.load();

    MapMetadata mapMetadata = metadataLoader.get();
    List<BackgroundSceneData> backgroundSceneData =
        mapMetadata.getMapBackgroundsList().stream()
            .map(
                mapBackground ->
                    new MapBackgroundAndImage(
                        mapBackground,
                        imageSupplier.get(mapBackground.getFileLocation()).orElse(null)))
            .filter(mapBackgroundAndImage -> mapBackgroundAndImage.image != null)
            .map(
                mapBackgroundAndImage ->
                    BackgroundSceneData.create(
                        mapBackgroundAndImage.image, mapBackgroundAndImage.mapBackground))
            .collect(Collectors.toList());

    mapData = tiledMapParser.parseMap(tiledMap, backgroundSceneData);
    nameToMapDataMap.put(name, mapData);
    return Optional.of(mapData);
  }
}
