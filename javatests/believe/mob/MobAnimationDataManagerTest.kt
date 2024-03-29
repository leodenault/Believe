package believe.mob

import believe.animation.AnimationFactory
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.fakeAnimation
import believe.datamodel.DataManager
import believe.mob.proto.MobAnimationDataProto.DamageFrame
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test

internal class MobAnimationDataManagerTest {
    private val spriteSheetManager = mock<DataManager<DataManager<AnimationFactory>>>()
    private val mobDataManager = mock<DataManager<DataManager<List<DamageFrame>>>>()
    private val manager = MobAnimationDataManager(spriteSheetManager, mobDataManager)

    @Test
    fun getDataFor_returnsMobAnimationManagerWithValidAnimations() {
        val animation = fakeAnimation(IterationMode.PING_PONG, isLooping = true)
        val damageFrames = listOf(
            DamageFrame.newBuilder().setFrameIndex(0).build(),
            DamageFrame.newBuilder().setFrameIndex(1).build()
        )
        val animationManager = mock<DataManager<AnimationFactory>> {
            on { getDataFor(any()) } doReturn { animation }
        }
        val damageFrameManager = mock<DataManager<List<DamageFrame>>> {
            on { getDataFor(any()) } doReturn damageFrames
        }
        whenever(spriteSheetManager.getDataFor(any())) doReturn animationManager
        whenever(mobDataManager.getDataFor(any())) doReturn damageFrameManager

        val mobAnimationData: MobAnimationData =
            manager.getDataFor("some sprite sheet")!!.getDataFor("some animation")!!

        assertThat(mobAnimationData.createAnimation()).isEqualTo(animation)
        assertThat(mobAnimationData.damageFrames).containsExactlyElementsIn(damageFrames)
    }

    @Test
    fun getDataFor_animationManagerIsNull_returnsNull() {
        val damageFrames = listOf(
            DamageFrame.newBuilder().setFrameIndex(0).build(),
            DamageFrame.newBuilder().setFrameIndex(1).build()
        )
        val damageFrameManager = mock<DataManager<List<DamageFrame>>> {
            on { getDataFor(any()) } doReturn damageFrames
        }
        whenever(mobDataManager.getDataFor(any())) doReturn damageFrameManager

        val mobAnimationManager = manager.getDataFor("some sprite sheet")

        assertThat(mobAnimationManager).isNull()
    }

    @Test
    fun getDataFor_animationIsNull_returnsNullForAnimation() {
        val damageFrames = listOf(
            DamageFrame.newBuilder().setFrameIndex(0).build(),
            DamageFrame.newBuilder().setFrameIndex(1).build()
        )
        val animationManager = mock<DataManager<AnimationFactory>>()
        val damageFrameManager = mock<DataManager<List<DamageFrame>>> {
            on { getDataFor(any()) } doReturn damageFrames
        }
        whenever(spriteSheetManager.getDataFor(any())) doReturn animationManager
        whenever(mobDataManager.getDataFor(any())) doReturn damageFrameManager

        val mobAnimationData: MobAnimationData? =
            manager.getDataFor("some sprite sheet")!!.getDataFor("some animation")

        assertThat(mobAnimationData).isNull()
    }

    @Test
    fun getDataFor_damageFrameManagerIsNull_returnsEmptyFrames() {
        val animation = fakeAnimation(IterationMode.PING_PONG, isLooping = true)
        val animationManager = mock<DataManager<AnimationFactory>> {
            on { getDataFor(any()) } doReturn { animation }
        }
        whenever(spriteSheetManager.getDataFor(any())) doReturn animationManager

        val mobAnimationData: MobAnimationData =
            manager.getDataFor("some sprite sheet")!!.getDataFor("some animation")!!

        assertThat(mobAnimationData.createAnimation()).isEqualTo(animation)
        assertThat(mobAnimationData.damageFrames).isEmpty()
    }

    @Test
    fun getDataFor_mobAnimationIsNull_returnsEmptyFramesAndLogsError() {
        val animation = fakeAnimation(IterationMode.PING_PONG, isLooping = true)
        val animationManager = mock<DataManager<AnimationFactory>> {
            on { getDataFor(any()) } doReturn { animation }
        }
        val damageFrameManager = mock<DataManager<List<DamageFrame>>>()
        whenever(spriteSheetManager.getDataFor(any())) doReturn animationManager
        whenever(mobDataManager.getDataFor(any())) doReturn damageFrameManager

        val mobAnimationData: MobAnimationData =
            manager.getDataFor("some sprite sheet")!!.getDataFor("some animation")!!

        assertThat(mobAnimationData.createAnimation()).isEqualTo(animation)
        assertThat(mobAnimationData.damageFrames).isEmpty()
    }
}
