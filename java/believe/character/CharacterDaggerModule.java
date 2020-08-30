package believe.character;

import believe.character.proto.CharacterAnimationsProto;
import believe.datamodel.MutableValue;
import believe.input.InputAdapter;
import believe.input.keyboard.KeyboardInputAdapter;
import believe.map.io.ObjectParser;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.IntoSet;
import kotlin.jvm.functions.Function1;

import java.util.function.Supplier;

@Module
public abstract class CharacterDaggerModule {
  @Provides
  static InputAdapter<CharacterMovementInputAction> provideInputAdapter(
      KeyboardInputAdapter.Factory keyboardInputAdapterFactory, CharacterKeyboardActionMap map) {
    return keyboardInputAdapterFactory.create(map);
  }

  @Provides
  @Reusable
  static MutableValue<CharacterV2> provideMutableCharacter() {
    return MutableValue.of(null);
  }

  @Provides
  @Reusable
  static Function1<CharacterAnimationsProto.CharacterAnimations, Animations>
      provideAnimationsParser(Animations.Parser impl) {
    return impl::parse;
  }

  @Binds
  abstract Supplier<CharacterV2> bindCharacterSupplier(MutableValue<CharacterV2> impl);

  @Binds
  @IntoSet
  abstract ObjectParser bindPlayerObjectParser(PlayerObjectParser impl);
}
