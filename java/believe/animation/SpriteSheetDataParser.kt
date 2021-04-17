package believe.animation

import believe.animation.proto.AnimationProto
import believe.datamodel.DataManager
import dagger.Reusable
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet
import org.newdawn.slick.util.Log
import believe.util.KotlinHelpers.whenNull
import javax.inject.Inject

internal class SpriteSheetDataParser private constructor(
    private val supplyImage: (String) -> Image?,
    private val supplySpriteSheet: (Image, Int, Int) -> SpriteSheet
) : (AnimationProto.SpriteSheet) -> DataManager<AnimationFactory>? {

    override fun invoke(data: AnimationProto.SpriteSheet): DataManager<AnimationFactory>? =
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
    ) : DataManager<AnimationFactory> {

        private val loadedAnimations: MutableMap<String, AnimationFactory> = mutableMapOf()

        override fun getDataFor(name: String): AnimationFactory? {
            return loadedAnimations[name] ?: animationData[name].whenNull {
                Log.error("Animation data does not exist for name '$name'.")
            }?.let { animationData ->
                {
                    animation(
                        spriteSheet,
                        frameRange = animationData.startFrame..animationData.endFrame,
                        frameDuration = animationData.duration,
                        iterationMode = animationData.iterationMode,
                        isLooping = animationData.isLooping
                    )
                }
            }?.also { loadedAnimations[name] = it }
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
