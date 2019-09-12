package believe.dialogue;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Container class for qualifier annotations internal to this package. */
final class InternalQualifiers {
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface ModulePrivate {}

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface DialogueNameProperty {}
}
