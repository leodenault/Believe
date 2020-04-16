package believe.util

import believe.util.MapEntry.Companion.entry
import believe.util.Util.changeClipContext
import believe.util.Util.hashMapOf
import believe.util.Util.hashSetOf
import believe.util.Util.immutableMapOf
import believe.util.Util.immutableSetOf
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Rectangle

class UtilTest {
    private val g: Graphics = mock()

    @Test
    fun changeClipContext_returnsOldClipAndSetsNewClipOnGraphics() {
        val expectedParentClip = Rectangle(0f, 0f, 100f, 100f)
        val newClip = believe.geometry.Rectangle(50f, 50f, 100f, 100f)
        whenever(g.clip).thenReturn(expectedParentClip)

        val actualParentClip = g.changeClipContext(newClip)

        with(actualParentClip!!) {
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(100f)
            assertThat(height).isEqualTo(100f)
        }
        verify(g).clip = check {
            with(it) {
                assertThat(x).isEqualTo(50f)
                assertThat(y).isEqualTo(50f)
                assertThat(width).isEqualTo(100f)
                assertThat(height).isEqualTo(100f)
            }
        }
    }

    @Test
    fun changeClipContext_parentIsNull_returnsNull() {
        whenever(g.clip).thenReturn(null)

        val oldClip = g.changeClipContext(believe.geometry.Rectangle(123f, 123f, 123f, 123f))

        assertThat(oldClip).isNull()
    }

    @Test
    fun hashSetOf_returnsProperHashSet() {
        assertThat(hashSetOf("one", "two", "three")).containsAllOf("one", "two", "three")
    }

    @Test
    fun immutableSetOf_cannotBeModified() {
        assertThat(immutableSetOf("one", "two", "three")).containsAllOf("one", "two", "three")
    }

    @Test
    fun hashMapOf_returnsCorrectHashMap() {
        val expectedMap = hashMapOf(
            Pair("key", "value"), Pair("key2", "value2")
        )

        assertThat(
            hashMapOf(
                entry("key", "value"), entry("key2", "value2")
            )
        ).containsExactlyEntriesIn(expectedMap)
    }

    @Test
    fun immutableMapOf_cannotBeModified() {
        val expectedMap = hashMapOf(
            Pair("key", "value"), Pair("key2", "value2")
        )

        assertThat(
            immutableMapOf(
                entry("key", "value"), entry("key2", "value2")
            )
        ).containsExactlyEntriesIn(expectedMap)
    }
}