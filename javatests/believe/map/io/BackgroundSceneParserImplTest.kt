package believe.map.io

import believe.gui.ImageSupplier
import believe.gui.testing.FakeImage
import believe.map.data.BackgroundSceneData
import believe.map.data.proto.MapMetadataProto.MapBackground
import com.google.common.truth.Truth8.assertThat
import org.junit.jupiter.api.Test
import org.newdawn.slick.Image
import org.newdawn.slick.SlickException
import java.util.*

internal class BackgroundSceneParserImplTest {
    @Test
    @Throws(SlickException::class)
    fun load_returnsValidBackgroundSceneData() {
        val backgroundImage = FakeImage(0, 0)
        val backgroundSceneParser =
            BackgroundSceneParserImpl(FakeImageSupplier(Optional.of(backgroundImage)))

        val backgroundSceneData = backgroundSceneParser.load(MAP_BACKGROUND)

        assertThat(backgroundSceneData).hasValue(
            BackgroundSceneData.create(
                backgroundImage, MAP_BACKGROUND
            )
        )
    }

    @Test
    fun load_emptyImageIsSupplied_returnsEmptyAndLogsError() {
        val backgroundSceneParser = BackgroundSceneParserImpl(FakeImageSupplier(Optional.empty()))

        assertThat(backgroundSceneParser.load(MAP_BACKGROUND)).isEmpty()
    }

    companion object {
        private val MAP_BACKGROUND =
            MapBackground.newBuilder().setFileLocation("file location").setBottomYPosition(1f)
                .setTopYPosition(0f).setHorizontalSpeedMultiplier(1f).build()
    }

    private class FakeImageSupplier(private val returnValue: Optional<Image>) : ImageSupplier {
        override fun get(fileLocation: String): Optional<Image> = returnValue
    }
}
