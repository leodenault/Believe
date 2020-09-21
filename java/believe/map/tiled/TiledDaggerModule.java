package believe.map.tiled;

import believe.datamodel.DataManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import kotlin.jvm.functions.Function1;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;

@Module
abstract class TiledDaggerModule {
  @Provides
  @Reusable
  static DocumentBuilder provideDocumentBuilder() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(false);
    try {
      DocumentBuilder documentBuilder = factory.newDocumentBuilder();
      documentBuilder.setEntityResolver(
          (publicId, systemId) -> new InputSource(new ByteArrayInputStream(new byte[0])));
      return documentBuilder;
    } catch (ParserConfigurationException e) {
      Log.error("Failed to instantiate XML document builder.", e);
      throw new RuntimeException(e);
    }
  }

  @Binds
  abstract DataManager<PartialTileSet> bindPartialTileSetManager(TileSetManager impl);

  @Provides
  static Function1<String, Image> provideImageProvider() {
    return location -> {
      try {
        return new Image(location);
      } catch (SlickException e) {
        return null;
      }
    };
  }
}
