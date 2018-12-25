package com.github.christophpickl.derbauer2.home

import com.github.christophpickl.derbauer2.action.ActionView
import com.github.christophpickl.derbauer2.build.BuildView
import com.github.christophpickl.derbauer2.endturn.EndTurnExecutor
import com.github.christophpickl.derbauer2.endturn.EndTurnView
import com.github.christophpickl.derbauer2.endturn.achievement.AchievementChecker
import com.github.christophpickl.derbauer2.military.MilitaryView
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.trade.TradeView
import com.github.christophpickl.derbauer2.ui.Formatter
import com.github.christophpickl.derbauer2.upgrade.UpgradeView

class HomeController : HomeCallback {

    override fun goEndTurnReport() {
        val report = EndTurnExecutor.execute()
        val message = """
            So, this is what happened over night:
            
            ${formatGrowth("Gold income    ", Model.gold, report.goldIncome)}
            ${formatGrowth("Food production", Model.food, report.foodIncome)}
            ${formatGrowth("People growth  ", Model.people, report.peopleIncome)}
             
            Go on and continue your miserable existence.
            """.trimIndent()
        Model.currentView = EndTurnView(message)
    }

    override fun onHomeEnum(choice: HomeChoice) {
        when (choice.enum) {
            HomeEnum.Trade -> {
                Model.currentView = TradeView()
            }
            HomeEnum.Build -> {
                Model.currentView = BuildView()
            }
            HomeEnum.Upgrade -> {
                Model.currentView = UpgradeView()
            }
            HomeEnum.Military -> {
                Model.currentView = MilitaryView()
            }
            HomeEnum.Action -> {
                Model.currentView = ActionView()
            }
            HomeEnum.EndTurn -> {
                val achievement = AchievementChecker.nextView()
                if (achievement != null) {
                    Model.currentView = achievement
                } else {
                    goEndTurnReport()
                }
            }
        }.enforceWhenBranches()
    }

    private val numberWidth = 6
    private val numberWidthGrow = 5 // plus sign
    private fun formatGrowth(label: String, current: Int, growth: Int) =
        "$label: ${Formatter.formatNumber(current, numberWidth)} => " +
            "${Formatter.formatNumber(growth, numberWidthGrow, addPlusSign = true)} => " +
            Formatter.formatNumber(current + growth, numberWidth)

}
