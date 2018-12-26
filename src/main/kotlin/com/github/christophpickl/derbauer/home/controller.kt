package com.github.christophpickl.derbauer.home

import com.github.christophpickl.derbauer.action.ActionView
import com.github.christophpickl.derbauer.build.BuildView
import com.github.christophpickl.derbauer.endturn.EndTurnExecutor
import com.github.christophpickl.derbauer.endturn.EndTurnView
import com.github.christophpickl.derbauer.endturn.achievement.AchievementChecker
import com.github.christophpickl.derbauer.military.MilitaryView
import com.github.christophpickl.derbauer.misc.enforceWhenBranches
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.trade.TradeView
import com.github.christophpickl.derbauer.upgrade.UpgradeView

class HomeController : HomeCallback {

    private val endTurn = EndTurnExecutor()
    
    override fun goEndTurnReport() {
        val report = endTurn.execute()
        Model.currentView = EndTurnView(report)
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

}
