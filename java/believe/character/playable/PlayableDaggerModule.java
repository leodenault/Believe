package believe.character.playable;

import static believe.util.Util.hashSetOf;

import believe.character.playable.InternalQualifiers.JumpMovementHandler;
import believe.character.playable.InternalQualifiers.LeftMovementHandler;
import believe.character.playable.InternalQualifiers.RightMovementHandler;
import believe.character.playable.InternalQualifiers.StopMovementHandler;
import believe.map.collidable.command.CommandCollisionHandler;
import believe.map.collidable.command.CommandSupplier;
import believe.map.collidable.tile.CollidableTileCollisionHandler;
import believe.map.io.ObjectParser;
import believe.map.io.TileParser;
import believe.map.tiled.EntityType;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.damage.DamageBoxCollisionHandler;
import believe.statemachine.State.Action;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import dagger.multibindings.StringKey;
import javax.inject.Singleton;

import java.util.Set;

/** Dagger module for bindings on playable entities. */
@Module
public abstract class PlayableDaggerModule {
  @Provides
  @Singleton
  @RightMovementHandler
  static PlayableCharacterMovementCommandCollisionHandler provideRightMovementCollisionHandler() {
    return new PlayableCharacterMovementCommandCollisionHandler(Action.SELECT_RIGHT);
  }

  @Provides
  @Singleton
  @LeftMovementHandler
  static PlayableCharacterMovementCommandCollisionHandler provideLeftMovementCollisionHandler() {
    return new PlayableCharacterMovementCommandCollisionHandler(Action.SELECT_LEFT);
  }

  @Provides
  @Singleton
  @JumpMovementHandler
  static PlayableCharacterMovementCommandCollisionHandler provideJumpMovementCollisionHandler() {
    return new PlayableCharacterMovementCommandCollisionHandler(Action.JUMP);
  }

  @Provides
  @Singleton
  @StopMovementHandler
  static PlayableCharacterMovementCommandCollisionHandler provideStopMovementCollisionHandler() {
    return new PlayableCharacterMovementCommandCollisionHandler(Action.STOP);
  }

  @Provides
  @IntoMap
  @StringKey("right")
  static CommandSupplier<PlayableCharacter, ?> bindRightMovementCommandSupplier(
      @RightMovementHandler PlayableCharacterMovementCommandCollisionHandler rightMovementHandler) {
    return CommandSupplier.from(rightMovementHandler);
  }

  @Provides
  @IntoMap
  @StringKey("left")
  static CommandSupplier<PlayableCharacter, ?> bindLeftMovementCommandSupplier(
      @LeftMovementHandler PlayableCharacterMovementCommandCollisionHandler leftMovementHandler) {
    return CommandSupplier.from(leftMovementHandler);
  }

  @Provides
  @IntoMap
  @StringKey("jump")
  static CommandSupplier<PlayableCharacter, ?> bindJumpMovementCommandSupplier(
      @JumpMovementHandler PlayableCharacterMovementCommandCollisionHandler jumpMovementHandler) {
    return CommandSupplier.from(jumpMovementHandler);
  }

  @Provides
  @IntoMap
  @StringKey("stop")
  static CommandSupplier<PlayableCharacter, ?> bindStopMovementCommandSupplier(
      @StopMovementHandler PlayableCharacterMovementCommandCollisionHandler stopMovementHandler) {
    return CommandSupplier.from(stopMovementHandler);
  }

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
  @ElementsIntoSet
  static Set<CollisionHandler<? extends Collidable<?>, ? super PlayableCharacter>>
      providePlayableCharacterCollisionHandler(
          CollidableTileCollisionHandler collidableTileCollisionHandler,
          DamageBoxCollisionHandler damageBoxCollisionHandler,
          @RightMovementHandler
              PlayableCharacterMovementCommandCollisionHandler rightMovementHandler,
          @LeftMovementHandler PlayableCharacterMovementCommandCollisionHandler leftMovementHandler,
          @JumpMovementHandler PlayableCharacterMovementCommandCollisionHandler jumpMovementHandler,
          @StopMovementHandler
              PlayableCharacterMovementCommandCollisionHandler stopMovementHandler) {
    Set<CollisionHandler<? extends Collidable<?>, ? super PlayableCharacter>> collisionHandlers =
        hashSetOf(collidableTileCollisionHandler, damageBoxCollisionHandler);
    collisionHandlers.add(rightMovementHandler);
    collisionHandlers.add(leftMovementHandler);
    collisionHandlers.add(jumpMovementHandler);
    collisionHandlers.add(stopMovementHandler);
    return collisionHandlers;
  }

  @Provides
  @IntoSet
  static CollisionHandler<? extends Collidable<?>, ? super EnemyCharacter>
      provideEnemyCollisionHandler(CollidableTileCollisionHandler collidableTileCollisionHandler) {
    return collidableTileCollisionHandler;
  }
}
