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
import com.github.christophpickl.derbauer2.ui.Alert
import com.github.christophpickl.derbauer2.ui.AlertType
import com.github.christophpickl.derbauer2.upgrade.UpgradeView

class HomeController : HomeCallback {

    private val endTurn = EndTurnExecutor()
    
    override fun goEndTurnReport() {
        val report = endTurn.execute()
        Alert.show(AlertType.FullyUpgraded)
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
