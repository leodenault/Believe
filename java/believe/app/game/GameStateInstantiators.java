package believe.app.game;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Qualifies a set of {@link believe.app.StateInstantiator} instances that will be called at
 * startup.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface GameStateInstantiators {}
