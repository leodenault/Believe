package musicGame.physics;

import musicGame.physics.DamageHandler.Faction;

public interface DamageBoxCollidable extends Collidable {
	Faction getFaction();
	void inflictDamage(float damage);
}
