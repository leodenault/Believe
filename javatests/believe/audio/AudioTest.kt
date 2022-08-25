package believe.audio

import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stubbing
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.newdawn.slick.Music
import org.newdawn.slick.SlickException
import org.newdawn.slick.Sound

internal class AudioTest {
    private val slickSound: Sound = mock {}
    private val slickMusic: Music = mock {}

    @Test
    internal fun soundFrom_returnsSoundInstance() {
        soundFrom(VALID_FILE_LOCATION, ::provideSlickSound).load()?.play()

        verify(slickSound).play()
    }

    @Test
    @VerifiesLoggingCalls
    internal fun soundFrom_soundFailsToLoad_returnsNullAndLogsError(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(soundFrom("not a valid location", ::provideSlickSound).load()).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .containsExactly("Could not load sound at 'not a valid location'.")
    }

    @Test
    internal fun musicFrom_returnsMusicInstance() {
        musicFrom(VALID_FILE_LOCATION, ::provideSlickMusic).load()?.play()

        verify(slickMusic).play()
    }

    @Test
    internal fun musicFrom_isPaused_whenMusicIsPausedInMiddle_returnsTrue() {
        stubbing(slickMusic) {
            on { playing() } doReturn false
            on { position } doReturn 0.23f
        }

        assertThat(musicFrom(VALID_FILE_LOCATION, ::provideSlickMusic).load()?.isPaused()).isTrue()
    }

    @Test
    internal fun musicFrom_isPaused_whenMusicIsPausedAtBeginning_returnsFalse() {
        stubbing(slickMusic) {
            on { playing() } doReturn false
            on { position } doReturn 0f
        }

        assertThat(musicFrom(VALID_FILE_LOCATION, ::provideSlickMusic).load()?.isPaused()).isFalse()
    }

    @Test
    internal fun musicFrom_isPaused_whenMusicIsPlaying_returnsFalse() {
        stubbing(slickMusic) {
            on { playing() } doReturn true
            on { position } doReturn 0.23f
        }

        assertThat(musicFrom(VALID_FILE_LOCATION, ::provideSlickMusic).load()?.isPaused()).isFalse()
    }

    @Test
    @VerifiesLoggingCalls
    internal fun musicFrom_musicFailsToLoad_returnsNullAndLogsError(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(musicFrom("not a valid location", ::provideSlickMusic).load()).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .containsExactly("Could not load music at 'not a valid location'.")
    }

    private fun provideSlickSound(fileLocation: String) =
        provideSlickInstance(fileLocation, slickSound)

    private fun provideSlickMusic(fileLocation: String) =
        provideSlickInstance(fileLocation, slickMusic)

    private fun <S> provideSlickInstance(fileLocation: String, slickInstance: S): S =
        if (fileLocation == VALID_FILE_LOCATION) {
            slickInstance
        } else throw SlickException("Exception was not caught")

    companion object {
        private const val VALID_FILE_LOCATION = "some file location"
    }
}
