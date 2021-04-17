package believe.character

import believe.animation.Animation
import believe.animation.AnimationFactory
import believe.animation.emptyAnimation
import believe.character.proto.CharacterAnimationsProto
import believe.datamodel.DataManager
import dagger.Reusable
import org.newdawn.slick.util.Log
import javax.inject.Inject

/** A set of animations used in animating a [CharacterV2]. */
interface Animations {
    /** The animation used when the character is idle. */
    val idleAnimation: Animation

    /** The animation used when the character is moving. */
    val movementAnimation: Animation

    /** The animation used when the character is jumping. */
    val jumpingAnimation: Animation

    companion object {
        internal val EMPTY: Animations = AnimationsImpl(
            emptyAnimation(), emptyAnimation(), emptyAnimation()
        )
    }

    @Reusable
    class Parser @Inject internal constructor(
        private val animationManager: DataManager<DataManager<AnimationFactory>>
    ) {
        fun parse(data: CharacterAnimationsProto.CharacterAnimations): Animations {
            val spriteSheetManager =
                animationManager.getDataFor(data.spriteSheetName) ?: return EMPTY.also {
                    Log.error("Could not find a managed sprite sheet with name '${data.spriteSheetName}'.")
                }

            return AnimationsImpl(
                idleAnimation = spriteSheetManager.getDataFor(data.idleAnimationName)?.invoke()
                    ?: emptyAnimation(), movementAnimation = spriteSheetManager.getDataFor(
                    data.movementAnimationName
                )?.invoke() ?: emptyAnimation(), jumpingAnimation = spriteSheetManager.getDataFor(
                    data.jumpAnimationName
                )?.invoke() ?: emptyAnimation()
            )
        }
    }
}

private class AnimationsImpl constructor(
    override val idleAnimation: Animation,
    override val movementAnimation: Animation,
    override val jumpingAnimation: Animation
) : Animations
