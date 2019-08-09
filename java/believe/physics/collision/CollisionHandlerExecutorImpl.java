package believe.physics.collision;

import dagger.Reusable;
import javax.inject.Inject;

import java.util.HashSet;
import java.util.Set;

/** Default implementation of {@link CollisionHandlerExecutor}. */
@Reusable
final class CollisionHandlerExecutorImpl implements CollisionHandlerExecutor {
  @Inject
  CollisionHandlerExecutorImpl() {}

  // Both sides of the retainAll calls deal with Set<CollisionHandler<...>> instances. They should
  // not cause any problems.
  @SuppressWarnings("SuspiciousMethodCalls")
  @Override
  public <A extends Collidable<A>, B extends Collidable<B>> void execute(
      Collidable<A> collidable1, Collidable<B> collidable2) {
    Set<CollisionHandler<? super A, ? extends Collidable>> leftAHandlers =
        new HashSet<>(collidable1.leftCompatibleHandlers());
    HashSet<CollisionHandler<? super B, ? extends Collidable<?>>> leftBHandlers =
        new HashSet<>(collidable2.leftCompatibleHandlers());
    HashSet<CollisionHandler<? extends Collidable<?>, ? super A>> rightAHandlers =
        new HashSet<>(collidable1.rightCompatibleHandlers());
    HashSet<CollisionHandler<? extends Collidable<?>, ? super B>> rightBHandlers =
        new HashSet<>(collidable2.rightCompatibleHandlers());

    leftAHandlers.retainAll(rightBHandlers);
    leftBHandlers.retainAll(rightAHandlers);

    @SuppressWarnings("unchecked") // Safe cast due to specification of Collidable.
    A typedCollidable1 = (A) collidable1;
    @SuppressWarnings("unchecked") // Safe cast due to specification of Collidable.
    B typedCollidable2 = (B) collidable2;
    for (CollisionHandler<? super A, ? extends Collidable> leftAHandler :
        leftAHandlers) { // Safe cast
      // since collidable1 declares the collisionHandler instance as a
      // CollisionHandler<? super A, ...> and collidable2 declares it as a
      // CollisionHandler<..., ? super B>.
      @SuppressWarnings("unchecked")
      CollisionHandler<? super A, ? super B> aBcollisionHandler =
          (CollisionHandler<? super A, ? super B>) leftAHandler;
      aBcollisionHandler.handleCollision(typedCollidable1, typedCollidable2);
    }
    for (CollisionHandler<? super B, ? extends Collidable<?>> collisionHandler :
        leftBHandlers) { // Safe
      // cast since collidable1 declares the collisionHandler instance as a
      // CollisionHandler<..., ? super A> and collidable2 declares it as a
      // CollisionHandler<? super B, ...>.
      @SuppressWarnings("unchecked")
      CollisionHandler<? super B, ? super A> bAcollisionHandler =
          (CollisionHandler<? super B, ? super A>) collisionHandler;
      bAcollisionHandler.handleCollision(typedCollidable2, typedCollidable1);
    }
  }
}
