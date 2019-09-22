package believe.app;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Qualifies a {@link believe.gamestate.GameStateBase} instance as the first state that should be
 * run in the app.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface FirstState {}