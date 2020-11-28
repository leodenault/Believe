package believe.gamestate;

import believe.app.ApplicationGameStateRunner;
import believe.character.CharacterDaggerModule;
import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module(includes = CharacterDaggerModule.class)
public abstract class GamestateDaggerModule {
  @Binds
  @Reusable
  @ApplicationGameStateRunner
  abstract GameStateRunner bindGameStateRunner(GameStateRunnerImpl impl);

  @Binds
  abstract StateController bindStateController(StateControllerImpl impl);
}
