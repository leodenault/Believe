package believe.map.tiled;

import believe.io.DefaultProvidersDaggerModule;
import believe.io.IoDaggerModule;
import dagger.BindsInstance;
import dagger.Component;
import javax.xml.parsers.DocumentBuilder;

@Component(
    modules = {
      DefaultProvidersDaggerModule.class,
      IoDaggerModule.class,
      TiledDaggerModule.class
    })
interface TileSetComponent {
  /** The {@link TileSet.Parser} associated with this component. */
  TileSet.Parser getTileSetParser();

  /** The {@link DocumentBuilder} used in parsing Tiled's XML documents. */
  DocumentBuilder getDocumentBuilder();

  @Component.Factory
  interface Factory {
    /**
     * Creates a {@link TileSetComponent}.
     *
     * @param tiledMapLocation the location of the Tiled map on disk.
     */
    TileSetComponent create(
        @BindsInstance @TiledMapLocation String tiledMapLocation,
        @BindsInstance @IsHeadless boolean isHeadless);
  }
}
