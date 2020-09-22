package believe.mob

import believe.map.data.EntityType
import believe.map.data.GeneratedMapEntityData
import believe.map.tiled.testing.TiledFakes.fakeTiledObject
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class StationaryEnemyParserTest {
    private val parser = StationaryEnemyParser()

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
    fun parse_entityIsNotEnemy_doesNothing() {
        val generatedMapEntityDataBuilder = GeneratedMapEntityData.newBuilder()

        parser.parseObject(
            EntityType.PLAYER, fakeTiledObject(x = 123f, y = 234f), generatedMapEntityDataBuilder
        )

        val generatedMapEntityData = generatedMapEntityDataBuilder.build()
        assertThat(generatedMapEntityData.sceneElements()).isEmpty()
    }
}
