package believe.gui;

import believe.audio.Sound;
import believe.audio.SoundImpl;
import believe.input.InputAdapter;
import believe.input.keyboard.KeyboardInputAdapter;
import dagger.Module;
import dagger.Provides;

@Module(includes = GuiCommonDaggerModule.class)
public abstract class GuiDaggerModuleV2 {
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

  @Provides
  static TextBoxStyle provideTextBoxStyle() {
    return new TextBoxStyle(/* textColour= */ 0xffffff);
  }
}
