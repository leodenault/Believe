package believe.map.io;

import believe.map.io.InternalQualifiers.EntityTypeProperty;
import believe.map.io.InternalQualifiers.IsFrontLayerProperty;
import believe.map.io.InternalQualifiers.IsVisibleProperty;
import believe.map.io.InternalQualifiers.MapDefinitionsDirectory;
import believe.map.io.InternalQualifiers.PlayerStartXProperty;
import believe.map.io.InternalQualifiers.PlayerStartYProperty;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.Multibinds;

import java.util.Set;

/** Module containing bindings for map parsers. */
@Module
public abstract class MapParsingDaggerModule {
  @Provides
  @MapDefinitionsDirectory
  static String provideMapDefinitionsDirectory() {
    return "/res/maps";
  }

  @Provides
  @IsFrontLayerProperty
  static String providesIsFrontLayerProperty() {
    return "is_front_layer";
  }

  @Provides
  @IsVisibleProperty
  static String providesIsVisibleProperty() {
    return "is_visible";
  }

  @Provides
  @EntityTypeProperty
  static String providesEntityTypeProperty() {
    return "entity_type";
  }

  @Provides
  @PlayerStartXProperty
  static String providePlayerStartXProperty() {
    return "player_start_x";
  }

  @Provides
  @PlayerStartYProperty
  static String providePlayerStartYProperty() {
    return "player_start_y";
  }

  @Multibinds
  abstract Set<TileParser> bindTileParsers();

  @Multibinds
  abstract Set<ObjectParser> bindObjectParsers();

  @Binds
  abstract TiledMapLayerParser bindTiledMapLayerParser(
      TiledMapLayerParserImpl tiledMapLayerParserImpl);

  @Binds
  abstract TiledMapObjectLayerParser bindTiledMapObjectLayerParser(
      TiledMapObjectLayerParserImpl tiledMapObjectLayerParserImpl);

  @Binds
  abstract BackgroundSceneParser bindBackgroundSceneParser(
      BackgroundSceneParserImpl backgroundSceneParser);

  @Binds
  abstract MapMetadataParser bindTiledMapParser(MapMetadataParserImpl tiledMapParserImpl);

  @Provides
  static TiledMapParser provideTiledMapParser(
      @PlayerStartXProperty String playerStartXProperty,
      @PlayerStartYProperty String playerStartYProperty,
      TiledMapLayerParser tiledMapLayerParser,
      TiledMapObjectLayerParser tiledMapObjectLayerParser) {
    return TiledMapParser.create(
        playerStartXProperty, playerStartYProperty, tiledMapLayerParser, tiledMapObjectLayerParser);
  }
}
