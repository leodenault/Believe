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
    private val spriteSheetDataManager: DataManager<DataManager<Animation>>,
    private val mobSpriteSheetManager: DataManager<DataManager<MobAnimation>>,
    private val stationaryEnemyFactory: StationaryEnemy.Factory
) : ObjectParser {

    override fun parseObject(
        entityType: EntityType,
        tiledObject: TiledObject,
        generatedMapEntityDataBuilder: GeneratedMapEntityData.Builder
    ) {
        if (entityType != EntityType.ENEMY) return

        val mobAnimationManager = mobSpriteSheetManager.getDataFor("stationary_enemy")
        val attackAnimation = mobAnimationManager?.getDataFor("attacking")
        generatedMapEntityDataBuilder.addSceneElement(
            stationaryEnemyFactory.create(
                tiledObject.x,
                tiledObject.y,
                spriteSheetDataManager.getDataFor("stationary_enemy")?.getDataFor("idle")
                    ?: emptyAnimation(),
                attackAnimation?.animation ?: emptyAnimation(),
                attackAnimation?.damageFrames ?: emptyList()
            )
        )
    }
}