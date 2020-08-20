package believe.map.gui;

import believe.gui.ComponentBase;
import believe.map.data.LayerData;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import javax.inject.Inject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

/** A renderable component representing a layer of a map. */
@AutoFactory
final class MapLayerComponent extends ComponentBase {
  private final LayerData layerData;

  @Inject
  MapLayerComponent(@Provided GUIContext container, LayerData layerData) {
    super(container);
    this.layerData = layerData;
  }

  @Override
  public void resetLayout() {}

  @Override
  protected void renderComponent(GUIContext context, Graphics g) {
    layerData.layer().render(getX(), getY());
  }
}
