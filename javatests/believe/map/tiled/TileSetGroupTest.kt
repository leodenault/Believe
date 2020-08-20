package believe.map.tiled

import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test
import java.util.Properties

internal class TileSetGroupTest {
    @Test
    fun tileSetForGid_tileSetExistsForGid_returnsTileSet() {
        val expectedTileSet = mock<TileSet> { on { contains(any()) } doReturn true }
        val group = TileSetGroup {
            add(mock { on { contains(any<Int>()) } doReturn false })
            add(expectedTileSet)
        }

        assertThat(group.tileSetForGid(123)).isEqualTo(expectedTileSet)
    }

    @Test
    @VerifiesLoggingCalls
    fun tileSetForGid_tileSetDoesNotExistForGid_logsWarningAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        val tileSet = mock<TileSet> { on { contains(any()) } doReturn false }
        val group = TileSetGroup {
            add(tileSet)
            add(tileSet)
            add(tileSet)
            add(tileSet)
            add(tileSet)
        }

        assertThat(group.tileSetForGid(123)).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.WARNING
        ).containsExactly("No tile set containing tile with GID=123 was found.")
    }
}
