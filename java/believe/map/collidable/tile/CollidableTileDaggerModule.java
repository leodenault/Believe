package believe.map.collidable.tile;

import believe.map.io.TileParser;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

/** Module containing bindings for collidable tiles. */
@Module
public abstract class CollidableTileDaggerModule {
  @Binds
  @IntoSet
  abstract TileParser provideCollidableTileGenerator(
      CollidableTileGenerator collidableTileGenerator);
}
