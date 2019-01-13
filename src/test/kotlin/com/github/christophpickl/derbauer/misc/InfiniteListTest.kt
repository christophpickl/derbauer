package com.github.christophpickl.derbauer.misc

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

@Test
class InfiniteListTest {

    fun `cycle through`() {
        val list = infiniteListOf(1, 2)

        val actual = 1.rangeTo(5).map { list.nextInfinite() }

        assertThat(actual).isEqualTo(listOf(1, 2, 1, 2, 1))
    }

}
