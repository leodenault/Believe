package believe.physics.collision;

import believe.character.Faction;

public interface DamageBoxCollidable extends Collidable {
  Faction getFaction();
  void inflictDamage(float damage);
}
