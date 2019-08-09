package believe.map.io;

import believe.map.io.InternalQualifiers.EntityTypeProperty;
import believe.map.io.InternalQualifiers.IsFrontLayerProperty;
import believe.map.io.InternalQualifiers.IsVisibleProperty;
import believe.map.io.InternalQualifiers.MapDefinitionsFile;
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
  @MapDefinitionsFile
  static String provideMapDefinitionsFile() {
    return "/data/maps.xml";
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

  @Binds
  abstract TiledMapLayerParser bindTiledMapLayerParser(
      TiledMapLayerParserImpl tiledMapLayerParserImpl);

  @Binds
  abstract MapManager bindMapManager(MapManagerImpl mapManagerImpl);
}
