package believe.gui;

import dagger.Module;
import dagger.Provides;

/** Dagger module providing bindings for GUI components. */
@Module
public abstract class GuiDaggerModule {
  @Provides
  static ImageSupplier bindImageSupplier() {
    return ImageSupplier.DEFAULT;
  }
}
