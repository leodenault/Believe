package believe.mob

import believe.animation.Animation
import believe.datamodel.DataManager
import believe.mob.proto.MobAnimationDataProto.DamageFrame
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class MobAnimationDataManager @Inject internal constructor(
    private val spriteSheetManager: DataManager<DataManager<Animation>>,
    private val mobDataManager: DataManager<DataManager<List<DamageFrame>>>
) : DataManager<DataManager<MobAnimation>> {

    override fun getDataFor(name: String): DataManager<MobAnimation>? {
        val animationManager = spriteSheetManager.getDataFor(name) ?: return null
        val damageFrameManager = mobDataManager.getDataFor(name)
        return object : DataManager<MobAnimation> {
            override fun getDataFor(name: String): MobAnimation? {
                val animation = animationManager.getDataFor(name) ?: return null
                return MobAnimation(
                    animation, damageFrameManager?.getDataFor(name) ?: emptyList()
                )
            }
        }
    }
}
