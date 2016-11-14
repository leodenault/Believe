package musicGame.character;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.Timer;
import musicGame.physics.DamageBox;
import musicGame.physics.DamageHandler.Faction;
import musicGame.physics.PhysicsManager;

public class EnemyCharacter extends Character {

	private static final int PUNCH_PERIOD = 1500; // Period in ms
	
	private boolean attacking;
	private boolean damaging;
	private float verticalSpeed;
	private Timer punchTimer;
	private Animation idle;
	private Animation punch;
	private DamageBox dmg;
	private PhysicsManager manager;
	
	public EnemyCharacter(GUIContext container, int x, int y)
			throws SlickException {
		super(container, x, y);
		this.attacking = false;
		this.damaging = false;
		this.verticalSpeed = 0f;
		this.punchTimer = new Timer();
		this.punchTimer.play();
		this.idle = animSet.get("idle");
		this.punch = animSet.get("punch");
		this.manager = PhysicsManager.getInstance();
		anim.start();
	}
	
	@Override
	public void landed() {}

	@Override
	public float getVerticalSpeed() {
		return verticalSpeed;
	}

	@Override
	public void setVerticalSpeed(float speed) {
		this.verticalSpeed = speed;
	}

	@Override
	public void update(int delta) {
		if (punchTimer.getElapsedTime() >= PUNCH_PERIOD) {
			anim.stop();
			anim = punch;
			anim.start();
			punchTimer.stop();
			attacking = true;
			dmg = new DamageBox(getFaction(), getFloatX() + 9, getFloatY() + 43, 4, 3, 0.1f);
		}

		if (attacking) {
			if (anim.getFrame() >= 2 && !damaging) {
				manager.addStaticCollidable(dmg);
				damaging = true;
			}
			
			if (anim.isStopped()) {
				anim = idle;
				anim.setCurrentFrame(0);
				anim.start();
				punchTimer.play();
				attacking = false;
				damaging = false;
				manager.removeCollidable(dmg);
			}
		}
		
		punchTimer.update(delta);
		anim.update(delta);
	}

	@Override
	protected String getSheetName() {
		return "enemy";
	}
	
	@Override
	public Faction getFaction() {
		return Faction.BAD;
	}

	@Override
	public void inflictDamage(float damage) {}
}
