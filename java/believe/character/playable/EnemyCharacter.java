package believe.character.playable;

import believe.character.Character;
import believe.character.Faction;
import believe.core.Timer;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.damage.DamageBox;
import believe.physics.damage.DamageBoxFactory;
import believe.physics.manager.PhysicsManager;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import javax.annotation.Nullable;
import org.newdawn.slick.Animation;
import org.newdawn.slick.gui.GUIContext;

import java.util.Set;

@AutoFactory
public class EnemyCharacter extends Character<EnemyCharacter> {

  private static final int PUNCH_PERIOD = 1500; // Period in ms

  private final Timer punchTimer;
  private final Animation idle;
  private final Animation punch;
  private final PhysicsManager physicsManager;
  private final DamageBoxFactory damageBoxFactory;

  private float verticalSpeed;
  private boolean attacking;
  private boolean damaging;
  @Nullable private DamageBox dmg;

  EnemyCharacter(
      @Provided GUIContext container,
      @Provided PhysicsManager physicsManager,
      @Provided DamageBoxFactory damageBoxFactory,
      @Provided
          Set<CollisionHandler<? extends Collidable<?>, ? super EnemyCharacter>>
              rightCompatibleHandlers,
      int x,
      int y) {
    super(container, DamageListener.NONE, rightCompatibleHandlers, x, y);
    this.damageBoxFactory = damageBoxFactory;
    this.attacking = false;
    this.damaging = false;
    this.verticalSpeed = 0f;
    this.punchTimer = new Timer();
    this.punchTimer.play();
    this.idle = animSet.get("idle");
    this.punch = animSet.get("punch");
    this.physicsManager = physicsManager;
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
      dmg = damageBoxFactory.create(getFaction(), getX() + 9, getY() + 43, 4, 3, 0.1f);
    }

    if (attacking) {
      if (anim.getFrame() >= 2 && !damaging) {
        physicsManager.addStaticCollidable(dmg);
        damaging = true;
      }

      if (anim.isStopped()) {
        anim = idle;
        anim.setCurrentFrame(0);
        anim.start();
        punchTimer.play();
        attacking = false;
        damaging = false;
        physicsManager.removeCollidable(dmg);
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
}
