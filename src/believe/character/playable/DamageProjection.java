package believe.character.playable;

import believe.character.Character.Orientation;
import believe.physics.gravity.GravityObject;
import believe.physics.manager.PhysicsManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;

import java.util.Arrays;

/** A projection created by the character when they are damaged. */
final class DamageProjection implements GravityObject {
  private static final float RED_FILTER = 0.4f;
  private static final float GREEN_FILTER = 0.4f;
  private static final float BLUE_FILTER = 0.9f;
  private static final float MAX_ALPHA = 0.7f;

  private final Animation animation;
  private final PhysicsManager physicsManager;
  private final float recoilXRate;
  private final float coefficientA;
  private final float coefficientB;

  private Orientation orientation;
  private float x;
  private float y;
  private float verticalSpeed;
  private int elapsedTime;
  private boolean active;

  DamageProjection(Animation animation, PhysicsManager physicsManager, int recoilX) {
    this.animation = animation;
    this.physicsManager = physicsManager;
    this.animation.setAutoUpdate(false);

    int animationLength = Arrays.stream(animation.getDurations()).sum();
    this.recoilXRate = recoilX / (float) animationLength;
    this.active = false;

    // We want the transparency of the projection to follow a parabolic curve, so we set the
    // coefficents here.
    coefficientA = -4 * MAX_ALPHA / (animationLength * animationLength);
    coefficientB = 4 * MAX_ALPHA / animationLength;
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
    elapsedTime = 0;
    active = true;
    physicsManager.addGravityObject(this);
  }

  /** Updates the animation if it is still active. */
  void update(int delta) {
    elapsedTime += delta;
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
    // ax^2 + bx
    float
        transparencyFunctionResult =
        coefficientA * elapsedTime * elapsedTime + coefficientB * elapsedTime;
    Color filter = new Color(RED_FILTER, GREEN_FILTER, BLUE_FILTER, transparencyFunctionResult);
    if (orientation == Orientation.RIGHT) {
      animation.draw(x, y, filter);
    } else {
      animation.getCurrentFrame().getFlippedCopy(true, false).draw(x, y, filter);
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
