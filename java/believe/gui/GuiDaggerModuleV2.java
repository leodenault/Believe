package believe.gui;

import static believe.util.Util.hashSetOf;

import believe.audio.LoadableSound;
import believe.audio.Sound;
import believe.core.io.FontLoader;
import believe.input.InputAdapter;
import believe.input.keyboard.KeyboardInputAdapter;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import java.util.Set;
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
  @FocusSound
  static Sound provideFocusSound(LoadableSound.Factory soundFactory) {
    return soundFactory.create("res/sfx/selection.ogg").load();
  }

  @Provides
  @SelectedSound
  static Sound provideSelectedSound(LoadableSound.Factory soundFactory) {
    return soundFactory.create("res/sfx/selection_picked.ogg").load();
  }

  @Provides
  @ArrowPressedSound
  static Sound provideArrowPressedSound(LoadableSound.Factory soundFactory) {
    return soundFactory.create("res/sfx/tick.ogg").load();
  }

  @Provides
  @ArrowDepressedSound
  static Sound provideArrowDepressedSound(LoadableSound.Factory soundFactory) {
    return soundFactory.create("res/sfx/uptick.ogg").load();
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
