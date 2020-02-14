package believe.level;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/** Provides Dagger bindings for levels. */
@Module
public abstract class LevelDaggerModule {
  @Binds
  abstract LevelManager bindLevelManager(LevelManagerImpl impl);

  @Binds
  abstract LevelParser bindLevelParser(LevelData.Parser impl);

  @Provides
  @LevelDefinitionsDirectory
  static String provideLevelDataDirectory() {
    return "/java/believe/level/data";
  }
}
