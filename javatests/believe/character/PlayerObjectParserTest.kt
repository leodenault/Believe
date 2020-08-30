package believe.character

import believe.character.proto.CharacterAnimationsProto
import believe.datamodel.MutableValue
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.map.data.EntityType
import believe.map.data.GeneratedMapEntityData
import believe.map.tiled.testing.TiledFakes.fakeTiledObject
import believe.physics.manager.PhysicsManager
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
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
        on { create(any(), any(), any(), any(), any()) } doReturn expectedPlayer
    }
    private val parser = PlayerObjectParser(characterFactory, parseAnimations, mutablePlayer)
    private val generatedMapEntityDataBuilder = GeneratedMapEntityData.newBuilder()

    @Test
    fun parseObject_correctlyParsesPlayer() {
        val physicsManager = mock<PhysicsManager>()

        parser.parseObject(
            EntityType.PLAYER,
            fakeTiledObject(type = "", name = "Some name", x = 123f, y = 234f),
            generatedMapEntityDataBuilder
        )

        with(generatedMapEntityDataBuilder.build()) {
            assertThat(physicsManageables()).hasSize(1)
            assertThat(physicsManageables().first().addToPhysicsManager(physicsManager))
            assertThat(sceneElements()).contains(expectedPlayer)
        }
        assertThat(mutablePlayer.get()).isEqualTo(expectedPlayer)
        verify(characterFactory).create(any(), any(), eq(123f), eq(234f), eq(Faction.GOOD))
        verify(physicsManager).addCollidable(expectedPlayer)
        verify(physicsManager).addGravityObject(expectedPlayer)
    }

    @Test
    fun parseObject_entityTypeIsNotPlayer_doesNothing() {
        parser.parseObject(
            EntityType.COLLIDABLE_TILE,
            fakeTiledObject("", name = "Some name"),
            generatedMapEntityDataBuilder
        )

        with(generatedMapEntityDataBuilder.build()) {
            assertThat(physicsManageables()).isEmpty()
            assertThat(sceneElements()).isEmpty()
        }
        assertThat(mutablePlayer.get()).isEqualTo(initialPlayer)
    }

    @Test
    @VerifiesLoggingCalls
    fun parseObject_noNameExists_logsError(logSystem: VerifiableLogSystem) {
        parser.parseObject(
            EntityType.PLAYER, fakeTiledObject(""), generatedMapEntityDataBuilder
        )

        with(generatedMapEntityDataBuilder.build()) {
            assertThat(physicsManageables()).isEmpty()
            assertThat(sceneElements()).isEmpty()
        }
        assertThat(mutablePlayer.get()).isEqualTo(initialPlayer)
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .containsExactly("Expected player object to have a name.")
            .hasSeverity(LogSeverity.ERROR)
    }
}
