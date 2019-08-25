package believe.map.gui;

import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import believe.geometry.Rectangle;
import believe.gui.testing.FakeImage;
import believe.map.data.BackgroundSceneData;
import believe.map.data.proto.MapMetadataProto;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

/** Unit tests for {@link MapBackground}. */
@InstantiateMocksIn
final class MapBackgroundTest {
  private static final int MAP_HEIGHT = 500;
  private static final int IMAGE_WIDTH = 10;
  private static final int IMAGE_HEIGHT = 20;
  private static final float TOP_Y_POSITION = 0.1f;
  private static final float BOTTOM_Y_POSITION = 0.8f;
  private static final float HORIZONTAL_SPEED_MULTIPLIER = 0.5f;
  private static final FakeImage IMAGE = new FakeImage(IMAGE_WIDTH, IMAGE_HEIGHT);

  private final MapBackground mapBackground =
      new MapBackground(
          BackgroundSceneData.create(
              IMAGE,
              MapMetadataProto.MapBackground.newBuilder()
                  .setTopYPosition(TOP_Y_POSITION)
                  .setBottomYPosition(BOTTOM_Y_POSITION)
                  .setHorizontalSpeedMultiplier(HORIZONTAL_SPEED_MULTIPLIER)
                  .build()),
          /* mapHeight= */ 500);

  @Mock private GUIContext guiContext;
  @Mock private Graphics graphics;

  @Test
  void render_backgroundIsAtTop_rendersAtTopYPosition() {
    mapBackground.valueChanged(
        new Rectangle(
            /* x= */ 0, /* y= */ 0, /* width= */ IMAGE_WIDTH, /* height= */ IMAGE_HEIGHT));

    mapBackground.render(guiContext, graphics);

    verify(graphics)
        .drawImage(
            eq(IMAGE), /* x= */ anyFloat(), /* y= */ eq(positionFromPercentage(TOP_Y_POSITION)));
  }

  @Test
  void render_backgroundIsAtBottom_rendersAtBottomYPosition() {
    int parentHeight = 50;
    mapBackground.valueChanged(
        new Rectangle(
            /* x= */ 0,
            /* y= */ MAP_HEIGHT - parentHeight,
            /* width= */ IMAGE_WIDTH,
            /* height= */ parentHeight));

    mapBackground.render(guiContext, graphics);

    verify(graphics)
        .drawImage(
            eq(IMAGE), /* x= */ anyFloat(), /* y= */ eq(positionFromPercentage(BOTTOM_Y_POSITION)));
  }

  @Test
  void render_imageIsRepeatedAcrossParentWidth() {
    int parentWidth = IMAGE_WIDTH * 3;
    mapBackground.valueChanged(
        new Rectangle(
            /* x= */ 0, /* y= */ 0, /* width= */ parentWidth, /* height= */ IMAGE_HEIGHT));

    mapBackground.render(guiContext, graphics);

    verify(graphics).drawImage(eq(IMAGE), /* x= */ eq(0f), /* y= */ anyFloat());
    verify(graphics).drawImage(eq(IMAGE), /* x= */ eq((float) IMAGE_WIDTH), /* y= */ anyFloat());
    verify(graphics)
        .drawImage(eq(IMAGE), /* x= */ eq(2 * (float) IMAGE_WIDTH), /* y= */ anyFloat());
    verifyNoMoreInteractions(graphics);
  }

  @Test
  void render_parentPositionNotAtMultipleOfImageWidth_scrollsImageByHorizontalMultiplier() {
    int parentWidth = IMAGE_WIDTH * 3;
    int parentHorizontalScroll = IMAGE_WIDTH;
    float imageHorizontalScroll = HORIZONTAL_SPEED_MULTIPLIER * parentHorizontalScroll;
    mapBackground.valueChanged(
        new Rectangle(
            /* x= */ parentHorizontalScroll,
            /* y= */ 0,
            /* width= */ parentWidth,
            /* height= */ IMAGE_HEIGHT));

    mapBackground.render(guiContext, graphics);

    verify(graphics).drawImage(eq(IMAGE), /* x= */ eq(imageHorizontalScroll), /* y= */ anyFloat());
    verify(graphics)
        .drawImage(
            eq(IMAGE), /* x= */ eq(IMAGE_WIDTH + imageHorizontalScroll), /* y= */ anyFloat());
    verify(graphics)
        .drawImage(
            eq(IMAGE), /* x= */ eq(2 * IMAGE_WIDTH + imageHorizontalScroll), /* y= */ anyFloat());
    verify(graphics)
        .drawImage(
            eq(IMAGE), /* x= */ eq(3 * IMAGE_WIDTH + imageHorizontalScroll), /* y= */ anyFloat());
    verifyNoMoreInteractions(graphics);
  }

  private static float positionFromPercentage(float percentage) {
    return percentage * (MAP_HEIGHT - IMAGE_HEIGHT);
  }
}
