package believe.app.game;

import believe.app.ApplicationComponent;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Top-level component for the Believe video game.
 */
@Singleton
@Component(modules = BelieveGameModule.class)
interface BelieveComponent extends ApplicationComponent {}
