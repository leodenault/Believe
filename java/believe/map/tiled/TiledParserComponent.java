package believe.map.tiled;

import believe.io.DefaultProvidersDaggerModule;
import believe.io.IoDaggerModule;
import dagger.BindsInstance;
import dagger.Component;
import kotlin.jvm.functions.Function1;
import org.w3c.dom.Element;

/** A Dagger component encapsulating the Tiled integration package. */
@Component(
    modules = {DefaultProvidersDaggerModule.class, IoDaggerModule.class, TiledDaggerModule.class})
interface TiledParserComponent {
  /** The element parser used in parsing {@link TiledObjectGroup} instances. */
  Function1<Element, TiledObjectGroup> getTiledObjectGroupParser();
  /** The element parser used in parsing {@link Layer} instances. */
  Function1<Element, Layer> getLayerParser();

  @Component.Factory
  interface Factory {
    /**
     * Creates an instance of {@link TiledParserComponent}.
     *
     * @param tileMapLocation the location of the Tiled map on disk.
     * @param tileSetGroup the grouping of tile sets used in generating tiles for a map.
     */
    TiledParserComponent create(
        @BindsInstance @TiledMapLocation String tileMapLocation,
        @BindsInstance TileSetGroup tileSetGroup,
        @BindsInstance @TiledMapTileWidth int tiledMapTileWidth,
        @BindsInstance @TiledMapTileHeight int tiledMapTileHeight);
  }
}
