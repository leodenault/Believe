package believe.mob

import believe.animation.Animation
import believe.datamodel.DataManager
import believe.map.data.EntityType
import believe.map.data.GeneratedMapEntityData
import believe.map.tiled.testing.TiledFakes.fakeTiledObject
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class StationaryEnemyParserTest {
    private val spriteSheetManager = mock<DataManager<Animation>>()
    private val animationsManager = mock<DataManager<DataManager<Animation>>> {
        on { getDataFor("stationary_enemy") } doReturn spriteSheetManager
    }
    private val parser = StationaryEnemyParser(animationsManager)

    @Test
    fun parse_addsStationaryEnemyToPhysicsManager() {
        val generatedMapEntityDataBuilder = GeneratedMapEntityData.newBuilder()

        parser.parseObject(
            EntityType.ENEMY, fakeTiledObject(x = 123f, y = 234f), generatedMapEntityDataBuilder
        )

        val generatedMapEntityData = generatedMapEntityDataBuilder.build()
        assertThat(generatedMapEntityData.sceneElements()).hasSize(1)
        with(generatedMapEntityData.sceneElements().first()) {
            assertThat(x).isEqualTo(123f)
            assertThat(y).isEqualTo(234f)
        }
    }

    @Test
    fun parse_fetchesEnemyAnimations() {
        parser.parseObject(
            EntityType.ENEMY,
            fakeTiledObject(x = 123f, y = 234f),
            GeneratedMapEntityData.newBuilder()
        )

        verify(animationsManager).getDataFor("stationary_enemy")
        verify(spriteSheetManager).getDataFor("idle")
        verify(spriteSheetManager).getDataFor("attacking")
    }

    @Test
    fun parse_entityIsNotEnemy_doesNothing() {
        val generatedMapEntityDataBuilder = GeneratedMapEntityData.newBuilder()

        parser.parseObject(
            EntityType.PLAYER, fakeTiledObject(x = 123f, y = 234f), generatedMapEntityDataBuilder
        )

        val generatedMapEntityData = generatedMapEntityDataBuilder.build()
        assertThat(generatedMapEntityData.sceneElements()).isEmpty()
    }
}
