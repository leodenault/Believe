package believe.app;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Qualifier for the title of the application. */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationTitle {}
