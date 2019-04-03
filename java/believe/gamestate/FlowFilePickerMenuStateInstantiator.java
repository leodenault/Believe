package believe.gamestate;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Qualifies a {@link believe.app.StateInstantiator} for the {@link ArcadeState}. */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface FlowFilePickerMenuStateInstantiator {}
