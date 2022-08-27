package believe.gamestate;

import believe.animation.AnimationDaggerModule;
import believe.app.ApplicationGameStateRunner;
import believe.character.CharacterDaggerModule;
import believe.command.CommandDaggerModule;
import believe.level.LevelDaggerModule;
import believe.map.collidable.tile.CollidableTileDaggerModule;
import believe.map.io.MapParsingDaggerModule;
import believe.mob.MobDaggerModule;
import believe.physics.collision.CollisionDaggerModule;
import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module(
    includes = {
      AnimationDaggerModule.class,
      CharacterDaggerModule.class,
      CollidableTileDaggerModule.class,
      CollisionDaggerModule.class,
      CommandDaggerModule.class,
      LevelDaggerModule.class,
      MapParsingDaggerModule.class,
      MobDaggerModule.class
    })
public abstract class GamestateDaggerModule {
  @Binds
  @Reusable
  @ApplicationGameStateRunner
  abstract GameStateRunner bindGameStateRunner(GameStateRunnerImpl impl);

  @Binds
  abstract StateController bindStateController(StateControllerImpl impl);
}
