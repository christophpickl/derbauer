package com.github.christophpickl.derbauer2.home

import com.github.christophpickl.derbauer2.build.BuildView
import com.github.christophpickl.derbauer2.endturn.EndTurnExecutor
import com.github.christophpickl.derbauer2.endturn.EndTurnView
import com.github.christophpickl.derbauer2.endturn.achievement.AchievementChecker
import com.github.christophpickl.derbauer2.military.MilitaryView
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.trade.TradeView
import com.github.christophpickl.derbauer2.upgrade.UpgradeView

class HomeController : HomeCallback {
    override fun goEndTurnReport() {
        val message = EndTurnExecutor.execute()
        Model.view = EndTurnView(message)
    }

    override fun onHomeEnum(choice: HomeChoice) {
        when (choice.enum) {
            HomeEnum.Trade -> {
                Model.view = TradeView()
            }
            HomeEnum.Build -> {
                Model.view = BuildView()
            }
            HomeEnum.Upgrade -> {
                Model.view = UpgradeView()
            }
            HomeEnum.Military -> {
                Model.view = MilitaryView()
            }
            HomeEnum.EndTurn -> {
                val achievement = AchievementChecker.nextView()
                if (achievement != null) {
                    Model.view = achievement
                } else {
                    goEndTurnReport()
                }
            }
        }.enforceWhenBranches()
    }

}
