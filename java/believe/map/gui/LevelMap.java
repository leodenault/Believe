package believe.map.gui;

import believe.core.Updatable;
import believe.core.display.Camera.Layerable;
import believe.core.display.Renderable;
import believe.gui.ComponentBase;
import believe.map.data.LayerData;
import believe.map.data.MapData;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AutoFactory
public class LevelMap extends ComponentBase implements Layerable {
  static final int TARGET_WIDTH = 1600;
  static final int TARGET_HEIGHT = 900;

  private final List<MapLayerComponent> rearLayers;
  private final List<MapLayerComponent> frontLayers;
  private final Set<Renderable> renderables;
  private final Set<Updatable> updatables;
  private final List<MapBackground> mapBackgrounds;

  @Nullable private ComponentBase focus;

  @Inject
  public LevelMap(
      @Provided GUIContext container,
      @Provided MapLayerComponentFactory mapLayerComponentFactory,
      MapData mapData) {
    super(container, 0, 0);
    renderables =
        mapData.layers().stream()
            .map(LayerData::generatedMapEntityData)
            .flatMap(generatedMapEntityData -> generatedMapEntityData.renderables().stream())
            .collect(Collectors.toSet());
    updatables =
        mapData.layers().stream()
            .map(LayerData::generatedMapEntityData)
            .flatMap(generatedMapEntityData -> generatedMapEntityData.updatables().stream())
            .collect(Collectors.toSet());
    rect.setSize(mapData.width(), mapData.height());
    setLocation(0, 0);

    rearLayers = new ArrayList<>();
    frontLayers = new ArrayList<>();
    for (LayerData layer : mapData.layers()) {
      if (!layer.isVisible()) {
        continue;
      }

      if (layer.isFrontLayer()) {
        frontLayers.add(mapLayerComponentFactory.create(layer));
      } else {
        rearLayers.add(mapLayerComponentFactory.create(layer));
      }
    }
    mapBackgrounds =
        mapData.backgroundScenes().stream()
            .map(
                backgroundSceneData ->
                    new MapBackground(
                        container,
                        backgroundSceneData.image(),
                        backgroundSceneData.layer(),
                        backgroundSceneData.yPosition()))
            .collect(Collectors.toList());
  }

  public void reset() {
    setLocation(0, 0);
  }

  /**
   * Sets the object that should be centered on screen, like the player
   *
   * @param focus The component to focus on
   */
  public void setFocus(ComponentBase focus) {
    this.focus = focus;
  }

  public void update(int delta) {
    for (Updatable updatable : updatables) {
      updatable.update(delta);
    }
  }

  public List<MapBackground> getBackgrounds() {
    return mapBackgrounds;
  }

  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public void resetLayout() {}

  @Override
  public void renderComponent(GUIContext context, Graphics g, float xMin, float xMax)
      throws SlickException {
    renderComponent(context, g);
  }

  @Override
  protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
    for (MapLayerComponent rearLayer : rearLayers) {
      rearLayer.renderComponent(context, g);
    }

    for (Renderable renderable : renderables) {
      renderable.render(context, g);
    }

    if (focus != null) {
      focus.render(context, g);
    }

    for (MapLayerComponent frontLayer : frontLayers) {
      frontLayer.renderComponent(context, g);
    }
  }
}
