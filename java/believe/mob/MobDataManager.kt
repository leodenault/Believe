package believe.mob

import believe.datamodel.DataManager
import believe.datamodel.protodata.BinaryProtoFileManager
import believe.mob.proto.MobAnimationDataProto.DamageFrame
import believe.mob.proto.MobAnimationDataProto.MobAnimationData
import dagger.Reusable
import org.newdawn.slick.util.Log
import javax.inject.Inject

internal class MobDataManager private constructor(
    binaryProtoFileManager: BinaryProtoFileManager<
        MobAnimationData, DataManager<List<@JvmSuppressWildcards DamageFrame>>>
) : DataManager<DataManager<List<@JvmSuppressWildcards DamageFrame>>> by binaryProtoFileManager {

    @Reusable
    class Factory @Inject internal constructor(
        private val binaryProtoFileManagerFactory: BinaryProtoFileManager.Factory,
        @MobAnimationDataLocation private val mobDataLocation: String
    ) {

        fun create() =
            MobDataManager(
                binaryProtoFileManagerFactory.create(mobDataLocation) { mobAnimationData ->
                    object : DataManager<List<DamageFrame>> {
                        override fun getDataFor(name: String): List<DamageFrame>? {
                            val damageFrames =
                                mobAnimationData.damageFramesByAnimationMap[name]?.damageFramesList
                            if (damageFrames == null) {
                                Log.error(
                                    "Could not find damage frames associated with animation named" +
                                        " '$name'."
                                )
                            }
                            return damageFrames
                        }
                    }
                }
            )
    }
}
