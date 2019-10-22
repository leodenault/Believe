package believe.character.playable;

import believe.character.playable.InternalQualifiers.JumpMovementCommand;
import believe.character.playable.InternalQualifiers.LeftMovementCommand;
import believe.character.playable.InternalQualifiers.RightMovementCommand;
import believe.character.playable.InternalQualifiers.StopMovementCommand;
import believe.command.CommandSupplier;
import believe.datamodel.MutableValue;
import believe.map.collidable.command.CollidableCommandCollisionHandler;
import believe.map.collidable.tile.CollidableTileCollisionHandler;
import believe.map.io.ObjectParser;
import believe.map.tiled.EntityType;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.damage.DamageBoxCollisionHandler;
import believe.statemachine.State.Action;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import dagger.multibindings.StringKey;
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

  @Provides
  @Reusable
  @RightMovementCommand
  static PlayableCharacterMovementCommand provideRightMovementCommand(
      PlayableCharacterMovementCommandFactory factory) {
    return factory.create(Action.SELECT_RIGHT);
  }

  @Provides
  @Reusable
  @LeftMovementCommand
  static PlayableCharacterMovementCommand provideLeftMovementCommand(
      PlayableCharacterMovementCommandFactory factory) {
    return factory.create(Action.SELECT_LEFT);
  }

  @Provides
  @Reusable
  @JumpMovementCommand
  static PlayableCharacterMovementCommand provideJumpMovementCommand(
      PlayableCharacterMovementCommandFactory factory) {
    return factory.create(Action.JUMP);
  }

  @Provides
  @Reusable
  @StopMovementCommand
  static PlayableCharacterMovementCommand provideStopMovementCommand(
      PlayableCharacterMovementCommandFactory factory) {
    return factory.create(Action.STOP);
  }

  @Binds
  @IntoMap
  @StringKey("right")
  abstract CommandSupplier bindRightMovementCommandSupplier(
      @RightMovementCommand PlayableCharacterMovementCommand command);

  @Binds
  @IntoMap
  @StringKey("left")
  abstract CommandSupplier bindLeftMovementCommandSupplier(
      @LeftMovementCommand PlayableCharacterMovementCommand command);

  @Binds
  @IntoMap
  @StringKey("jump")
  abstract CommandSupplier bindJumpMovementCommandSupplier(
      @JumpMovementCommand PlayableCharacterMovementCommand command);

  @Binds
  @IntoMap
  @StringKey("stop")
  abstract CommandSupplier bindStopMovementCommandSupplier(
      @StopMovementCommand PlayableCharacterMovementCommand command);

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
  static ObjectParser provideEnemyCharacterGenerator(EnemyCharacterFactory enemyCharacterFactory) {
    return (tiledObject, generatedMapEntityDataBuilder) -> {
      if (tiledObject.entityType() == EntityType.ENEMY) {
        EnemyCharacter enemyCharacter =
            enemyCharacterFactory.create(tiledObject.x(), tiledObject.y() + tiledObject.height());
        generatedMapEntityDataBuilder.addPhysicsManageable(enemyCharacter);
        generatedMapEntityDataBuilder.addRenderable(enemyCharacter);
        generatedMapEntityDataBuilder.addUpdatable(enemyCharacter);
      }
    };
  }

  @Provides
  @IntoSet
  static CollisionHandler<? extends Collidable<?>, ? super EnemyCharacter>
      provideEnemyCollisionHandler(CollidableTileCollisionHandler collidableTileCollisionHandler) {
    return collidableTileCollisionHandler;
  }
}
