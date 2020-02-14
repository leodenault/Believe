package believe.command;

import believe.command.proto.CommandProto.CommandSequence;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import dagger.multibindings.Multibinds;

import java.util.Set;

/** Provides Dagger bindings for commands. */
@Module
public abstract class CommandDaggerModule {
  @Multibinds
  abstract Set<CommandParser<?>> bindCommandParsers();

  @Binds
  @IntoSet
  abstract CommandParser<?> bindCommandSequenceSupplier(
      CommandSequenceParser commandSequenceParser);

  @Provides
  @IntoSet
  static GeneratedExtension<?, ?> provideCommandSequenceExtension() {
    return CommandSequence.commandSequence;
  }

  @Binds
  abstract CommandGenerator bindCommandGenerator(CommandGeneratorImpl impl);
}
