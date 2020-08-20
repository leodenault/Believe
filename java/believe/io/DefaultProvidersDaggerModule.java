package believe.io;

import believe.core.io.JarClasspathLocation;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.IntoSet;
import org.newdawn.slick.util.FileSystemLocation;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

import java.io.File;

/** A Dagger module providing default bindings for dependencies within this package. */
@Module
public abstract class DefaultProvidersDaggerModule {
  @Provides
  @Reusable
  static FileSystemLocation provideDefaultFileSystemLocation() {
    return new FileSystemLocation(new File("."));
  }

  @Binds
  @IntoSet
  abstract ResourceLocation bindJarClasspathLocation(JarClasspathLocation impl);
}
