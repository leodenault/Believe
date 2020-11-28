package believe.gamestate.levelstate;

import believe.gamestate.GameStateRunner;
import believe.gamestate.GameStateRunnerImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public abstract class LevelStateDaggerModule {
  @Binds
  @Reusable
  @LevelStateRunner
  abstract GameStateRunner bindLevelStateRunner(GameStateRunnerImpl impl);

  @Binds
  abstract LevelStateController bindLevelStateController(LevelStateControllerImpl impl);
}
