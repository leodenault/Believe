package believe.app.game;

import believe.app.ApplicationComponent;
import believe.app.ApplicationModule;
import believe.gamestate.GamestateModule;
import dagger.Component;
import javax.inject.Singleton;

/** Top-level component for the Believe video game. */
@Singleton
@Component(
    modules = {
      ApplicationModule.class,
      BelieveGameModule.class,
      GamestateModule.class,
    })
interface BelieveComponent extends ApplicationComponent {}
