package believe.physics.damage;

import believe.character.Faction;
import believe.physics.collision.Collidable;

public interface DamageBoxCollidable<T extends DamageBoxCollidable<T>> extends Collidable<T> {
  Faction getFaction();

  void inflictDamage(float damage, Faction inflictor);
}
