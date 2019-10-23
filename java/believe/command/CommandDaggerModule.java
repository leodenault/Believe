package believe.command;

import believe.command.InternalQualifiers.SequenceParameter;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.IntoMap;
import dagger.multibindings.Multibinds;
import dagger.multibindings.StringKey;

import java.util.Map;

/** Provides Dagger bindings for commands. */
@Module
public abstract class CommandDaggerModule {
  @Multibinds
  @Reusable
  abstract Map<String, CommandSupplier> bindCommandSupplierMap();

  @Provides
  @SequenceParameter
  static String provideSequenceCommandParameter() {
    return "sequence";
  }

  @Binds
  @IntoMap
  @StringKey("sequence")
  abstract CommandSupplier bindCommandSequenceSupplier(
      CommandSequenceSupplier commandSequenceSupplier);

  @Binds
  abstract CommandGenerator bindCommandGenerator(CommandGeneratorImpl commandGeneratorImpl);
}
