package believe.map.tiled

import believe.datamodel.DataManager
import dagger.Binds
import dagger.Module
import org.w3c.dom.Element

@Module
internal abstract class TiledDaggerModule {
    @Binds
    abstract fun bindTiledObjectImplDataManager(
        impl: ObjectTemplateManager
    ): DataManager<PartialTiledObject>

    @Binds
    abstract fun bindPartialTiledObjectElementParser(
        impl: PartialTiledObject.Parser
    ): ElementParser<PartialTiledObject>

    @Binds
    abstract fun bindTiledObjectElementParser(impl: TiledObject.Parser): ElementParser<TiledObject>

    @Binds
    abstract fun bindTiledObjectGroupElementParser(
        impl: TiledObjectGroup.Parser
    ): ElementParser<TiledObjectGroup>

    @Binds
    abstract fun bindLayerElementParser(impl: Layer.Parser): ElementParser<Layer>
}