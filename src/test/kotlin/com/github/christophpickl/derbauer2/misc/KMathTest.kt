package com.github.christophpickl.derbauer2.misc

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

@Test
class KMathTest {
    fun `min min`() {
        assertThat(KMath.min(1, 2)).isEqualTo(1)
        assertThat(KMath.min(4, 3)).isEqualTo(3)
        assertThat(KMath.min(4, 3, 2)).isEqualTo(2)
        assertThat(KMath.min(4, 3, 2, 1)).isEqualTo(1)

        assertThat(KMath.minButNotNegative(-1, -2)).isEqualTo(0)
    }
}
