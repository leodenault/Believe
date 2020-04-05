package believe.gui.testing;

import believe.audio.Sound;
import believe.audio.testing.FakeSound;
import believe.gui.FocusSound;
import believe.gui.GuiAction;
import believe.gui.GuiConfigurations;
import believe.gui.SelectedSound;
import believe.input.InputAdapter;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.annotation.Nullable;
import org.newdawn.slick.gui.GUIContext;

import java.util.HashSet;
import java.util.Set;

@Module
abstract class GuiTestDaggerModule {
  @Provides
  @FocusSound
  static Sound provideFocusSoundLocation() {
    return new FakeSound();
  }

  @Provides
  @SelectedSound
  static Sound provideSelectedSoundLocation() {
    return new FakeSound();
  }

  @Provides
  static InputAdapter<GuiAction> provideInputAdapter() {
    return listener -> {};
  }

  @Binds
  abstract GUIContext bindGuiContext(FakeGuiContext impl);

  @Provides
  @GuiConfigurations
  static Set<?> bindGuiConfigurations(
      @Nullable @TestGuiConfigurations Set<?> testGuiConfigurations) {
    return testGuiConfigurations == null ? new HashSet<>() : testGuiConfigurations;
  }
}
