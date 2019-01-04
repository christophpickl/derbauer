package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.action.Actions
import com.github.christophpickl.derbauer.endturn.Notifications
import com.github.christophpickl.derbauer.endturn.achievement.Achievements
import com.github.christophpickl.derbauer.feature.Features
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Amountable
import com.github.christophpickl.derbauer.model.Global
import com.github.christophpickl.derbauer.model.History
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.Player
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ObjectAssert
import org.testng.ITestResult
import org.testng.TestListenerAdapter

class TestModelListener : TestListenerAdapter() {
    override fun onTestStart(testContext: ITestResult) {
        Model.reset()

        Model.player.resources.all.forEach { it.amount = Amount.zero }
        Model.people = Amount.one
        Model.land = Amount.one

        Model.player.buildings.all.forEach { it.amount = Amount.zero }
        Model.player.militaries.all.forEach { it.amount = Amount.zero }
    }
}

fun Model.reset() {
    currentView = HomeView()
    player = Player()
    global = Global()
    history = History()
    features = Features()
    achievements = Achievements()
    actions = Actions()
    notifications = Notifications()
}

infix fun Amountable.hasSameAmountAs(expected: Long) {
    assertThat(amount.real).isEqualTo(expected)
}

infix fun Amountable.hasSameAmountAs(expected: Amount) {
    assertThat(amount).isEqualTo(expected)
}

fun ObjectAssert<Amount>.isAmountEqualTo(expected: Long) {
    satisfies {
        assertThat(it.real).isEqualTo(expected)
    }
}
