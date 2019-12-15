package believe.io;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.Multibinds;
import org.newdawn.slick.util.ResourceLocation;

import java.util.Set;

/** Provides bindings for files responsible for I/O operations. */
@Module
public abstract class IoDaggerModule {
  @Binds
  abstract ResourceManager bindResourceLoader(ResourceManagerImpl resourceLoader);

  @Multibinds
  abstract Set<ResourceLocation> bindResourceLocations();
}
