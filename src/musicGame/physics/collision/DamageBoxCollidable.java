package musicGame.physics.collision;

import musicGame.character.Faction;

public interface DamageBoxCollidable extends Collidable {
  Faction getFaction();
  void inflictDamage(float damage);
}
