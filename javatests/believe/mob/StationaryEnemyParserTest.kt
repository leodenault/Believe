package believe.mob

import believe.animation.AnimationFactory
import believe.datamodel.DataManager
import believe.geometry.FloatPoint
import believe.geometry.floatPoint
import believe.map.data.testing.Truth.assertThat
import believe.map.tiled.testing.TiledFakes.fakeTiledObject
import believe.physics.damage.DamageBoxCollisionHandler
import believe.physics.damage.DamageBoxFactory
import believe.physics.manager.PhysicsManager
import believe.scene.GeneratedMapEntityData
import com.google.common.truth.Correspondence
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class StationaryEnemyParserTest {
    private val animationManager = mock<DataManager<AnimationFactory>>()
    private val mobAnimationManager = mock<DataManager<MobAnimationData>>()
    private val mobSpriteSheetManager = mock<DataManager<DataManager<MobAnimationData>>> {
        on { getDataFor("stationary_enemy") } doReturn mobAnimationManager
    }
    private val physicsManager = mock<PhysicsManager>()
    private val stationaryEnemyFactory = StationaryEnemy.Factory(
        physicsManager,
        DamageBoxFactory { DamageBoxCollisionHandler(physicsManager) })
    private val parser = StationaryEnemyParser(mobSpriteSheetManager, stationaryEnemyFactory)

    @Test
    fun parse_addsStationaryEnemyToPhysicsManager() {
        val objectFactory = parser.parseObject(fakeTiledObject(x = 123f, y = 234f))

        assertThat(objectFactory).outputSceneElementSet()
            .comparingElementsUsing(STATIONARY_ENEMY_CORRESPONDENCE).containsExactly(
                EnemyPositionData(
                    floatPoint(123f, 234f)
                )
            )
    }

    @Test
    fun parse_fetchesEnemyAnimations() {
        parser.parseObject(fakeTiledObject(x = 123f, y = 234f))

        verify(mobSpriteSheetManager).getDataFor("stationary_enemy")
        verify(mobAnimationManager).getDataFor("attacking")
    }

    @Test
    fun parse_entityIsNotEnemy_doesNothing() {
        val generatedMapEntityDataBuilder = GeneratedMapEntityData.newBuilder()

        parser.parseObject(fakeTiledObject(x = 123f, y = 234f))

        val generatedMapEntityData = generatedMapEntityDataBuilder.build()
        assertThat(generatedMapEntityData.sceneElements()).isEmpty()
    }

    private class EnemyPositionData(val coords: FloatPoint)

    companion object {
        private val STATIONARY_ENEMY_CORRESPONDENCE: Correspondence<StationaryEnemy, EnemyPositionData> =
            Correspondence.from({ actual, data ->
                actual?.x == data?.coords?.x && actual?.y == data?.coords?.y
            }, "")
    }
}
