package believe.gui.testing;

import believe.audio.Sound;
import believe.audio.testing.FakeSound;
import believe.gui.ArrowDepressedSound;
import believe.gui.ArrowPressedSound;
import believe.gui.FocusSound;
import believe.gui.GuiAction;
import believe.gui.GuiConfigurations;
import believe.gui.SelectedSound;
import believe.input.InputAdapter;
import believe.input.testing.FakeInputAdapter;
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
  static Sound provideFocusSound() {
    return new FakeSound();
  }

  @Provides
  @SelectedSound
  static Sound provideSelectedSound() {
    return new FakeSound();
  }

  @Provides
  @ArrowPressedSound
  static Sound provideArrowPressedSound() {
    return new FakeSound();
  }

  @Provides
  @ArrowDepressedSound
  static Sound provideArrowDepressedSound() {
    return new FakeSound();
  }

  @Provides
  static InputAdapter<GuiAction> provideInputAdapter() {
    return FakeInputAdapter.create();
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
