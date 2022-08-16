package believe.audio

import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.newdawn.slick.SlickException
import org.newdawn.slick.Sound

internal class LoadableSoundTest {
    private val slickSound: Sound = mock {}
    private val factory = LoadableSound.Factory {
        if (it == VALID_FILE_LOCATION) {
            slickSound
        } else throw SlickException("Exception was not caught")
    }

    @Test
    internal fun load_returnsSoundInstance() {
        factory.create(VALID_FILE_LOCATION).load()?.play()

        verify(slickSound).play()
    }

    @Test
    @VerifiesLoggingCalls
    internal fun load_soundFailsToLoad_returnsNullAndLogsError(logSystem: VerifiableLogSystem) {
        assertThat(factory.create("not a valid location").load()).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .containsExactly("Could not load sound at 'not a valid location'.")
    }

    companion object {
        private const val VALID_FILE_LOCATION = "some file location"
    }
}
