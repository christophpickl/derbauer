package com.github.christophpickl.derbauer2

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

@Test
class Foo {

    fun `foo bar`() {
        assertThat(3).isEqualTo(3)
    }

}
