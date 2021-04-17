package believe.animation

import believe.animation.proto.AnimationProto
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.FakeSpriteSheet
import believe.animation.testing.frames
import believe.animation.testing.images
import believe.datamodel.DataManager
import believe.gui.testing.FakeImage
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class SpriteSheetDataParserTest {
    private var spriteSheet: FakeSpriteSheet =
        FakeSpriteSheet(numRows = LOADED_IMAGE_HEIGHT, numColumns = LOADED_IMAGE_WIDTH)
    private val parse: SpriteSheetDataParser by lazy {
        SpriteSheetDataParser.Factory().create({
            if (it == VALID_SPRITE_SHEET_LOCATION) LOADED_IMAGE else null
        }, { _, _, _ -> spriteSheet })
    }
    private val dataBuilder = with(AnimationProto.SpriteSheet.newBuilder()) {
        fileLocation = VALID_SPRITE_SHEET_LOCATION
        tileWidth = 1
        tileHeight = 1
        putAnimations(ANIMATION_1_NAME, AnimationProto.Animation.newBuilder().apply {
            startFrame = 0
            endFrame = (NUM_FRAMES / 2) - 1
            duration = 10
            iterationMode = IterationMode.LINEAR
            isLooping = false
        }.build())
        putAnimations(ANIMATION_2_NAME, AnimationProto.Animation.newBuilder().apply {
            startFrame = NUM_FRAMES / 2
            endFrame = NUM_FRAMES - 1
            duration = 15
            iterationMode = IterationMode.PING_PONG
            isLooping = true
        }.build())
    }
    private val data: AnimationProto.SpriteSheet by lazy { dataBuilder.build() }

    @Test
    fun invoke_correctlyGeneratesAnimationDataManager() {
        val animationManager: DataManager<AnimationFactory> = parse(data)!!
        val animation1 = animationManager.getDataFor(ANIMATION_1_NAME)!!()
        val animation2 = animationManager.getDataFor(ANIMATION_2_NAME)!!()

        val animation1Frames = animation1.frames(iterations = 1)
        val animation2Frames = animation2.frames(iterations = 1)
        assertThat(
            animation1Frames.images()
        ).containsExactlyElementsIn(spriteSheet.imagesBetween(0 until (NUM_FRAMES / 2)))
        val animation2SpriteSheetImages =
            spriteSheet.imagesBetween((NUM_FRAMES / 2) until NUM_FRAMES)
        assertThat(
            animation2Frames.images()
        ).containsExactlyElementsIn(
            animation2SpriteSheetImages + animation2SpriteSheetImages.asReversed()
                .slice(1 until animation2SpriteSheetImages.size)
        )
    }

    @Test
    @VerifiesLoggingCalls
    fun invoke_suppliedImageIsNull_returnsNullAndLogsMessage(logSystem: VerifiableLogSystem) {
        dataBuilder.fileLocation = INVALID_SPRITE_SHEET_LOCATION

        assertThat(parse(data)).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Failed to load sprite sheet at $INVALID_SPRITE_SHEET_LOCATION.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    @VerifiesLoggingCalls
    fun invoke_spriteSheetIsEmpty_returnsNullAndLogsMessage(logSystem: VerifiableLogSystem) {
        spriteSheet = FakeSpriteSheet(numRows = 0, numColumns = 10)

        assertThat(parse(data)).isNull()

        spriteSheet = FakeSpriteSheet(numRows = 10, numColumns = 0)

        assertThat(parse(data)).isNull()

        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeast(2).messagesThat()
            .hasPattern("Sprite sheet image at '$VALID_SPRITE_SHEET_LOCATION' results in empty sprite sheet.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    @VerifiesLoggingCalls
    fun getDataFor_animationNameIsInvalid_returnsNullAndLogsMessage(logSystem: VerifiableLogSystem) {
        val animationManager: DataManager<AnimationFactory> = parse(data)!!

        assertThat(animationManager.getDataFor(INVALID_ANIMATION_NAME)).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Animation data does not exist for name '$INVALID_ANIMATION_NAME'.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    fun getDataFor_cachesAnimationFactories() {
        val animationManager: DataManager<AnimationFactory> = parse(data)!!

        val animationFactory1 = animationManager.getDataFor(ANIMATION_1_NAME)!!
        val animationFactory2 = animationManager.getDataFor(ANIMATION_1_NAME)!!

        assertThat(animationFactory1).isSameInstanceAs(animationFactory2)
    }

    companion object {
        private const val VALID_SPRITE_SHEET_LOCATION = "some valid location"
        private const val INVALID_SPRITE_SHEET_LOCATION = "not a valid location"
        private const val ANIMATION_1_NAME = "animation 1"
        private const val ANIMATION_2_NAME = "animation 2"
        private const val INVALID_ANIMATION_NAME = "invalid animation name"
        private const val LOADED_IMAGE_WIDTH = 5
        private const val LOADED_IMAGE_HEIGHT = 2
        private const val NUM_FRAMES = LOADED_IMAGE_WIDTH * LOADED_IMAGE_HEIGHT
        private val LOADED_IMAGE = FakeImage(LOADED_IMAGE_WIDTH, LOADED_IMAGE_HEIGHT)
    }
}
