package believe.map.collidable.tile;

import believe.map.data.EntityType;
import believe.map.data.EntityTypeKey;
import believe.map.io.ObjectParser;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/** Module containing bindings for collidable tiles. */
@Module
public abstract class CollidableTileDaggerModule {
  @Binds
  @IntoMap
  @EntityTypeKey(EntityType.COLLIDABLE_TILE)
  abstract ObjectParser provideCollidableTileParser(CollidableTileParser collidableTileParser);
}
