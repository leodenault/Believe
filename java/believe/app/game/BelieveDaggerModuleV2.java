package believe.app.game;

import believe.app.ApplicationTitle;
import dagger.Module;
import dagger.Provides;

@Module
class BelieveDaggerModuleV2 {
  @Provides
  @ApplicationTitle
  static String provideApplicationTitle() {
    return "Believe";
  }
}
