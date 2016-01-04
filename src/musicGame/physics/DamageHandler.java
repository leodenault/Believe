package musicGame.physics;

import musicGame.physics.Collidable.CollidableType;

public class DamageHandler implements CollisionHandler<DamageBoxCollidable> {
	public enum Faction {
		GOOD, BAD
	}
	
	private PhysicsManager manager;
	
	public DamageHandler() {
		this.manager = PhysicsManager.getInstance();
	}
	
	@Override
	public void handleCollision(DamageBoxCollidable inflicted, Collidable inflictor) {
		if (inflictor.getType() == CollidableType.DAMAGE_BOX) {
			DamageBox dmg = (DamageBox)inflictor;
			
			if (dmg.getFaction() != inflicted.getFaction()) {
				manager.removeCollidable(inflictor);
				inflicted.inflictDamage(dmg.getDamage());
			}
		}
	}
}
