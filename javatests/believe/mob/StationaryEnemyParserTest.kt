package believe.mob

import believe.animation.Animation
import believe.datamodel.DataManager
import believe.map.data.EntityType
import believe.map.data.GeneratedMapEntityData
import believe.map.tiled.testing.TiledFakes.fakeTiledObject
import believe.physics.damage.DamageBoxCollisionHandler
import believe.physics.damage.DamageBoxFactory
import believe.physics.manager.PhysicsManager
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import javax.xml.crypto.Data

internal class StationaryEnemyParserTest {
    private val animationManager = mock<DataManager<Animation>>()
    private val spriteSheetManager = mock<DataManager<DataManager<Animation>>> {
        on { getDataFor("stationary_enemy") } doReturn animationManager
    }
    private val mobAnimationManager = mock<DataManager<MobAnimation>>()
    private val mobSpriteSheetManager = mock<DataManager<DataManager<MobAnimation>>> {
        on { getDataFor("stationary_enemy") } doReturn mobAnimationManager
    }
    private val physicsManager = mock<PhysicsManager>()
    private val stationaryEnemyFactory = StationaryEnemy.Factory(physicsManager,
        DamageBoxFactory { DamageBoxCollisionHandler(physicsManager) })
    private val parser =
        StationaryEnemyParser(spriteSheetManager, mobSpriteSheetManager, stationaryEnemyFactory)

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

        verify(spriteSheetManager).getDataFor("stationary_enemy")
        verify(mobSpriteSheetManager).getDataFor("stationary_enemy")
        verify(animationManager).getDataFor("idle")
        verify(mobAnimationManager).getDataFor("attacking")
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
