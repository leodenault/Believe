package believe.map.gui;

import static believe.geometry.RectangleKt.mutableRectangle;

import believe.core.Updatable;
import believe.core.display.Renderable;
import believe.gui.ComponentBase;
import believe.map.data.BackgroundSceneData;
import believe.map.data.LayerData;
import believe.map.data.MapData;
import believe.scene.GeneratedMapEntityData;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AutoFactory
public class LevelMap extends ComponentBase {
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
    GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
        GeneratedMapEntityData.newBuilder();
    mapData.tiledMapData().objectLayers().stream()
        .flatMap(layer -> layer.objectFactories().stream())
        .forEach(factory -> factory.createAndAddTo(generatedMapEntityDataBuilder));
    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();

    renderables = generatedMapEntityData.renderables();
    updatables = generatedMapEntityData.updatables();
    rect =
        mutableRectangle(
            rect.getX(),
            rect.getY(),
            mapData.tiledMapData().width(),
            mapData.tiledMapData().height());
    setLocation(0, 0);

    rearLayers = new ArrayList<>();
    frontLayers = new ArrayList<>();
    for (LayerData layer : mapData.tiledMapData().layers()) {
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
                (BackgroundSceneData backgroundSceneData) ->
                    new MapBackground(backgroundSceneData, mapData.tiledMapData().height()))
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
  public void resetLayout() {}

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
