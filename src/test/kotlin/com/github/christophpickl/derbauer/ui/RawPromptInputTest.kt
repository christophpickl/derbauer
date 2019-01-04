package com.github.christophpickl.derbauer.ui

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

@Test
class RawPromptInputTest {

    fun `When entered empty Then return Empty`() {
        assertThat(RawPromptInput.by("")).isSameAs(PromptInput.Empty)
    }

    fun `When number entered Then return Number`() {
        assertThat(RawPromptInput.by("42")).isEqualTo(PromptInput.Number(42L))
    }

    fun `When amount number entered Then return Number`() {
        assertThat(RawPromptInput.by("1k")).isEqualTo(PromptInput.Number(1_000L))
    }

    fun `When invalid text entered Then return null`() {
        assertThat(RawPromptInput.by("a")).isNull()
        assertThat(RawPromptInput.by("1a")).isNull()
    }

}
