package believe.character.playable;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.character.Character.Orientation;
import believe.physics.manager.PhysicsManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

public final class DamageProjectionTest {
  private static final int RECOIL_X = 100;
  private static final int ANIMATION_LENGTH = 100;
  private static final float RECOIL_X_RATE = RECOIL_X / (float) ANIMATION_LENGTH;

  private DamageProjection projection;

  @Mock
  private Animation animation;
  @Mock
  private Image currentFrame;
  @Mock
  private PhysicsManager physicsManager;

  @Before
  public void setUp() {
    initMocks(this);
    when(animation.getDurations()).thenReturn(new int[]{
        ANIMATION_LENGTH / 2, ANIMATION_LENGTH / 2});

    projection = new DamageProjection(animation, physicsManager, RECOIL_X);
  }

  @Test
  public void trigger_resetsAnimationPosition() {
    projection.setVerticalSpeed(100);
    projection.trigger(Orientation.RIGHT, 200, 400);

    assertThat(projection.getFloatX(), is(equalTo(200f)));
    assertThat(projection.getFloatY(), is(equalTo(400f)));
    assertThat(projection.getVerticalSpeed(), is(equalTo(0f)));
  }

  @Test
  public void trigger_addsSelfAsGravityObject() {
    projection.trigger(Orientation.LEFT, 300, 200);

    verify(physicsManager).addGravityObject(projection);
  }

  @Test
  public void update_orientationIsRight_updatesAnimationPositionNegatively() {
    int delta = 10;
    projection.trigger(Orientation.RIGHT, 200, 400);
    projection.update(delta);

    verify(animation).update(delta);
    assertThat(projection.getFloatX(), is(equalTo(200 - RECOIL_X_RATE * delta)));
    assertThat(projection.getFloatY(), is(equalTo(400f)));
  }

  @Test
  public void update_orientationIsLeft_updatesAnimationPositionPositively() {
    int delta = 10;
    projection = new DamageProjection(animation, physicsManager, RECOIL_X);

    projection.trigger(Orientation.LEFT, 200, 400);
    projection.update(delta);

    verify(animation).update(delta);
    assertThat(projection.getFloatX(), is(equalTo(200 + RECOIL_X_RATE * delta)));
    assertThat(projection.getFloatY(), is(equalTo(400f)));
  }

  @Test
  public void update_animationIsNotStopped_doesNotRemovesSelfAsGravityObject() {
    when(animation.isStopped()).thenReturn(false);

    projection.trigger(Orientation.LEFT, 400, 1000);
    projection.update(ANIMATION_LENGTH - 1);

    verify(physicsManager, never()).removeGravityObject(projection);
  }

  @Test
  public void update_animationIsStopped_removesSelfAsGravityObject() {
    when(animation.isStopped()).thenReturn(true);

    projection.trigger(Orientation.LEFT, 400, 1000);
    projection.update(ANIMATION_LENGTH);

    verify(physicsManager).removeGravityObject(projection);
  }

  @Test
  public void setLocation_correctlySetsLocation() {
    projection.setLocation(10, 20);

    assertThat(projection.getFloatX(), is(equalTo(10f)));
    assertThat(projection.getFloatY(), is(equalTo(20f)));
  }

  @Test
  public void setVerticalSpeed_correctlySetsVerticalSpeed() {
    projection.setVerticalSpeed(30);

    assertThat(projection.getVerticalSpeed(), is(equalTo(30f)));
  }
}
