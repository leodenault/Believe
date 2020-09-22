package believe.character.playable;

import believe.character.playable.proto.PlayableCharacterMovementCommandProto;
import believe.command.CommandParser;
import believe.datamodel.MutableValue;
import believe.map.collidable.command.CollidableCommandCollisionHandler;
import believe.map.collidable.tile.CollidableTileCollisionHandler;
import believe.map.io.ObjectParser;
import believe.map.data.EntityType;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.damage.DamageBoxCollisionHandler;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import javax.inject.Singleton;

import java.util.Optional;
import java.util.function.Supplier;

/** Dagger module for bindings on playable entities. */
@Module
public abstract class PlayableDaggerModule {
  @Provides
  @Singleton
  static MutableValue<Optional<PlayableCharacter>> providePlayableCharacterMutableValue() {
    return MutableValue.of(Optional.empty());
  }

  @Binds
  abstract Supplier<Optional<PlayableCharacter>> bindPlayableCharacterSupplier(
      MutableValue<Optional<PlayableCharacter>> mutablePlayableCharacter);

  @Binds
  @IntoSet
  abstract CommandParser<?> bindPlayableCharacterMovementCommandParser(
      PlayableCharacterMovementCommandParser impl);

  @Binds
  @IntoSet
  abstract CollisionHandler<? extends Collidable<?>, ? super PlayableCharacter>
      bindPlayableCharacterCollidableCommandCollisionHandler(
          CollidableCommandCollisionHandler collidableCommandCollisionHandler);

  @Binds
  @IntoSet
  abstract CollisionHandler<? extends Collidable<?>, ? super PlayableCharacter>
      bindPlayableCharacterCollidableTileCollisionHandler(
          CollidableTileCollisionHandler collidableTileCollisionHandler);

  @Binds
  @IntoSet
  abstract CollisionHandler<? extends Collidable<?>, ? super PlayableCharacter>
      bindPlayableCharacterDamageBoxCollisionHandler(
          DamageBoxCollisionHandler damageBoxCollisionHandler);

  @Provides
  @IntoSet
  static CollisionHandler<? extends Collidable<?>, ? super EnemyCharacter>
      provideEnemyCollisionHandler(CollidableTileCollisionHandler collidableTileCollisionHandler) {
    return collidableTileCollisionHandler;
  }

  @Provides
  @IntoSet
  static GeneratedExtension<?, ?> providePlayableCharacterMovementCommandExtension() {
    return PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand
        .playableCharacterMovementCommand;
  }
}
