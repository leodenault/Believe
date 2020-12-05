package believe.gui;

import static believe.util.Util.hashSetOf;

import believe.audio.Sound;
import believe.audio.SoundImpl;
import believe.core.io.FontLoader;
import believe.input.InputAdapter;
import believe.input.keyboard.KeyboardInputAdapter;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import org.newdawn.slick.Font;

import java.util.Set;

@Module
public abstract class GuiDaggerModuleV2 {
  @Provides
  @GuiConfigurations
  static Set<?> provideGuiConfigurations(
      VerticalContainer.Configuration guiContainerConfiguration,
      MenuSelectionV2.Configuration menuSelectionConfiguration,
      TextBox.Configuration textBoxConfiguration,
      NumberPickerV2.Configuration numberPickerConfiguration) {
    return hashSetOf(
        guiContainerConfiguration,
        menuSelectionConfiguration,
        textBoxConfiguration,
        numberPickerConfiguration);
  }

  @Provides
  @FocusSound
  static Sound provideFocusSound() {
    return new SoundImpl("res/sfx/selection.ogg");
  }

  @Provides
  @SelectedSound
  static Sound provideSelectedSound() {
    return new SoundImpl("res/sfx/selection_picked.ogg");
  }

  @Provides
  @ArrowPressedSound
  static Sound provideArrowPressedSound() {
    return new SoundImpl("res/sfx/tick.ogg");
  }

  @Provides
  @ArrowDepressedSound
  static Sound provideArrowDepressedSound() {
    return new SoundImpl("res/sfx/uptick.ogg");
  }

  @Provides
  @Reusable
  static InputAdapter<GuiAction> provideInputAdapter(
      KeyboardInputAdapter.Factory factory, GuiKeyboardActionMap mapInput) {
    return factory.create(mapInput);
  }

  @Provides
  @Reusable
  static FocusableGroup.Factory provideFocusableGroupFactory(
      FocusableGroupImplFactory focusableGroupImplFactory) {
    return focusableGroupImplFactory::create;
  }

  @Provides
  static ImageSupplier provideImageSupplier() {
    return ImageSupplier.DEFAULT;
  }

  @Provides
  @Reusable
  static Font provideFont(FontLoader fontLoader) {
    return fontLoader.getBaseFont();
  }
}
