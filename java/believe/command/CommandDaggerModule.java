package believe.command;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;
import dagger.multibindings.Multibinds;

import java.util.Map;

/** Provides Dagger bindings for commands. */
@Module
public abstract class CommandDaggerModule {
  @Multibinds
  @Reusable
  abstract Map<String, CommandSupplier> bindCommandSupplierMap();

  @Binds
  abstract CommandGenerator bindCommandGenerator(CommandGeneratorImpl commandGeneratorImpl);
}
