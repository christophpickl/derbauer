package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.action.Actions
import com.github.christophpickl.derbauer.endturn.Notifications
import com.github.christophpickl.derbauer.endturn.achievement.Achievements
import com.github.christophpickl.derbauer.feature.Features
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.Amountable
import com.github.christophpickl.derbauer.model.Global
import com.github.christophpickl.derbauer.model.History
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.Player
import com.github.christophpickl.kpotpourri.common.random.RandomService
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

// TODO [KPOT] move to kpot test infra
object RandomServiceMinimum : RandomService {

    override fun randomize(base: Int, from: Double, to: Double) =
        (base * from).toInt()

    override fun nextInt(fromInclusive: Int, toExclusive: Int) =
        fromInclusive

    override fun nextDouble(fromInclusive: Double, toExclusive: Double) =
        fromInclusive
}

