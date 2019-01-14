package com.github.christophpickl.derbauer.model.amount

import com.github.christophpickl.derbauer.DEV_MODE_PROPERTY
import mu.KotlinLogging.logger
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

@Test
class AmountTest {

    @BeforeClass
    fun `init system property`() {
        System.setProperty(DEV_MODE_PROPERTY, "1")
    }

    @AfterClass
    fun `reset system property`() {
        System.clearProperty(DEV_MODE_PROPERTY)
    }

    fun `Given non-annotated method When invoking toString Then throw`() {
        Assertions.assertThatThrownBy {
            Amount(1).toString()
        }.isExactlyInstanceOf(UnsupportedOperationException::class.java)
    }

    @AmountToStringAllowed
    fun `Given annotated method When invoking toString Then dont throw`() {
        Amount(1).toString()
    }

    @AmountToStringAllowed
    fun `Given annotated method invoking other method When invoking toString Then dont throw`() {
        otherMethod()
    }

    private fun otherMethod() {
        Amount(1).toString()
    }

    fun `When log amount using toString Then succeed`() {
        val log = logger {}
        var logSucceeded = false
        log.debug { Amount(1).toString(); logSucceeded = true; "" }
        assertThat(logSucceeded).isTrue()
    }

}
