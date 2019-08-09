package believe.physics.damage;

import believe.character.Faction;
import believe.physics.collision.CollisionHandler;
import believe.physics.manager.PhysicsManager;
import dagger.Reusable;
import javax.inject.Inject;

@Reusable
public class DamageBoxCollisionHandler
    implements CollisionHandler<DamageBox, DamageBoxCollidable<?>> {
  private final PhysicsManager physicsManager;

  @Inject
  DamageBoxCollisionHandler(PhysicsManager physicsManager) {
    this.physicsManager = physicsManager;
  }

  @Override
  public void handleCollision(DamageBox damageBox, DamageBoxCollidable<?> subject) {
    Faction inflictorFaction = damageBox.getFaction();
    if (inflictorFaction != subject.getFaction()) {
      physicsManager.removeCollidable(damageBox);
      subject.inflictDamage(damageBox.getDamage(), inflictorFaction);
    }
  }
}
