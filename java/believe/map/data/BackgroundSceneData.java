package believe.map.data;

import com.google.auto.value.AutoValue;
import org.newdawn.slick.Image;

/** The data model for representing a background displayed behind a map. */
@AutoValue
public abstract class BackgroundSceneData {
  /** The file path to the image that should be displayed as a background. */
  public abstract Image image();

  /**
   * The ordering with which the image should be displayed. The lower the number, the higher the
   * priority of the image being rendered.
   */
  public abstract int layer();

  /** The vertical position of the background image. */
  public abstract int yPosition();

  public static BackgroundSceneData create(Image image, int layer, int yPosition) {
    return new AutoValue_BackgroundSceneData(image, layer, yPosition);
  }
}
