package believe.character

import believe.character.proto.CharacterAnimationsProto
import believe.datamodel.MutableValue
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.map.data.testing.Truth.assertThat
import believe.map.tiled.testing.TiledFakes.fakeTiledObject
import believe.physics.manager.PhysicsManageable
import believe.physics.manager.PhysicsManager
import com.google.common.truth.Correspondence
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class PlayerObjectParserTest {
    private val initialPlayer = mock<CharacterV2>()
    private val expectedPlayer = mock<CharacterV2>()
    private val mutablePlayer = MutableValue.of(initialPlayer)
    private val parseAnimations =
        mock<(CharacterAnimationsProto.CharacterAnimations) -> Animations> {
            on { invoke(any()) } doReturn Animations.EMPTY
        }
    private val characterFactory = mock<CharacterV2.Factory> {
        on { create(any(), any(), any(), any()) } doReturn expectedPlayer
    }
    private val parser = PlayerObjectParser(characterFactory, parseAnimations, mutablePlayer)

    @Test
    fun parseObject_correctlyParsesPlayer() {

        val objectFactory =
            parser.parseObject(fakeTiledObject(type = "", name = "Some name", x = 123f, y = 234f))

        assertThat(objectFactory).outputPhysicsManageableSet().comparingElementsUsing(
            PLAYER_CORRESPONDENCE
        ).containsExactly(expectedPlayer)
        assertThat(objectFactory).outputSceneElementSet().containsExactly(expectedPlayer)
        assertThat(mutablePlayer.get()).isEqualTo(expectedPlayer)
    }

    @Test
    @VerifiesLoggingCalls
    fun parseObject_noNameExists_logsError(logSystem: VerifiableLogSystem) {
        val objectFactory = parser.parseObject(fakeTiledObject(""))

        assertThat(objectFactory).outputPhysicsManageableSet().isEmpty()
        assertThat(objectFactory).outputSceneElementSet().isEmpty()
        assertThat(mutablePlayer.get()).isEqualTo(initialPlayer)
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .containsExactly("Expected player object to have a name.")
            .hasSeverity(LogSeverity.ERROR)
    }

    companion object {
        private val PLAYER_CORRESPONDENCE: Correspondence<PhysicsManageable, CharacterV2> =
            Correspondence.from({ manageable, player ->
                val physicsManager = mock<PhysicsManager>()
                manageable?.addToPhysicsManager(physicsManager)
                verify(physicsManager).addCollidable(player)
                verify(physicsManager).addGravityObject(player)
                true
            }, "")
    }
}
