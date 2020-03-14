package believe.gamestate;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class GamestateDaggerModule {
  @Binds
  abstract GameStateRunner bindGameStateRunner(GameStateRunnerImpl impl);
}
