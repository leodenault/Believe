package believe.mob

import believe.map.data.EntityType
import believe.map.data.GeneratedMapEntityData
import believe.map.io.ObjectParser
import believe.map.tiled.TiledObject
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class StationaryEnemyParser @Inject internal constructor() : ObjectParser {
    override fun parseObject(
        entityType: EntityType,
        tiledObject: TiledObject,
        generatedMapEntityDataBuilder: GeneratedMapEntityData.Builder
    ) {
        if (entityType != EntityType.ENEMY) return
        generatedMapEntityDataBuilder.addSceneElement(StationaryEnemy(tiledObject.x, tiledObject.y))
    }
}