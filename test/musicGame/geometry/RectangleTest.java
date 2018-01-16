package musicGame.geometry;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RectangleTest {

  private void testRectIntersectionWithParameters(
      int x1, int y1, int width1, int height1,
      int x2, int y2, int width2, int height2,
      float ix, float iy, float iwidth, float iheight) {
    Rectangle rect1 = new Rectangle(x1, y1, width1, height1);
    Rectangle rect2 = new Rectangle(x2, y2, width2, height2);

    Rectangle inter = rect1.intersection(rect2);
    assertThat(inter.getX(), is(ix));
    assertThat(inter.getY(), is(iy));
    assertThat(inter.getWidth(), is(iwidth));
    assertThat(inter.getHeight(), is(iheight));
  }

  @Test
  public void getIntersectionShouldReturnIntersectionOfTwoRectangles() {
    testRectIntersectionWithParameters(10, 10, 50, 30, 0, 10, 20, 10, 10, 10, 10, 10);
    testRectIntersectionWithParameters(10, 20, 50, 30, 5, 26, 100, 30, 10, 26, 50, 24);
  }

  @Test
  public void getIntersectionShouldReturnSmallestRectangleIfCompletelyInLargerOne() {
    testRectIntersectionWithParameters(0, 0, 100, 100, 0, 0, 50, 50, 0, 0, 50, 50);
    testRectIntersectionWithParameters(0, 0, 100, 100, 0, 0, 100, 100, 0, 0, 100, 100);
    testRectIntersectionWithParameters(265, 15, 490, 100, 265, 15, 490, 100, 265, 15, 490, 100);
    testRectIntersectionWithParameters(0, 0, 100, 100, 10, 10, 50, 50, 10, 10, 50, 50);
    testRectIntersectionWithParameters(10, 10, 50, 50, 0, 0, 100, 100, 10, 10, 50, 50);
  }

  @Test
  public void getIntersectionShouldReturnZeroWidthHeightRectangleIfNoIntersection() {
    testRectIntersectionWithParameters(
        0, 0, 10, 15,
        100, 1000, 10, 15,
        0, 0, 0, 0);
    testRectIntersectionWithParameters(
        0, 0, 10, 10,
        10, 0, 10, 10,
        0, 0, 0, 0);
    testRectIntersectionWithParameters(
        0, 0, 10, 10,
        0, 10, 10, 10,
        0, 0, 0, 0);
  }

  @Test
  public void horizontalCollisionDirectionShouldReturnTrueIfRightOfCenter() {
    Rectangle rect1 = new Rectangle(0, 0, 20, 20);
    Rectangle rect2 = new Rectangle(10, 0, 20, 20);
    assertThat(rect1.horizontalCollisionDirection(rect2), is(true));
    rect2 = new Rectangle(1, 20, 20, 20);
    assertThat(rect1.horizontalCollisionDirection(rect2), is(true));
  }

  @Test
  public void horizontalCollisionDirectionShouldReturnFalseIfLeftOfCenter() {
    Rectangle rect1 = new Rectangle(0, 0, 20, 20);
    Rectangle rect2 = new Rectangle(-1, 0, 20, 20);
    assertThat(rect1.horizontalCollisionDirection(rect2), is(false));
    rect2 = new Rectangle(0, 20, 20, 20);
    assertThat(rect1.horizontalCollisionDirection(rect2), is(false));
  }

  @Test
  public void verticalCollisionDirectionShouldReturnTrueIfBelowCenter() {
    Rectangle rect1 = new Rectangle(0, 0, 20, 20);
    Rectangle rect2 = new Rectangle(-1, 550, 20, 20);
    assertThat(rect1.verticalCollisionDirection(rect2), is(true));
    rect2 = new Rectangle(0, 1, 20, 20);
    assertThat(rect1.verticalCollisionDirection(rect2), is(true));
  }

  @Test
  public void verticalCollisionDirectionShouldReturnFalseIfAboveCenter() {
    Rectangle rect1 = new Rectangle(0, 0, 20, 20);
    Rectangle rect2 = new Rectangle(-1, -42, 20, 20);
    assertThat(rect1.verticalCollisionDirection(rect2), is(false));
    rect2 = new Rectangle(0, 0, 20, 20);
    assertThat(rect1.verticalCollisionDirection(rect2), is(false));
  }

  @Test
  public void intersectsShouldReturnFalseWhenTouchingSameLine() {
    Rectangle rect1 = new Rectangle(0, 0, 20, 20);
    Rectangle rect2 = new Rectangle(20, 0, 20, 20);
    Rectangle rect3 = new Rectangle(0, 20, 20, 20);

    assertThat(rect1.intersects(rect2), is(false));
    assertThat(rect1.intersects(rect3), is(false));
  }
}
