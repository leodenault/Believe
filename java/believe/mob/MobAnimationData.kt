package believe.mob

import believe.animation.AnimationFactory
import believe.mob.proto.MobAnimationDataProto.DamageFrame

internal class MobAnimationData(
    val createAnimation: AnimationFactory, val damageFrames: List<DamageFrame>
)
