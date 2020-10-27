package believe.mob;

import believe.datamodel.DataManager;
import believe.map.io.ObjectParser;
import believe.mob.proto.MobAnimationDataProto.DamageFrame;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.IntoSet;
import java.util.List;

@Module
public abstract class MobDaggerModule {
  @Provides
  @Reusable
  @MobAnimationDataLocation
  static String provideMobAnimationDataLocation() {
    return "/java/believe/mob/data";
  }

  @Binds
  @IntoSet
  abstract ObjectParser bindStationaryEnemyParser(StationaryEnemyParser impl);

  @Provides
  @Reusable
  static DataManager<DataManager<List<DamageFrame>>> bindMobDataManager(
      MobDataManager.Factory factory) {
    return factory.create();
  }

  @Binds
  abstract DataManager<DataManager<MobAnimation>> bindMobAnimationDataManager(
      MobAnimationDataManager impl);
}
