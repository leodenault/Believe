package believe.command;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Qualifiers private to this package. */
final class InternalQualifiers {
  /**
   * Qualifies a string as the value of the parameter for fetching a sequence of commands on a
   * command block.
   */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface SequenceParameter {}

  private InternalQualifiers() {}
}
