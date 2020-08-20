package believe.map.tiled

import believe.map.tiled.testing.fakeElement
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class ElementParserTest {
    @Test
    fun stringAttributeParser_succeeds_returnsCorrectString() {
        assertThat(
            stringAttributeParser(
                "some_key"
            )(fakeElement(attributes = arrayOf("some_key" to "some_value")))
        ).isEqualTo("some_value")
    }

    @Test
    fun stringAttributeParser_fails_returnsNull() {
        assertThat(stringAttributeParser("some_key")(fakeElement())).isNull()
    }

    @Test
    fun integerAttributeParser_succeeds_returnsCorrectInteger() {
        assertThat(
            integerAttributeParser(
                "some_key"
            )(fakeElement(attributes = arrayOf("some_key" to "123")))
        ).isEqualTo(123)
    }

    @Test
    fun integerAttributeParser_fails_returnsNull() {
        assertThat(integerAttributeParser("some_key")(fakeElement())).isNull()
        assertThat(
            integerAttributeParser(
                "some_key"
            )(fakeElement(attributes = arrayOf("some_key" to "abcd")))
        ).isNull()
    }

    @Test
    fun floatAttributeParser_succeeds_returnsCorrectFloat() {
        assertThat(
            floatAttributeParser(
                "some_key"
            )(fakeElement(attributes = arrayOf("some_key" to "123.321")))
        ).isEqualTo(123.321f)
    }

    @Test
    fun floatAttributeParser_fails_returnsNull() {
        assertThat(integerAttributeParser("some_key")(fakeElement())).isNull()
        assertThat(
            integerAttributeParser(
                "some_key"
            )(fakeElement(attributes = arrayOf("some_key" to "abcd")))
        ).isNull()
    }

    @Test
    fun attributeParser_succeeds_returnsCorrectValue() {
        assertThat(
            attributeParser(
                "some_key"
            ) { it.toByteArray() }(fakeElement(attributes = arrayOf("some_key" to "some_value")))
        ).isEqualTo("some_value".toByteArray())
    }

    @Test
    fun attributeParser_fails_returnsNull() {
        assertThat(attributeParser("some_key") { it.toByteArray() }(fakeElement())).isNull()
        assertThat(
            attributeParser(
                "some_key"
            ) { null as ByteArray? }(fakeElement(attributes = arrayOf("some_key" to "abcd")))
        ).isNull()
    }

    @Test
    fun byCombining_returnsParserCombiningSubParsers() {
        val parsedContents = {
            mutableListOf<Int>()
        }.byCombining(integerAttributeParser("key1") withSetter { add(it) },
            integerAttributeParser("key2") withSetter { add(it * 2) })(
            fakeElement(attributes = arrayOf("key1" to "1", "key2" to "2"))
        )

        assertThat(parsedContents).containsExactly(1, 4)
    }

    @Test
    fun byCombining_nothingToParse_doesNotChangeReceiver() {
        val parsedContents = {
            mutableListOf<Int>()
        }.byCombining(integerAttributeParser("key1") withSetter { add(it) },
            integerAttributeParser("key2") withSetter { add(it * 2) })(
            fakeElement(attributes = arrayOf("key3" to "3"))
        )

        assertThat(parsedContents).isEmpty()
    }

    @Test
    fun accumulating_correctlyAccumulatesMultipleNodes() {
        val parsedContents = { arrayOf(0, 1, 2, 3) }.byAccumulating("accumulated_tag").withParser(
            integerAttributeParser("some_key")
        ).reduce { this[it!! % this.size] = it }(
            fakeElement(
                children = arrayOf(
                    fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "4")
                    ), fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "5")
                    ), fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "6")
                    ), fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "7")
                    ), fakeElement(
                        tagName = "non_accumulated_tag", attributes = arrayOf("some_key" to "8")
                    ), fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "9")
                    )
                )
            )
        )

        assertThat(parsedContents).asList().containsExactly(4, 9, 6, 7)
    }

    @Test
    fun accumulating_receiverIsList_correctlyAccumulatesMultipleNodes() {
        val parsedContents = { mutableListOf<Int?>() }.byAccumulating("accumulated_tag").withParser(
            integerAttributeParser("some_key")
        )(
            fakeElement(
                children = arrayOf(
                    fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "4")
                    ), fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "5")
                    ), fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "6")
                    ), fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "7")
                    ), fakeElement(
                        tagName = "non_accumulated_tag", attributes = arrayOf("some_key" to "8")
                    ), fakeElement(
                        tagName = "accumulated_tag", attributes = arrayOf("some_key" to "9")
                    )
                )
            )
        )

        assertThat(parsedContents).containsExactly(4, 5, 6, 7, 9)
    }
}
