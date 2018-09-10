package believe.physics.collision;

import believe.character.Faction;
import believe.physics.manager.PhysicsManager;
import believe.physics.collision.Collidable.CollidableType;

public class DamageHandler implements CollisionHandler<DamageBoxCollidable> {
  private PhysicsManager manager;

  public DamageHandler() {
    this.manager = PhysicsManager.getInstance();
  }

  @Override
  public void handleCollision(DamageBoxCollidable inflicted, Collidable inflictor) {
    if (inflictor.getType() == CollidableType.DAMAGE_BOX) {
      DamageBox dmg = (DamageBox)inflictor;

      Faction inflictorFaction = dmg.getFaction();
      if (inflictorFaction != inflicted.getFaction()) {
        manager.removeCollidable(inflictor);
        inflicted.inflictDamage(dmg.getDamage(), inflictorFaction);
      }
    }
  }
}
