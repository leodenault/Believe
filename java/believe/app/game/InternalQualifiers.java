package believe.app.game;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Qualifiers used within the context of the Believe video game. */
final class InternalQualifiers {
  private InternalQualifiers() {}

  /** Qualifier for the title of the application. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface ApplicationTitle {}

  /** Qualifier for any bindings that are private to the Dagger module in this package. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface ModulePrivate {}
}
