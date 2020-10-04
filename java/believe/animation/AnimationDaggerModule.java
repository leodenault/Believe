package believe.animation;

import believe.datamodel.DataManager;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public abstract class AnimationDaggerModule {
  @Provides
  @Reusable
  @SpriteSheetDirectoryName
  static String provideSpriteSheetDirectoryName() {
    return "/java/believe/animation/data";
  }

  @Provides
  @Reusable
  static DataManager<DataManager<Animation>> provideAnimationManagers(
      SpriteSheetDataManagerFactory factory) {
    return factory.create();
  }
}
