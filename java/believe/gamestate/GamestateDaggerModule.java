package believe.gamestate;

import believe.character.CharacterDaggerModule;
import dagger.Binds;
import dagger.Module;

@Module(includes = CharacterDaggerModule.class)
public abstract class GamestateDaggerModule {
  @Binds
  abstract GameStateRunner bindGameStateRunner(GameStateRunnerImpl impl);

  @Binds
  abstract StateController bindStateController(StateControllerImpl impl);
}
