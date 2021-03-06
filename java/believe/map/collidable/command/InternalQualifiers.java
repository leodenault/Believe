package believe.map.collidable.command;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Qualifiers private to this package. */
final class InternalQualifiers {
  /**
   * Qualifies a string as the value of the parameter for fetching the type of command on a command
   * block.
   */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface CommandParameter {}

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface ShouldDespawnParameter {}

  private InternalQualifiers() {}
}
