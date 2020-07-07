package believe.character

import believe.character.proto.CharacterAnimationsProto
import believe.datamodel.DataManager
import dagger.Reusable
import org.newdawn.slick.Animation
import org.newdawn.slick.util.Log
import javax.inject.Inject

class Animations private constructor(
    internal val idleAnimation: Animation,
    internal val movementAnimation: Animation,
    internal val jumpingAnimation: Animation
) {

    @Reusable
    class Parser @Inject internal constructor(
        private val animationManager: DataManager<DataManager<Animation>>
    ) {
        fun parse(data: CharacterAnimationsProto.CharacterAnimations): Animations {
            val spriteSheetManager =
                animationManager.getDataFor(data.spriteSheetName) ?: return Animations(
                    Animation(), Animation(), Animation()
                ).also {
                    Log.error("Could not find a managed sprite sheet with name '${data.spriteSheetName}'.")
                }

            return Animations(
                idleAnimation = spriteSheetManager.getDataFor(data.idleAnimationName)
                    ?: Animation(), movementAnimation = spriteSheetManager.getDataFor(
                    data.movementAnimationName
                ) ?: Animation(), jumpingAnimation = spriteSheetManager.getDataFor(
                    data.jumpAnimationName
                ) ?: Animation()
            )
        }
    }
}