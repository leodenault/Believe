package believe.character.playable;

import believe.character.Character.Orientation;
import believe.physics.gravity.GravityObject;
import believe.physics.manager.PhysicsManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;

import java.util.Arrays;

/** A projection created by the character when they are damaged. */
final class DamageProjection implements GravityObject {
  private static final Color ANIMATION_FILTER = new Color(0.2f, 0.2f, 0.75f, 0.66f);

  private final Animation animation;
  private final PhysicsManager physicsManager;
  private final float recoilXRate;

  private Orientation orientation;
  private float x;
  private float y;
  private float verticalSpeed;
  private boolean active;

  DamageProjection(Animation animation, PhysicsManager physicsManager, int recoilX) {
    this.animation = animation;
    this.physicsManager = physicsManager;
    this.animation.setAutoUpdate(false);

    int animationLength = Arrays.stream(animation.getDurations()).sum();
    this.recoilXRate = recoilX / (float) animationLength;
    this.active = false;
  }

  /**
   * Triggers the animation to draw at location (x, y).
   *
   * @param orientation the {@link Orientation} with which the animation should be drawn.
   * @param x the starting location on the x axis.
   * @param y the starting location on the y axis.
   */
  void trigger(Orientation orientation, int x, int y) {
    this.orientation = orientation;
    setVerticalSpeed(0);
    setLocation(x, y);

    animation.restart();
    active = true;
    physicsManager.addGravityObject(this);
  }

  /** Updates the animation if it is still active. */
  void update(int delta) {
    if (orientation == Orientation.RIGHT) {
      x -= Math.round(recoilXRate * delta);
    } else {
      x += Math.round(recoilXRate * delta);
    }

    animation.update(delta);
    if (animation.isStopped() && active) {
      active = false;
      physicsManager.removeGravityObject(this);
    }
  }

  /** Renders the animation */
  void render() {
    if (orientation == Orientation.RIGHT) {
      animation.draw(x, y, ANIMATION_FILTER);
    } else {
      animation.getCurrentFrame().getFlippedCopy(true, false).draw(x, y, ANIMATION_FILTER);
    }
  }

  /** Indicates whether the animation has reached the last frame. */
  boolean isStopped() {
    return animation.isStopped();
  }

  @Override
  public float getVerticalSpeed() {
    return verticalSpeed;
  }

  @Override
  public void setVerticalSpeed(float speed) {
    verticalSpeed = speed;
  }

  @Override
  public void setLocation(float x, float y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public float getFloatX() {
    return x;
  }

  @Override
  public float getFloatY() {
    return y;
  }
}
