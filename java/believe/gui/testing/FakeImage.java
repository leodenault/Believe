package believe.gui.testing;

import org.newdawn.slick.Image;

import java.util.Objects;

public final class FakeImage extends Image {
  public FakeImage(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Image getScaledCopy(float scale) {
    return new FakeImage((int) (width * scale), (int) (height * scale));
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof FakeImage)) {
      return false;
    }

    FakeImage other = (FakeImage) obj;

    return width == other.width && height == other.height;
  }

  @Override
  public int hashCode() {
    return Objects.hash(Integer.hashCode(width), Integer.hashCode(height));
  }
}
