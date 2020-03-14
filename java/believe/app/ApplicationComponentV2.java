package believe.app;

import believe.app.flag_parsers.CommandLineArguments;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;

/** The top-level component of a Believe application. */
@Singleton
@Component(modules = ApplicationDaggerModuleV2.class)
public interface ApplicationComponentV2 {
  /** The {@link ApplicationV2} that will run the Believe application logic. */
  ApplicationV2 application();

  @Component.Factory
  interface Factory {
    /**
     * Creates an {@link ApplicationComponentV2}.
     *
     * @param commandLineArgs the command line arguments that were passed to the application.
     * @param applicationTitle the title of the application.
     */
    ApplicationComponentV2 create(
        @BindsInstance @CommandLineArguments String[] commandLineArgs,
        @BindsInstance @ApplicationTitle String applicationTitle,
        @BindsInstance StateSelector stateSelector);
  }
}
