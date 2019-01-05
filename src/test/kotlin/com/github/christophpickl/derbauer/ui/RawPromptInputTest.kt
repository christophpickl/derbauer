package com.github.christophpickl.derbauer.ui

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

@Test
class RawPromptInputTest {

    fun `When entered empty Then return Empty`() {
        assertThat(RawPromptInput.by("")).isSameAs(RawPromptInput.Empty)
    }

    fun `When number entered Then return Number`() {
        assertThat(RawPromptInput.by("42")).isEqualTo(RawPromptInput.Number(42L))
    }

    fun `When amount number entered Then return Number`() {
        assertThat(RawPromptInput.by("1k")).isEqualTo(RawPromptInput.Number(1_000L))
    }

    fun `When invalid text entered Then return null`() {
        assertThat(RawPromptInput.by("a")).isEqualTo(RawPromptInput.Invalid("a"))
        assertThat(RawPromptInput.by("1a")).isEqualTo(RawPromptInput.Invalid("1a"))
    }

}
