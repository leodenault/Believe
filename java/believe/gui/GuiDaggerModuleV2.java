package believe.gui;

import static believe.audio.AudioKt.soundFrom;
import static believe.util.Util.hashSetOf;

import believe.audio.Sound;
import believe.core.io.FontLoader;
import believe.input.InputAdapter;
import believe.input.keyboard.KeyboardInputAdapter;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import java.util.Set;
import javax.inject.Singleton;
import org.newdawn.slick.Font;

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
  @Singleton
  @FocusSound
  static Sound provideFocusSound() {
    return soundFrom("res/sfx/selection.ogg").load();
  }

  @Provides
  @Singleton
  @SelectedSound
  static Sound provideSelectedSound() {
    return soundFrom("res/sfx/selection_picked.ogg").load();
  }

  @Provides
  @Singleton
  @ArrowPressedSound
  static Sound provideArrowPressedSound() {
    return soundFrom("res/sfx/tick.ogg").load();
  }

  @Provides
  @Singleton
  @ArrowDepressedSound
  static Sound provideArrowDepressedSound() {
    return soundFrom("res/sfx/uptick.ogg").load();
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
