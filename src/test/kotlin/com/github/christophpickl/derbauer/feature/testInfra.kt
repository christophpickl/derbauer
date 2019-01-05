package com.github.christophpickl.derbauer.feature

import com.github.christophpickl.derbauer.misc.Feature
import org.assertj.core.api.Assertions
import org.assertj.core.api.ObjectAssert

fun <F : Feature> ObjectAssert<F>.isEnabled() {
    satisfies {
        Assertions.assertThat(it.isEnabled()).describedAs("Expected feature to be enabled").isTrue()
    }
}
