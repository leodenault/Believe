package believe.map.tiled;

import believe.io.DefaultProvidersDaggerModule;
import believe.io.IoDaggerModule;
import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {DefaultProvidersDaggerModule.class, IoDaggerModule.class})
interface TileSetParserComponent {
  /** The {@link TileSet.Parser} associated with this component. */
  TileSet.Parser getTileSetParser();

  @Component.Factory
  interface Factory {
    /**
     * Creates a {@link TileSetParserComponent}.
     *
     * @param tiledMapLocation the location of the Tiled map on disk.
     * @param tileSetsLocation the location of the tile sets on disk.
     */
    TileSetParserComponent create(
        @BindsInstance @TiledMapLocation String tiledMapLocation,
        @BindsInstance @TileSetsLocation String tileSetsLocation);
  }
}
