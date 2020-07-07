package believe.animation

import believe.animation.proto.AnimationProto
import believe.datamodel.DataManager
import dagger.Reusable
import org.newdawn.slick.Animation
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet
import org.newdawn.slick.util.Log
import believe.util.KotlinHelpers.whenNull
import java.lang.RuntimeException
import javax.inject.Inject

internal class SpriteSheetDataParser private constructor(
    private val supplyImage: (String) -> Image?,
    private val supplySpriteSheet: (Image, Int, Int) -> SpriteSheet
) : (AnimationProto.SpriteSheet) -> DataManager<Animation>? {

    override fun invoke(data: AnimationProto.SpriteSheet): DataManager<Animation>? =
        supplyImage(data.fileLocation).whenNull {
            Log.error("Failed to load sprite sheet at ${data.fileLocation}.")
        }?.let { spriteSheetImage ->
            supplySpriteSheet(
                spriteSheetImage, data.tileWidth, data.tileHeight
            ).takeIf { spriteSheet ->
                spriteSheet.horizontalCount > 0 && spriteSheet.verticalCount > 0
            }.whenNull {
                Log.error("Sprite sheet image at '${data.fileLocation}' results in empty sprite sheet.")
            }?.let { spriteSheet -> AnimationManager(spriteSheet, data.animationsMap) }
        }

    private class AnimationManager(
        private val spriteSheet: SpriteSheet,
        private val animationData: Map<String, AnimationProto.Animation>
    ) : DataManager<Animation> {

        private val loadedAnimations: MutableMap<String, Animation> = mutableMapOf()

        override fun getDataFor(name: String): Animation? {
            val loadedAnimation = loadedAnimations[name]
            if (loadedAnimation != null) return loadedAnimation

            return animationData[name].whenNull {
                Log.error("Animation data does not exist for name '$name'.")
            }?.let { animationData ->
                try {
                    Animation(
                        spriteSheet,
                        animationData.startFrame % spriteSheet.horizontalCount,
                        animationData.startFrame / spriteSheet.horizontalCount,
                        animationData.endFrame % spriteSheet.horizontalCount,
                        animationData.endFrame / spriteSheet.horizontalCount,
                        true,
                        animationData.duration,
                        false
                    ).also { loadedAnimations[name] = it }
                } catch (e: RuntimeException) {
                    Log.error("Animation data is invalid. Are the indices correct? Offending data:\n${animationData}")
                    null
                }
            }
        }
    }

    @Reusable
    internal class Factory @Inject internal constructor() {
        internal fun create(
            imageSupplier: (String) -> Image?, spriteSheetSupplier: (Image, Int, Int) -> SpriteSheet
        ) = SpriteSheetDataParser(imageSupplier, spriteSheetSupplier)

        internal fun create() = create({ Image(it) },
            { image, tileWidth, tileHeight -> SpriteSheet(image, tileWidth, tileHeight) })
    }
}
