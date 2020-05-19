package believe.physics.damage;

import static believe.util.Util.hashSetOf;

import believe.character.Faction;
import believe.geometry.Rectangle;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.util.Collections;
import java.util.Set;

@AutoFactory
public class DamageBox implements Collidable<DamageBox> {
  private final DamageBoxCollisionHandler damageBoxCollisionHandler;
  private final float damage;
  private final Faction faction;

  private Rectangle rect;

  DamageBox(
      @Provided DamageBoxCollisionHandler damageBoxCollisionHandler,
      Faction faction,
      int x,
      int y,
      int width,
      int height,
      float damage) {
    this.damageBoxCollisionHandler = damageBoxCollisionHandler;
    this.rect = new Rectangle(x, y, width, height);
    this.damage = damage;
    this.faction = faction;
  }

  @Override
  public Set<CollisionHandler<? super DamageBox, ? extends Collidable<?>>>
      leftCompatibleHandlers() {
    return hashSetOf(damageBoxCollisionHandler);
  }

  @Override
  public Set<CollisionHandler<? extends Collidable<?>, ? super DamageBox>>
      rightCompatibleHandlers() {
    return Collections.emptySet();
  }

  @Override
  public Rectangle rect() {
    return rect;
  }

  public float getDamage() {
    return damage;
  }

  public void setLocation(float x, float y) {
    this.rect = new Rectangle(x, y, rect.getWidth(), rect.getHeight());
  }

  public Faction getFaction() {
    return faction;
  }
}
