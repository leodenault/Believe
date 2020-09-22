package believe.mob;

import believe.map.io.ObjectParser;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public abstract class MobDaggerModule {
  @Binds
  @IntoSet
  abstract ObjectParser bindStationaryEnemyParser(StationaryEnemyParser impl);
}
