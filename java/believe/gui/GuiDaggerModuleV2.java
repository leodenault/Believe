package believe.gui;

import static believe.util.Util.hashSetOf;

import believe.audio.Sound;
import believe.audio.SoundImpl;
import believe.input.InputAdapter;
import believe.input.keyboard.KeyboardInputAdapter;
import dagger.Module;
import dagger.Provides;

import java.util.Set;

@Module
public abstract class GuiDaggerModuleV2 {
  @Provides
  @GuiConfigurations
  static Set<?> provideGuiConfigurations(
      MenuSelectionV2.Configuration menuSelectionConfiguration,
      TextBox.Configuration textBoxConfiguration) {
    return hashSetOf(menuSelectionConfiguration, textBoxConfiguration);
  }

  @Provides
  @FocusSound
  static Sound provideFocusSoundLocation() {
    return new SoundImpl("res/sfx/selection.ogg");
  }

  @Provides
  @SelectedSound
  static Sound provideSelectedSoundLocation() {
    return new SoundImpl("res/sfx/selection_picked.ogg");
  }

  @Provides
  static InputAdapter<GuiAction> provideInputAdapter(
      KeyboardInputAdapter.Factory factory, GuiKeyboardActionMap mapInput) {
    return factory.create(mapInput);
  }
}
