package believe.character.playable;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Container class for qualifier annotations internal to this package. */
final class InternalQualifiers {
  /** Qualifies a {@link PlayableCharacterMovementCommand} for moving right. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface RightMovementCommand {}

  /** Qualifies a {@link PlayableCharacterMovementCommand} for moving left. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface LeftMovementCommand {}

  /** Qualifies a {@link PlayableCharacterMovementCommand} for jumping. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface JumpMovementCommand {}

  /** Qualifies a {@link PlayableCharacterMovementCommand} for stopping movement. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface StopMovementCommand {}
}
