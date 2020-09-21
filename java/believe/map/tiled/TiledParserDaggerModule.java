package believe.map.tiled;

import believe.datamodel.DataManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import kotlin.jvm.functions.Function1;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

@Module
abstract class TiledParserDaggerModule {
  @Binds
  abstract DataManager<PartialTiledObject> bindTiledObjectImplDataManager(
      ObjectTemplateManager impl);

  @Binds
  abstract Function1<Element, PartialTiledObject> bindPartialTiledObjectElementParser(
      PartialTiledObject.Parser impl);

  @Binds
  abstract Function1<Element, TiledObject> bindTiledObjectElementParser(TiledObject.Parser impl);

  @Binds
  abstract Function1<Element, TiledObjectGroup> bindTiledObjectGroupElementParser(
      TiledObjectGroup.Parser impl);

  @Binds
  abstract Function1<Element, Layer> bindLayerElementParser(Layer.Parser impl);
}
