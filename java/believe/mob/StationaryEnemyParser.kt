package believe.mob

import believe.animation.EMPTY_ANIMATION_FACTORY
import believe.datamodel.DataManager
import believe.map.data.ObjectFactory
import believe.map.io.ObjectParser
import believe.map.tiled.TiledObject
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class StationaryEnemyParser @Inject internal constructor(
    private val mobSpriteSheetManager: DataManager<DataManager<MobAnimationData>>,
    private val stationaryEnemyFactory: StationaryEnemy.Factory
) : ObjectParser {

    override fun parseObject(tiledObject: TiledObject): ObjectFactory {
        val mobAnimationManager = mobSpriteSheetManager.getDataFor("stationary_enemy")
        val createIdleAnimation =
            mobAnimationManager?.getDataFor("idle")?.createAnimation ?: EMPTY_ANIMATION_FACTORY
        val attackAnimationData = mobAnimationManager?.getDataFor("attacking")
        val createAttackAnimation = attackAnimationData?.createAnimation ?: EMPTY_ANIMATION_FACTORY
        val damageFrames = attackAnimationData?.damageFrames ?: emptyList()

        return ObjectFactory {
            it.addSceneElement(
                stationaryEnemyFactory.create(
                    tiledObject.x,
                    tiledObject.y,
                    createIdleAnimation(),
                    createAttackAnimation(),
                    damageFrames
                )
            )
        }
    }
}