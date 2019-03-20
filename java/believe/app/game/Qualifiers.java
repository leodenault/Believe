package believe.app.game;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Qualifiers used within the context of the Believe video game.
 */
final class Qualifiers {
  private Qualifiers() {}

  /**
   * Qualifier for the title of the application.
   */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface ApplicationTitle {}
}
