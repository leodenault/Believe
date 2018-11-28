package believe.character.playable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.character.Character.Orientation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public final class DamageProjectionTest {
  private static final int RECOIL_X = 100;
  private static final int RECOIL_Y = 10;
  private static final int ANIMATION_LENGTH = 100;
  private static final float RECOIL_X_RATE = RECOIL_X / (float) ANIMATION_LENGTH;
  private static final float RECOIL_Y_RATE = RECOIL_Y / (float) ANIMATION_LENGTH;

  private DamageProjection projection;

  @Mock
  private Animation animation;
  @Mock
  private Image currentFrame;

  @Before
  public void setUp() {
    initMocks(this);
    when(animation.getDurations()).thenReturn(new int[]{
        ANIMATION_LENGTH / 2, ANIMATION_LENGTH / 2});

    projection = new DamageProjection(animation, RECOIL_X, RECOIL_Y);
  }

  @Test
  public void trigger_resetsAnimationPosition() {
    projection.trigger(Orientation.RIGHT, 200, 400);
    projection.render();

    verify(animation).draw(eq(200f), eq(400f), any(Color.class));
  }

  @Test
  public void update_orientationIsRight_updatesAnimationPositionNegatively() {
    int delta = 10;
    projection.trigger(Orientation.RIGHT, 200, 400);
    projection.update(delta);
    projection.render();

    verify(animation).update(delta);
    verify(animation).draw(eq(200 - RECOIL_X_RATE * delta), eq(400 + RECOIL_Y_RATE * delta), any());
  }

  @Test
  public void update_orientationIsLeft_updatesAnimationPositionPositively() {
    when(animation.getCurrentFrame()).thenReturn(currentFrame);
    when(currentFrame.getFlippedCopy(true, false)).thenReturn(currentFrame);
    int delta = 10;
    projection = new DamageProjection(animation, RECOIL_X, RECOIL_Y);

    projection.trigger(Orientation.LEFT, 200, 400);
    projection.update(delta);
    projection.render();

    verify(animation).update(delta);
    verify(currentFrame).draw(eq(200 + RECOIL_X_RATE * delta),
        eq(400 + RECOIL_Y_RATE * delta),
        any());
  }
}
