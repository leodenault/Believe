package believe.mob

import believe.animation.AnimationFactory
import believe.datamodel.DataManager
import believe.mob.proto.MobAnimationDataProto.DamageFrame
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class MobAnimationDataManager @Inject internal constructor(
    private val spriteSheetManager: DataManager<DataManager<AnimationFactory>>,
    private val mobDataManager: DataManager<DataManager<List<DamageFrame>>>
) : DataManager<DataManager<MobAnimationData>> {

    override fun getDataFor(name: String): DataManager<MobAnimationData>? {
        val animationManager = spriteSheetManager.getDataFor(name) ?: return null
        val damageFrameManager = mobDataManager.getDataFor(name)
        return object : DataManager<MobAnimationData> {
            override fun getDataFor(name: String): MobAnimationData? {
                val animation = animationManager.getDataFor(name) ?: return null
                return MobAnimationData(
                    animation, damageFrameManager?.getDataFor(name) ?: emptyList()
                )
            }
        }
    }
}
