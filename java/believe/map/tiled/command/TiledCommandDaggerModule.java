package believe.map.tiled.command;

import believe.command.proto.CommandProto;
import believe.proto.TextProtoParser;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.Multibinds;

import java.util.Map;

/** Provides Dagger bindings for commands. */
@Module
public abstract class TiledCommandDaggerModule {
  @Provides
  @TiledCommandParameter
  static String provideTiledCommandParameter() {
    return "command";
  }

  @Binds
  abstract TiledCommandParser bindTiledCommandParser(TiledCommandParserImpl impl);

  @Provides
  @ModulePrivate
  static TextProtoParser<CommandProto.Command> provideTextProtoParser(
      TextProtoParser.Factory factory) {
    return factory.create(CommandProto.Command.class);
  }
}
