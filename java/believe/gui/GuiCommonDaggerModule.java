package believe.gui;

import static believe.util.Util.hashSetOf;

import dagger.Module;
import dagger.Provides;

import java.util.Set;

/** Common Dagger bindings between test and production environments. */
@Module
public class GuiCommonDaggerModule {
  @Provides
  @GuiConfigurations
  static Set<?> provideGuiConfigurations(MenuSelectionV2.Configuration menuSelectionConfiguration) {
    return hashSetOf(menuSelectionConfiguration);
  }
}
