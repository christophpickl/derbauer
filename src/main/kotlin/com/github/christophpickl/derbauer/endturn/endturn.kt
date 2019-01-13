package com.github.christophpickl.derbauer.endturn

import com.github.christophpickl.derbauer.data.Messages
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.resource.ResourceEndTurn
import com.github.christophpickl.kpotpourri.common.random.RandomService
import com.github.christophpickl.kpotpourri.common.random.RealRandomService
import mu.KotlinLogging.logger

class EndTurnExecutor(
    private val random: RandomService = RealRandomService
) {

    private val log = logger {}
    private val resourceEndTurner = ResourceEndTurn()

    fun execute(): EndTurnReport {
        log.debug { "execute end turn" }
        val resourceReport = resourceEndTurner.endTurn()
        Model.gold += resourceReport.goldIncome
        Model.food += resourceReport.foodIncome
        Model.people += resourceReport.peopleIncome

        Model.global.day++
        val newVisitors = random.nextInt(0, Math.max(2, (Model.people.real / 100.0).toInt()))
        Model.actions.visitorsWaitingInThroneRoom += newVisitors
        Model.features.checkAndNotifyAll()
        Model.features.upgrade.menu.checkAndNotify() // do it again, as it itself depends on other upgrades to be enabled
        adjustKarma()
        loseArmyIfGoldNegative(resourceReport.goldIncome)
        Messages.onEndTurn()

        return EndTurnReport(
            goldIncome = resourceReport.goldIncome,
            foodIncome = resourceReport.foodIncome,
            peopleIncome = resourceReport.peopleIncome
        )
    }

    private fun loseArmyIfGoldNegative(goldIncome: Amount) {
        if (Model.gold.real < 0 && Model.player.armies.totalAmount.isNotZero) {
            val armyType = Model.player.armies.all.filter { it.amount.isNotZero }.random()
            val countRunAway = Amount(Math.min(armyType.amount.real, Math.max(1,
                // restore 25% of negative income by army upkeep
                (Math.abs(goldIncome.real) / armyType.upkeep.real / 100.0 * 25.0 * random.nextDouble(0.8, 1.2)).toLong())))
            val label = if (countRunAway.real == 1L) armyType.labelSingular else armyType.labelPlural
            armyType.amount -= countRunAway
            log.trace {
                "${armyType.labelPlural} lost ${countRunAway.real} amount due to negative gold. " +
                    "(gold=${Model.gold.real}, goldIncome=${goldIncome.real}, armyTypeUpkeep=${armyType.upkeep.real})"
            }
            Model.notifications.add("Because of lack of gold ${countRunAway.formatted} $label left you")
        }
    }

    private fun adjustKarma() {
        val currentKarma = Model.global.karma
        if (currentKarma == 0.0) {
            return
        }
        val balancer = Values.karma.karmaTurnBalancer
        if (currentKarma > 0.0) {
            if (currentKarma <= balancer) {
                Model.global.karma = 0.0
            } else {
                Model.global.karma -= balancer
            }
        }
        if (currentKarma < 0.0) {
            if (currentKarma >= -balancer) {
                Model.global.karma = 0.0
            } else {
                Model.global.karma += balancer
            }
        }
    }

}

class EndTurnReport(
    goldIncome: Amount,
    foodIncome: Amount,
    peopleIncome: Amount
) {
    val gold = EndTurnReportLine(
        newValue = Model.gold,
        change = goldIncome,
        oldValue = Model.gold - goldIncome
    )
    val food = EndTurnReportLine(
        newValue = Model.food,
        change = foodIncome,
        oldValue = Model.food - foodIncome
    )
    val people = EndTurnReportLine(
        newValue = Model.people,
        change = peopleIncome,
        oldValue = Model.people - peopleIncome
    )
}

class EndTurnReportLine(
    val oldValue: Amount,
    val newValue: Amount,
    val change: Amount
)
