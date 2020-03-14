package believe.app.game;

import believe.app.ApplicationComponentV2;
import dagger.Component;

@Component(dependencies = ApplicationComponentV2.class, modules = BelieveDaggerModuleV2.class)
interface BelieveComponentV2 {}
