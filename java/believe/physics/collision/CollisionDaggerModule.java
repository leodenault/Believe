package believe.physics.collision;

import dagger.Binds;
import dagger.Module;

/** Module containing bindings for collision logic. */
@Module
public abstract class CollisionDaggerModule {
  @Binds
  abstract CollisionHandlerExecutor bindCollisionHandlerExecutor(
      CollisionHandlerExecutorImpl collisionHandlerExecutorImpl);
}
