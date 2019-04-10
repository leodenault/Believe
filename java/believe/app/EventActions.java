package believe.app;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Qualifies a mapping of keyboard keys to actions affecting a {@link
 * believe.gamestate.PlatformingState}.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface EventActions {}
