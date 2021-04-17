package believe.map.collidable.command;

import believe.map.collidable.command.InternalQualifiers.CommandParameter;
import believe.map.collidable.command.InternalQualifiers.ShouldDespawnParameter;
import believe.map.data.EntityType;
import believe.map.data.EntityTypeKey;
import believe.map.io.ObjectParser;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;

import java.util.Set;

@Module
public abstract class CollidableCommandDaggerModule {
  @Provides
  @CommandParameter
  static String provideCommandParameter() {
    return "command";
  }

  @Provides
  @ShouldDespawnParameter
  static String provideShouldDespawnParameter() {
    return "should_despawn";
  }

  @Binds
  @IntoMap
  @EntityTypeKey(EntityType.COMMAND)
  abstract ObjectParser bindCommandGenerator(CollidableCommandGenerator collidableCommandGenerator);

  @Binds
  @IntoSet
  abstract CollisionHandler<? super CollidableCommand, ? extends Collidable<?>>
      bindCollidableCommandCollisionHandler(
          CollidableCommandCollisionHandler collidableCommandCollisionHandler);
}
