package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.feature.Feature
import com.github.christophpickl.derbauer2.home.HomeScreen
import com.github.christophpickl.derbauer2.model.Amountable
import com.github.christophpickl.derbauer2.model.Global
import com.github.christophpickl.derbauer2.model.History
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.Player
import org.assertj.core.api.Assertions.assertThat
import org.testng.ITestResult
import org.testng.TestListenerAdapter

class TestModelListener : TestListenerAdapter() {
    override fun onTestStart(testContext: ITestResult) {
        Model.reset()

        Model.player.resources.all.forEach { it.amount = 0 }
        Model.people = 1
        Model.land = 1

        Model.player.buildings.all.forEach { it.amount = 0 }
        Model.player.militaries.all.forEach { it.amount = 0 }
    }
}

fun Model.reset() {
    screen = HomeScreen()
    global = Global()
    player = Player()
    history = History()
    feature = Feature()
}

infix fun Amountable.hasSameAmountAs(expected: Int) {
    assertThat(amount).isEqualTo(expected)
}
