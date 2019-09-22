package believe.map.collidable.command;

import believe.character.playable.PlayableCharacter;
import believe.map.collidable.command.InternalQualifiers.CommandParameter;
import believe.map.io.ObjectParser;
import believe.map.io.TileParser;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import dagger.multibindings.Multibinds;

import java.util.Map;

@Module
public abstract class CommandDaggerModule {
  @Provides
  @CommandParameter
  static String provideCommandParameter() {
    return "command";
  }

  @Multibinds
  abstract Map<String, CommandSupplier<PlayableCharacter, ?>> bindCommandCollisionHandlers();

  @Binds
  @IntoSet
  abstract ObjectParser bindCommandGenerator(CommandGenerator commandGenerator);
}
