package believe.animation

import believe.datamodel.DataManager
import believe.datamodel.protodata.BinaryProtoFileManager
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class SpriteSheetDataManagerFactory @Inject internal constructor(
    @SpriteSheetDirectoryName
    private val spriteSheetDirectoryName: String, private val binaryProtoFileManagerFactory: BinaryProtoFileManager.Factory,
    parserFactory: SpriteSheetDataParser.Factory
) {

    private val parser = parserFactory.create()

    fun create(): DataManager<DataManager<Animation>> =
        binaryProtoFileManagerFactory.create(spriteSheetDirectoryName, parser)
}
