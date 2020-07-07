package believe.character;

import believe.input.InputAdapter;
import believe.input.keyboard.KeyboardInputAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class CharacterDaggerModule {
  @Provides
  static InputAdapter<CharacterMovementInputAction> provideInputAdapter(
      KeyboardInputAdapter.Factory keyboardInputAdapterFactory, CharacterKeyboardActionMap map) {
    return keyboardInputAdapterFactory.create(map);
  }
}
