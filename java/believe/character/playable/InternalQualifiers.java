package believe.character.playable;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Container class for qualifier annotations internal to this package. */
final class InternalQualifiers {
  /** Qualifies a {@link PlayableCharacterMovementCommandCollisionHandler} for moving right. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface RightMovementHandler {}

  /** Qualifies a {@link PlayableCharacterMovementCommandCollisionHandler} for moving left. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface LeftMovementHandler {}

  /** Qualifies a {@link PlayableCharacterMovementCommandCollisionHandler} for jumping. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface JumpMovementHandler {}

  /** Qualifies a {@link PlayableCharacterMovementCommandCollisionHandler} for stopping movement. */
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface StopMovementHandler {}
}
