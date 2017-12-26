package musicGame.physics.collision;

import musicGame.geometry.Rectangle;
import musicGame.character.base.Faction;

public class DamageBox implements Collidable {

	private float damage;
	private Rectangle rect;
	private Faction faction;
	
	public DamageBox(Faction faction, float x, float y, float width, float height, float damage) {
		this.rect = new Rectangle(x, y, width, height);
		this.damage = damage;
		this.faction = faction;
	}
	
	@Override
	public void collision(Collidable other) {}

	@Override
	public CollidableType getType() {
		return CollidableType.DAMAGE_BOX;
	}

	@Override
	public Rectangle getRect() {
		return rect;
	}

	public float getDamage() {
		return damage;
	}
	
	public void setLocation(float x, float y) {
		this.rect.setLocation(x, y);
	}
	
	public Faction getFaction() {
		return faction;
	}
}
