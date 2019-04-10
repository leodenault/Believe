package believe.app;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Qualifies a set of {@link believe.gamestate.GameStateBase} instances that will be called at
 * startup.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface GameStates {}
