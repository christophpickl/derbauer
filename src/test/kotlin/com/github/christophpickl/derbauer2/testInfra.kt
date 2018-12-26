package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.action.Actions
import com.github.christophpickl.derbauer2.endturn.Notifications
import com.github.christophpickl.derbauer2.endturn.achievement.Achievements
import com.github.christophpickl.derbauer2.feature.Features
import com.github.christophpickl.derbauer2.home.HomeView
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
        Model._reset()

        Model.player.resources.all.forEach { it.amount = 0 }
        Model.people = 1
        Model.land = 1

        Model.player.buildings.all.forEach { it.amount = 0 }
        Model.player.militaries.all.forEach { it.amount = 0 }
    }
}

fun Model._reset() {
    currentView = HomeView()
    player = Player()
    global = Global()
    history = History()
    features = Features()
    achievements = Achievements()
    actions = Actions()
    notifications = Notifications()
}

infix fun Amountable.hasSameAmountAs(expected: Int) {
    assertThat(amount).isEqualTo(expected)
}
