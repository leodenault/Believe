package believe.datamodel

import com.google.common.truth.Truth.assertThat

import org.junit.jupiter.api.Test

internal class MutableValueTest {
    private val mutableValue = MutableValue.of("initial value")

    @Test
    fun get_noUpdates_returnsInitialValue() {
        assertThat(mutableValue.get()).isEqualTo("initial value")
    }

    @Test
    fun get_valueUpdated_returnsUpdatedValue() {
        mutableValue.update("updated value")

        assertThat(mutableValue.get()).isEqualTo("updated value")
    }
}
