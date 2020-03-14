package believe.map.gui;

import believe.core.display.Renderable;
import believe.geometry.Rectangle;
import believe.map.data.BackgroundSceneData;
import believe.react.Observer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

/**
 * A GUI wrapper around an {@link Image} for displaying a horizontally repeating image in the
 * background of the scene.
 */
public final class MapBackground implements Observer<Rectangle>, Renderable {
  private final BackgroundSceneData backgroundSceneData;
  private final float verticalWindowSpace;
  private final int mapHeight;

  private float xMin;
  private float xMax;
  private int y;

  MapBackground(BackgroundSceneData backgroundSceneData, int mapHeight) {
    this.backgroundSceneData = backgroundSceneData;
    // The segment of the map through which the image can slide through, expressed as a percentage.
    verticalWindowSpace =
        backgroundSceneData.bottomYPosition() - backgroundSceneData.topYPosition();
    this.mapHeight = mapHeight;
  }

  @Override
  public void render(Graphics g) {
    for (float currentImageX = xMin;
        currentImageX < xMax;
        currentImageX += backgroundSceneData.image().getWidth()) {
      g.drawImage(backgroundSceneData.image(), currentImageX, y);
    }
  }

  @Override
  public void valueChanged(Rectangle parentRect) {
    float parentYPositionPercent = parentRect.getY() / (mapHeight - parentRect.getHeight());
    float yPercent =
        verticalWindowSpace * parentYPositionPercent + backgroundSceneData.topYPosition();
    y = (int) (yPercent * (mapHeight - backgroundSceneData.image().getHeight()));
    xMin =
        parentRect.getX()
            - backgroundSceneData.horizontalSpeedMultiplier()
                * (parentRect.getX()
                    % (backgroundSceneData.image().getWidth()
                        / backgroundSceneData.horizontalSpeedMultiplier()));
    xMax = parentRect.getMaxX();
  }
}
