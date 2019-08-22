package believe.map.io;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Internal qualifiers for use in this package.
 */
final class InternalQualifiers {
  /** Qualifies a string as the file location of a file containing the definitions of maps. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface MapDefinitionsDirectory {}

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface IsFrontLayerProperty {}

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface IsVisibleProperty {}

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface EntityTypeProperty {}

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface PlayerStartXProperty {}

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface PlayerStartYProperty {}
}
