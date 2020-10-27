package believe.mob

import believe.animation.Animation
import believe.mob.proto.MobAnimationDataProto.DamageFrame

internal class MobAnimation(val animation: Animation, val damageFrames: List<DamageFrame>)
