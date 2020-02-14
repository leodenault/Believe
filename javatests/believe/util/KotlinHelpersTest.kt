package believe.util

import believe.util.KotlinHelpers.whenEmpty
import believe.util.KotlinHelpers.whenNull
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test
import java.util.*

internal class KotlinHelpersTest {
    private val whenEmptyOrNull: () -> Unit = mock()

    @Test
    fun whenEmpty_isPresent_doesNotInvokeEmptyFunctionAndReturnsValue() {
        assertThat(Optional.of("67").whenEmpty(whenEmptyOrNull)).isEqualTo("67")
        verifyZeroInteractions(whenEmptyOrNull)
    }

    @Test
    fun whenEmpty_isEmpty_invokesEmptyFunctionAndReturnsNull() {
        assertThat(Optional.empty<String>().whenEmpty(whenEmptyOrNull)).isNull()
        verify(whenEmptyOrNull).invoke()
    }

    @Test
    fun whenNull_isNotNull_doesNotInvokeNullFunctionAndReturnsValue() {
        assertThat("67".whenNull(whenEmptyOrNull)).isEqualTo("67")
        verifyZeroInteractions(whenEmptyOrNull)
    }

    @Test
    fun fold_forNullable_isNull_invokesNullFunction() {
        assertThat((null as String?).whenNull(whenEmptyOrNull)).isNull()
        verify(whenEmptyOrNull).invoke()
    }
}