package believe.mob

import believe.animation.Animation
import believe.animation.emptyAnimation
import believe.datamodel.DataManager
import believe.map.data.EntityType
import believe.map.data.GeneratedMapEntityData
import believe.map.io.ObjectParser
import believe.map.tiled.TiledObject
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class StationaryEnemyParser @Inject internal constructor(
    private val animationsManager: DataManager<DataManager<Animation>>
) : ObjectParser {

    override fun parseObject(
        entityType: EntityType,
        tiledObject: TiledObject,
        generatedMapEntityDataBuilder: GeneratedMapEntityData.Builder
    ) {
        if (entityType != EntityType.ENEMY) return

        val spriteSheetManager = animationsManager.getDataFor("stationary_enemy")
        generatedMapEntityDataBuilder.addSceneElement(
            StationaryEnemy(
                tiledObject.x, tiledObject.y, StationaryEnemyStateMachine(
                    spriteSheetManager?.getDataFor("idle") ?: emptyAnimation(),
                    spriteSheetManager?.getDataFor("attacking") ?: emptyAnimation()
                )
            )
        )
    }
}