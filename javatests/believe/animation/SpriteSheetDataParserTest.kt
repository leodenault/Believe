package believe.animation

import believe.animation.proto.AnimationProto
import believe.datamodel.DataManager
import believe.gui.testing.FakeImage
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.newdawn.slick.Animation
import org.newdawn.slick.Color
import org.newdawn.slick.SpriteSheet
import java.lang.RuntimeException

internal class SpriteSheetDataParserTest {
    private var spriteSheet: SpriteSheet = mock {
        (0 until LOADED_IMAGE_WIDTH).forEach { x ->
            (0 until LOADED_IMAGE_HEIGHT).forEach { y ->
                on { getSprite(x, y) } doReturn FakeImage(1, 1, listOf(listOf(COLOURS[x][y])))
            }
        }
        on { horizontalCount } doReturn LOADED_IMAGE_WIDTH
        on { verticalCount } doReturn LOADED_IMAGE_HEIGHT
    }
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
        }.build())
        putAnimations(ANIMATION_2_NAME, AnimationProto.Animation.newBuilder().apply {
            startFrame = NUM_FRAMES / 2
            endFrame = NUM_FRAMES - 1
            duration = 15
        }.build())
    }
    private val data: AnimationProto.SpriteSheet by lazy { dataBuilder.build() }

    @Test
    fun invoke_correctlyGeneratesAnimationDataManager() {
        val animationManager: DataManager<Animation> = parse(data)!!

        val animation1: Animation = animationManager.getDataFor(ANIMATION_1_NAME)!!
        val animation2: Animation = animationManager.getDataFor(ANIMATION_2_NAME)!!
        (0 until LOADED_IMAGE_WIDTH).forEach { x ->
            (0 until LOADED_IMAGE_HEIGHT / 2).forEach { y ->
                animation1.setCurrentFrame(x + y * LOADED_IMAGE_WIDTH)
                assertThat(animation1.currentFrame.getColor(0, 0)).isEqualTo(Color(COLOURS[x][y]))
            }
            (LOADED_IMAGE_HEIGHT / 2 until LOADED_IMAGE_HEIGHT).forEach { y ->
                animation2.setCurrentFrame(x + (y - LOADED_IMAGE_HEIGHT / 2) * LOADED_IMAGE_WIDTH)
                assertThat(animation2.currentFrame.getColor(0, 0)).isEqualTo(Color(COLOURS[x][y]))
            }
        }
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
        spriteSheet = mock {
            on { horizontalCount } doReturn 0
        }

        assertThat(parse(data)).isNull()

        spriteSheet = mock {
            on { verticalCount } doReturn 0
        }

        assertThat(parse(data)).isNull()

        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeast(2).messagesThat()
            .hasPattern("Sprite sheet image at '$VALID_SPRITE_SHEET_LOCATION' results in empty sprite sheet.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    @VerifiesLoggingCalls
    fun getDataFor_animationDataInvalid_returnsNullAndLogsMessage(logSystem: VerifiableLogSystem) {
        spriteSheet = mock {
            on { getSprite(any(), any()) } doThrow RuntimeException()
            on { horizontalCount } doReturn LOADED_IMAGE_WIDTH
            on { verticalCount } doReturn LOADED_IMAGE_HEIGHT
        }

        val animationManager: DataManager<Animation> = parse(data)!!

        assertThat(animationManager.getDataFor(ANIMATION_1_NAME)).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Animation data is invalid(?s).*")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    @VerifiesLoggingCalls
    fun getDataFor_animationNameIsInvalid_returnsNullAndLogsMessage(logSystem: VerifiableLogSystem) {
        val animationManager: DataManager<Animation> = parse(data)!!

        assertThat(animationManager.getDataFor(INVALID_ANIMATION_NAME)).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Animation data does not exist for name '$INVALID_ANIMATION_NAME'.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    fun getDataFor_cachesAnimations() {
        val animationManager: DataManager<Animation> = parse(data)!!

        animationManager.getDataFor(ANIMATION_1_NAME)
        animationManager.getDataFor(ANIMATION_1_NAME)

        verify(
            spriteSheet, times(LOADED_IMAGE_WIDTH * LOADED_IMAGE_HEIGHT / 2)
        ).getSprite(any(), any())
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
        private val COLOURS = (0 until LOADED_IMAGE_WIDTH).map { x ->
            (0 until LOADED_IMAGE_HEIGHT).map { y -> LOADED_IMAGE_WIDTH * x + y }
        }
        private val LOADED_IMAGE = FakeImage(LOADED_IMAGE_WIDTH, LOADED_IMAGE_HEIGHT, COLOURS)
    }
}
