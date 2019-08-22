package believe.map.data;

import believe.map.data.proto.MapMetadataProto.MapBackground;
import believe.map.data.proto.MapMetadataProto.MapMetadata;
import com.google.auto.value.AutoValue;
import org.newdawn.slick.Image;

/** The data model for representing a background displayed behind a map. */
@AutoValue
public abstract class BackgroundSceneData {
  abstract MapBackground mapBackground();

  /** The file path to the image that should be displayed as a background. */
  public abstract Image image();

  /**
   * The vertical position of the background when the game camera is at the top (y = 0) of the map.
   * Expressed as a value within the range [0, 1].
   */
  public float topYPosition() {
    return mapBackground().getTopYPosition();
  }

  /**
   * The vertical position of the background when the game camera is at the bottom (y = map height)
   * of the map. Expressed as a value within the range [0, 1].
   */
  public float bottomYPosition() {
    return mapBackground().getBottomYPosition();
  }

  /**
   * A multiplier for the background's horizontal scroll speed, in pixels per second. A value of 1
   * will ensure that the image moves by 1 pixel for every second that the character is moving.
   */
  public float horizontalSpeedMultiplier() {
    return mapBackground().getHorizontalSpeedMultiplier();
  }

  public static BackgroundSceneData create(Image image, MapBackground mapBackground) {
    return new AutoValue_BackgroundSceneData(mapBackground, image);
  }
}
