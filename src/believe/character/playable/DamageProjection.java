package believe.character.playable;

import believe.character.Character.Orientation;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;

import java.util.Arrays;

/** A projection created by the character when they are damaged. */
final class DamageProjection {
  private static final Color ANIMATION_FILTER = new Color(0f, 0f, 1f);

  private final Animation animation;
  private final float recoilXRate;
  private final float recoilYRate;

  private Orientation orientation;
  private int x;
  private int y;

  DamageProjection(Animation animation, int recoilX, int recoilY) {
    this.animation = animation;
    this.animation.setAutoUpdate(false);

    int animationLength = Arrays.stream(animation.getDurations()).sum();
    this.recoilXRate = recoilX / (float) animationLength;
    this.recoilYRate = recoilY / (float) animationLength;
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
    this.x = x;
    this.y = y;

    animation.restart();
  }

  /** Updates the animation if it is still active. */
  void update(int delta) {
    if (orientation == Orientation.RIGHT) {
      x -= Math.round(recoilXRate * delta);
      y += Math.round(recoilYRate * delta);
    } else {
      x += Math.round(recoilXRate * delta);
      y += Math.round(recoilYRate * delta);
    }

    animation.update(delta);
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
}
