package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.action.Actions
import com.github.christophpickl.derbauer.endturn.achievement.Achievements
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.military.attack.Military
import com.github.christophpickl.derbauer.misc.Features
import com.github.christophpickl.derbauer.misc.Notifications
import com.github.christophpickl.derbauer.model.Global
import com.github.christophpickl.derbauer.model.History
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.Player
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.model.amount.Amountable
import mu.KotlinLogging.logger
import org.assertj.core.api.AbstractDoubleAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ObjectAssert
import org.testng.ITestResult
import org.testng.TestListenerAdapter

class TestModelListener : TestListenerAdapter() {

    private val log = logger {}
    
    override fun onTestStart(testContext: ITestResult) {
        log.trace { "Resetting model for tests. Just one house, granary and people; nothing else." }
        Model.reset()

        Model.player.resources.all.forEach { it.amount = Amount.zero }
        Model.people = Amount.one
        Model.land = Amount.two

        Model.player.buildings.all.forEach { it.amount = Amount.zero }
        Model.player.armies.all.forEach { it.amount = Amount.zero }

        Model.player.buildings.houses.amount = Amount.one
        Model.player.buildings.granaries.amount = Amount.one
        Model.player.resources.people.amount = Amount.one
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
    military = Military()
}

fun Model.nullify() {
    player.resources.all.forEach {
        it.amount = Amount.zero
    }
    player.buildings.all.forEach {
        it.amount = Amount.zero
    }
    player.upgrades.all.forEach {
        it.currentLevel = 0
    }
    player.armies.all.forEach {
        it.amount = Amount.zero
    }
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

fun assertAverage(captor: () -> Int): AbstractDoubleAssert<*> = assertThat(1.rangeTo(1_000).map {
    captor()
}.average())
