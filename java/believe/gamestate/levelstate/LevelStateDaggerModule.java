package believe.gamestate.levelstate;

import believe.animation.AnimationDaggerModule;
import believe.character.CharacterDaggerModule;
import believe.command.CommandDaggerModule;
import believe.gamestate.GameStateRunner;
import believe.gamestate.GameStateRunnerImpl;
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
      CollisionDaggerModule.class,
      CollidableTileDaggerModule.class,
      CommandDaggerModule.class,
      LevelDaggerModule.class,
      MapParsingDaggerModule.class,
      MobDaggerModule.class,
    })
public abstract class LevelStateDaggerModule {
  @Binds
  @Reusable
  @LevelStateRunner
  abstract GameStateRunner bindLevelStateRunner(GameStateRunnerImpl impl);

  @Binds
  abstract LevelStateController bindLevelStateController(LevelStateControllerImpl impl);
}
